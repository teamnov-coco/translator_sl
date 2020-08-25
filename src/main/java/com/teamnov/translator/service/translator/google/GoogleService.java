package com.teamnov.translator.service.translator.google;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translate.TranslateOption;
import com.google.cloud.translate.TranslateOptions;
import com.teamnov.translator.dto.NovTranslate;

import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

@Service
public class GoogleService {
    
    private Translate translate = TranslateOptions.getDefaultInstance().getService();

    public Mono<NovTranslate> kor2Eng(String txt) {
        NovTranslate bean = new NovTranslate();
        bean.setCom("google");
        String result = "";
        try {
            result = translate.translate(txt, TranslateOption.sourceLanguage("ko"), TranslateOption.targetLanguage("en")).getTranslatedText(); 
            bean.setCde("ok");
        }catch (Exception e) {
            bean.setCde("err");
        }finally {
            bean.setMsg(result);
        }
        return Mono.just(bean);
    }

    public Mono<NovTranslate> eng2Kor(String txt) {
        NovTranslate bean = new NovTranslate();
        bean.setCom("google");
        String result = "";
        try {
            result = translate.translate(txt, TranslateOption.sourceLanguage("en"), TranslateOption.targetLanguage("ko")).getTranslatedText(); 
            bean.setCde("ok");
        }catch (Exception e) {
            bean.setCde("err");
        }finally {
            bean.setMsg(result);
        }
        return Mono.just(bean);
    }

}