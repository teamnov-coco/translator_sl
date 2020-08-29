package com.teamnov.translator.controller.translator;

import com.teamnov.translator.dto.NovTranslate;
import com.teamnov.translator.service.translator.google.GoogleService;
import com.teamnov.translator.service.translator.kakao.KakaoService;
import com.teamnov.translator.service.translator.naver.NaverService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;


@RestController
public class TranslatorController {
    
    @Autowired
    private KakaoService kakaoService;

    @Autowired
    private NaverService naverService;

    @Autowired
    private GoogleService googleService;

    /**
     * 한국어 -> 영어
     */ 
    @GetMapping(path = "/ko2en", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<NovTranslate> ko2En(@RequestParam(value = "txt", required = true) String txt) {
        return Flux.merge(
            kakaoService.ko2En(txt),
            naverService.ko2En(txt),
            googleService.ko2En(txt)
        );
    }

    /**
     * 한국어 -> 중국어
     */ 
    @GetMapping(path = "/ko2cn", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<NovTranslate> ko2Cn(@RequestParam(value = "txt", required = true) String txt) {
        return Flux.merge(
            kakaoService.ko2Cn(txt),
            naverService.ko2Cn(txt),
            googleService.ko2Cn(txt)
        );
    }

    /**
     * 한국어 -> 일본어
     */ 
    @GetMapping(path = "/ko2ja", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<NovTranslate> ko2Ja(@RequestParam(value = "txt", required = true) String txt) {
        return Flux.merge(
            kakaoService.ko2Ja(txt),
            naverService.ko2Ja(txt),
            googleService.ko2Ja(txt)
        );
    }

    /**
     * 영어 -> 한국어
     */ 
    @GetMapping(path = "/en2ko", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<NovTranslate> en2Ko(@RequestParam(value = "txt", required = true) String txt) {
        return Flux.merge(
            kakaoService.en2Ko(txt),
            naverService.en2Ko(txt),
            googleService.en2Ko(txt)
        );
    }

    /**
     * 영어 -> 중국어
     */ 
    @GetMapping(path = "/en2cn", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<NovTranslate> en2Cn(@RequestParam(value = "txt", required = true) String txt) {
        return Flux.merge(
            kakaoService.en2Cn(txt),
            naverService.en2Cn(txt),
            googleService.en2Cn(txt)
        );
    }

    /**
     * 영어 -> 일본어
     */ 
    @GetMapping(path = "/en2ja", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<NovTranslate> en2Ja(@RequestParam(value = "txt", required = true) String txt) {
        return Flux.merge(
            kakaoService.en2Ja(txt),
            naverService.en2Ja(txt),
            googleService.en2Ja(txt)
        );
    }

    /**
     * 중국어 -> 한국어
     */ 
    @GetMapping(path = "/cn2ko", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<NovTranslate> cn2Ko(@RequestParam(value = "txt", required = true) String txt) {
        return Flux.merge(
            kakaoService.cn2Ko(txt),
            naverService.cn2Ko(txt),
            googleService.cn2Ko(txt)
        );
    }

    /**
     * 중국어 -> 영어
     */ 
    @GetMapping(path = "/cn2en", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<NovTranslate> cn2En(@RequestParam(value = "txt", required = true) String txt) {
        return Flux.merge(
            kakaoService.cn2En(txt),
            naverService.cn2En(txt),
            googleService.cn2En(txt)
        );
    }

    /**
     * 중국어 -> 일본어
     */ 
    @GetMapping(path = "/cn2ja", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<NovTranslate> cn2Ja(@RequestParam(value = "txt", required = true) String txt) {
        return Flux.merge(
            kakaoService.cn2Ja(txt),
            naverService.cn2Ja(txt),
            googleService.cn2Ja(txt)
        );
    }

    /**
     * 일본어 -> 한국어
     */ 
    @GetMapping(path = "/ja2ko", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<NovTranslate> ja2Ko(@RequestParam(value = "txt", required = true) String txt) {
        return Flux.merge(
            kakaoService.ja2Ko(txt),
            naverService.ja2Ko(txt),
            googleService.ja2Ko(txt)
        );
    }

    /**
     * 일본어 -> 영어
     */ 
    @GetMapping(path = "/ja2en", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<NovTranslate> ja2En(@RequestParam(value = "txt", required = true) String txt) {
        return Flux.merge(
            kakaoService.ja2En(txt),
            naverService.ja2En(txt),
            googleService.ja2En(txt)
        );
    }

    /**
     * 일본어 -> 중국어
     */ 
    @GetMapping(path = "/ja2cn", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<NovTranslate> ja2Cn(@RequestParam(value = "txt", required = true) String txt) {
        return Flux.merge(
            kakaoService.ja2Cn(txt),
            naverService.ja2Cn(txt),
            googleService.ja2Cn(txt)
        );
    }

}