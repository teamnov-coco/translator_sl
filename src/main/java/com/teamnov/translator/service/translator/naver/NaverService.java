package com.teamnov.translator.service.translator.naver;

import java.util.Map;

import javax.annotation.PostConstruct;

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

    public Mono<Map<String, Object>> kor2Eng(String txt) {
        return con.post()
            .body(BodyInserters.fromFormData("source", "ko").with("target", "en").with("text", txt))
            .retrieve()
            .bodyToFlux(Object.class)
            .collectMap(i->{return "naver";}, i->{return i;});
    }

    public Mono<Map<String, Object>> eng2Kor(String txt) {
        return con.post()
            .body(BodyInserters.fromFormData("source", "en").with("target", "kor").with("text", txt))
            .retrieve()
            .bodyToFlux(Object.class)
            .collectMap(i->{return "naver";}, i->{return i;});
    }

}