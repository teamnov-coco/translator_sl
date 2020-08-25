package com.teamnov.translator.service.translator.naver;

import javax.annotation.PostConstruct;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.teamnov.translator.dto.NovTranslate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Service
public class NaverService {
    
    @Value("${app.api.naver.url}")
    private String NAVER_URL;

    @Value("${app.api.naver.client-id}")
    private String NAVER_CLIENT_ID;

    @Value("${app.api.naver.client-secret}")
    private String NAVER_CLIENT_SECRET;

    @PostConstruct
    private void init() {
        con = WebClient.builder()
            .baseUrl(NAVER_URL)
            .defaultHeader("X-Naver-Client-Id", NAVER_CLIENT_ID)
            .defaultHeader("X-Naver-Client-Secret", NAVER_CLIENT_SECRET)
            .build();
    }

    private WebClient con;

    public Mono<NovTranslate> kor2Eng(String txt) {
        return con.post()
            .body(BodyInserters.fromFormData("source", "ko").with("target", "en").with("text", txt))
            .retrieve()
            .bodyToMono(String.class)
            .onErrorResume(err->{
                NovTranslate bean = new NovTranslate();
                bean.setCom("naver");
                bean.setCde("err");
                bean.setMsg(err.toString());
                return Mono.just(new Gson().toJson(bean));
            })
            .map(i->{
                NovTranslate bean = new NovTranslate();
                bean.setCom("naver");
                bean.setCde("ok");
                bean.setMsg(JsonParser.parseString(i).getAsJsonObject().get("message").getAsJsonObject().get("result").getAsJsonObject().get("translatedText").getAsString());
                return bean;
            });
    }

    public Mono<NovTranslate> eng2Kor(String txt) {
        return con.post()
            .body(BodyInserters.fromFormData("source", "en").with("target", "ko").with("text", txt))
            .retrieve()
            .bodyToMono(String.class)
            .onErrorResume(err->{
                NovTranslate bean = new NovTranslate();
                bean.setCom("naver");
                bean.setCde("err");
                bean.setMsg(err.toString());
                return Mono.just(new Gson().toJson(bean));
            })
            .map(i->{
                NovTranslate bean = new NovTranslate();
                bean.setCom("naver");
                bean.setCde("ok");
                bean.setMsg(JsonParser.parseString(i).getAsJsonObject().get("message").getAsJsonObject().get("result").getAsJsonObject().get("translatedText").getAsString());
                return bean;
            });
    }

}