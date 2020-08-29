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
    
    public NovTranslate getTranslateBean(String jsonString) {
        NovTranslate bean = new NovTranslate();
        bean.setCom("naver");
        bean.setCde("ok");
        bean.setMsg(JsonParser.parseString(jsonString).getAsJsonObject().get("message").getAsJsonObject().get("result").getAsJsonObject().get("translatedText").getAsString());
        return bean;
    }

    public Mono<String> getErrorBean(Throwable err) {
        NovTranslate bean = new NovTranslate();
        bean.setCom("naver");
        bean.setCde("err");
        bean.setMsg(err.toString());
        return Mono.just(new Gson().toJson(bean));
    }

    /**
     * 한국어 -> 영어
     */
    public Mono<NovTranslate> ko2En(String txt) {
        return con.post()
            .body(BodyInserters.fromFormData("source", NaverLang.KOR).with("target", NaverLang.ENG).with("text", txt))
            .retrieve()
            .bodyToMono(String.class)
            .onErrorResume(err->getErrorBean(err))
            .map(i->getTranslateBean(i));
    }

    /**
     * 한국어 -> 중국어
     */
    public Mono<NovTranslate> ko2Cn(String txt) {
        return con.post()
            .body(BodyInserters.fromFormData("source", NaverLang.KOR).with("target", NaverLang.CHN).with("text", txt))
            .retrieve()
            .bodyToMono(String.class)
            .onErrorResume(err->getErrorBean(err))
            .map(i->getTranslateBean(i));
    }

    /**
     * 한국어 -> 일본어
     */
    public Mono<NovTranslate> ko2Ja(String txt) {
        return con.post()
            .body(BodyInserters.fromFormData("source", NaverLang.KOR).with("target", NaverLang.JPN).with("text", txt))
            .retrieve()
            .bodyToMono(String.class)
            .onErrorResume(err->getErrorBean(err))
            .map(i->getTranslateBean(i));
    }

    /**
     * 영어 -> 한국어
     */
    public Mono<NovTranslate> en2Ko(String txt) {
        return con.post()
            .body(BodyInserters.fromFormData("source", NaverLang.ENG).with("target", NaverLang.KOR).with("text", txt))
            .retrieve()
            .bodyToMono(String.class)
            .onErrorResume(err->getErrorBean(err))
            .map(i->getTranslateBean(i));
    }

    /**
     * 영어 -> 중국어
     */
    public Mono<NovTranslate> en2Cn(String txt) {
        return con.post()
            .body(BodyInserters.fromFormData("source", NaverLang.ENG).with("target", NaverLang.CHN).with("text", txt))
            .retrieve()
            .bodyToMono(String.class)
            .onErrorResume(err->getErrorBean(err))
            .map(i->getTranslateBean(i));
    }

    /**
     * 영어 -> 일본어
     */
    public Mono<NovTranslate> en2Ja(String txt) {
        return con.post()
            .body(BodyInserters.fromFormData("source", NaverLang.ENG).with("target", NaverLang.JPN).with("text", txt))
            .retrieve()
            .bodyToMono(String.class)
            .onErrorResume(err->getErrorBean(err))
            .map(i->getTranslateBean(i));
    }

    /**
     * 중국어 -> 한국어
     */
    public Mono<NovTranslate> cn2Ko(String txt) {
        return con.post()
            .body(BodyInserters.fromFormData("source", NaverLang.CHN).with("target", NaverLang.KOR).with("text", txt))
            .retrieve()
            .bodyToMono(String.class)
            .onErrorResume(err->getErrorBean(err))
            .map(i->getTranslateBean(i));
    }

    /**
     * 중국어 -> 영어
     */
    public Mono<NovTranslate> cn2En(String txt) {
        return con.post()
            .body(BodyInserters.fromFormData("source", NaverLang.CHN).with("target", NaverLang.ENG).with("text", txt))
            .retrieve()
            .bodyToMono(String.class)
            .onErrorResume(err->getErrorBean(err))
            .map(i->getTranslateBean(i));
    }

    /**
     * 중국어 -> 일본어
     */
    public Mono<NovTranslate> cn2Ja(String txt) {
        return con.post()
            .body(BodyInserters.fromFormData("source", NaverLang.CHN).with("target", NaverLang.JPN).with("text", txt))
            .retrieve()
            .bodyToMono(String.class)
            .onErrorResume(err->getErrorBean(err))
            .map(i->getTranslateBean(i));
    }

    /**
     * 일본어 -> 한국어
     */
    public Mono<NovTranslate> ja2Ko(String txt) {
        return con.post()
            .body(BodyInserters.fromFormData("source", NaverLang.JPN).with("target", NaverLang.KOR).with("text", txt))
            .retrieve()
            .bodyToMono(String.class)
            .onErrorResume(err->getErrorBean(err))
            .map(i->getTranslateBean(i));
    }

    /**
     * 일본어 -> 영어
     */
    public Mono<NovTranslate> ja2En(String txt) {
        return con.post()
            .body(BodyInserters.fromFormData("source", NaverLang.JPN).with("target", NaverLang.ENG).with("text", txt))
            .retrieve()
            .bodyToMono(String.class)
            .onErrorResume(err->getErrorBean(err))
            .map(i->getTranslateBean(i));
    }

    /**
     * 일본어 -> 중국어
     */
    public Mono<NovTranslate> ja2Cn(String txt) {
        return con.post()
            .body(BodyInserters.fromFormData("source", NaverLang.JPN).with("target", NaverLang.CHN).with("text", txt))
            .retrieve()
            .bodyToMono(String.class)
            .onErrorResume(err->getErrorBean(err))
            .map(i->getTranslateBean(i));
    }

}