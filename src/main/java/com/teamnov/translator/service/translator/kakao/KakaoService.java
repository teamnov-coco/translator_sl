package com.teamnov.translator.service.translator.kakao;

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
public class KakaoService {

    @Value("${app.api.kakao.url}")
    private String KAKAO_URL;

    @Value("${app.api.kakao.rest-api-key}")
    private String KAKAO_REST_API_KEY;

    @PostConstruct
    private void init() {
        con = WebClient.builder()
            .baseUrl(KAKAO_URL)
            .defaultHeader("Authorization", "KakaoAK " + KAKAO_REST_API_KEY)
            .build();
    }

    private WebClient con;

    public Mono<NovTranslate> kor2Eng(String txt) {
        return con.post()
            .body(BodyInserters.fromFormData("src_lang", "kr").with("target_lang", "en").with("query", txt))
            .retrieve()
            .bodyToMono(String.class)
            .onErrorResume(err->{
                NovTranslate bean = new NovTranslate();
                bean.setCom("kakao");
                bean.setCde("err");
                bean.setMsg(err.toString());
                return Mono.just(new Gson().toJson(bean));
            })
            .map(i->{
                NovTranslate bean = new NovTranslate();
                bean.setCom("kakao");
                bean.setCde("ok");
                bean.setMsg(JsonParser.parseString(i).getAsJsonObject().get("translated_text").getAsJsonArray().getAsString());
                return bean;
            });
    }

    public Mono<NovTranslate> eng2Kor(String txt) {
        return con.post()
            .body(BodyInserters.fromFormData("src_lang", "en").with("target_lang", "kr").with("query", txt))
            .retrieve()
            .bodyToMono(String.class)
            .onErrorResume(err->{
                NovTranslate bean = new NovTranslate();
                bean.setCom("kakao");
                bean.setCde("err");
                bean.setMsg(err.toString());
                return Mono.just(new Gson().toJson(bean));
            })
            .map(i->{
                NovTranslate bean = new NovTranslate();
                bean.setCom("kakao");
                bean.setCde("ok");
                bean.setMsg(JsonParser.parseString(i).getAsJsonObject().get("translated_text").getAsJsonArray().getAsString());
                return bean;
            });
    }

}