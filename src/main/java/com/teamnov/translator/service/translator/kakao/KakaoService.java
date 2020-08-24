package com.teamnov.translator.service.translator.kakao;

import java.util.Map;

import javax.annotation.PostConstruct;

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

    public Mono<Map<String, Object>> kor2Eng(String txt) {
        return con.post()
            .body(BodyInserters.fromFormData("src_lang", "kr").with("target_lang", "en").with("query", txt))
            .retrieve()
            .bodyToFlux(Object.class)
            .collectMap(i->{return "kakao";}, i->{return i;});
    }

    public Mono<Map<String, Object>> eng2Kor(String txt) {
        return con.post()
            .body(BodyInserters.fromFormData("src_lang", "en").with("target_lang", "kr").with("query", txt))
            .retrieve()
            .bodyToFlux(Object.class)
            .collectMap(i->{return "kakao";}, i->{return i;});
    }

}