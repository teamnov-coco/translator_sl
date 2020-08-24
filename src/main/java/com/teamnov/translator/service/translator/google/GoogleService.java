package com.teamnov.translator.service.translator.google;

import java.util.HashMap;
import java.util.Map;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translate.TranslateOption;
import com.google.cloud.translate.TranslateOptions;

import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

@Service
public class GoogleService {
    
    private Translate translate = TranslateOptions.getDefaultInstance().getService();

    public Mono<Map<String, Object>> kor2Eng(String txt) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("google", translate.translate(txt, TranslateOption.sourceLanguage("ko"), TranslateOption.targetLanguage("en")).getTranslatedText());
        return Mono.just(map);
    }

    public Mono<Map<String, Object>> eng2Kor(String txt) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("google", translate.translate(txt, TranslateOption.sourceLanguage("en"), TranslateOption.targetLanguage("ko")).getTranslatedText());
        return Mono.just(map);
    }

}