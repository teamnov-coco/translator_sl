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

    public NovTranslate getTranslateBean(String jsonString) {
        NovTranslate bean = new NovTranslate();
        bean.setCom("kakao");
        bean.setCde("ok");
        bean.setMsg(JsonParser.parseString(jsonString).getAsJsonObject().get("translated_text").getAsJsonArray().getAsString());
        return bean;
    }

    public Mono<String> getErrorBean(Throwable err) {
        NovTranslate bean = new NovTranslate();
        bean.setCom("kakao");
        bean.setCde("err");
        bean.setMsg(err.toString());
        return Mono.just(new Gson().toJson(bean));
    }

    /**
     * 한국어 -> 영어
     */
    public Mono<NovTranslate> ko2En(String txt) {
        return con.post()
            .body(BodyInserters.fromFormData("src_lang", KakaoLang.KOR).with("target_lang", KakaoLang.ENG).with("query", txt))
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
            .body(BodyInserters.fromFormData("src_lang", KakaoLang.KOR).with("target_lang", KakaoLang.CHN).with("query", txt))
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
            .body(BodyInserters.fromFormData("src_lang", KakaoLang.KOR).with("target_lang", KakaoLang.JPN).with("query", txt))
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
            .body(BodyInserters.fromFormData("src_lang", KakaoLang.ENG).with("target_lang", KakaoLang.KOR).with("query", txt))
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
            .body(BodyInserters.fromFormData("src_lang", KakaoLang.ENG).with("target_lang", KakaoLang.CHN).with("query", txt))
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
            .body(BodyInserters.fromFormData("src_lang", KakaoLang.ENG).with("target_lang", KakaoLang.JPN).with("query", txt))
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
            .body(BodyInserters.fromFormData("src_lang", KakaoLang.CHN).with("target_lang", KakaoLang.KOR).with("query", txt))
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
            .body(BodyInserters.fromFormData("src_lang", KakaoLang.CHN).with("target_lang", KakaoLang.ENG).with("query", txt))
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
            .body(BodyInserters.fromFormData("src_lang", KakaoLang.CHN).with("target_lang", KakaoLang.JPN).with("query", txt))
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
            .body(BodyInserters.fromFormData("src_lang", KakaoLang.JPN).with("target_lang", KakaoLang.KOR).with("query", txt))
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
            .body(BodyInserters.fromFormData("src_lang", KakaoLang.JPN).with("target_lang", KakaoLang.ENG).with("query", txt))
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
            .body(BodyInserters.fromFormData("src_lang", KakaoLang.JPN).with("target_lang", KakaoLang.CHN).with("query", txt))
            .retrieve()
            .bodyToMono(String.class)
            .onErrorResume(err->getErrorBean(err))
            .map(i->getTranslateBean(i));
    }

}