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
    
    @GetMapping(path = "/kor2eng", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<NovTranslate> kor2Eng(@RequestParam(value = "txt", required = true) String txt) {
        return Flux.merge(kakaoService.kor2Eng(txt), naverService.kor2Eng(txt), googleService.kor2Eng(txt));
    }

    @GetMapping(path = "/eng2kor", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<NovTranslate> eng2Kor(@RequestParam(value = "txt", required = true) String txt) {
        return Flux.merge(kakaoService.eng2Kor(txt), naverService.eng2Kor(txt), googleService.eng2Kor(txt));
    }

}