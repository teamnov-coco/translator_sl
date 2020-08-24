package com.teamnov.translator.controller.translator.google;

import java.util.Map;

import com.teamnov.translator.service.translator.google.GoogleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/google")
public class GoogleTranslatorController {
    
    @Autowired
    private GoogleService googleService;

    @GetMapping(path = "/kor2eng", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Map<String, Object>> kor2Eng(@RequestParam("txt") String txt) {
        return googleService.kor2Eng(txt);
    }

    @GetMapping(path = "/eng2kor", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Map<String, Object>> eng2Kor(@RequestParam("txt") String txt) {
        return googleService.eng2Kor(txt);
    }

}