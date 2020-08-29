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

    public Mono<NovTranslate> getTransltate(String txt, String src, String dst) {
        NovTranslate bean = new NovTranslate();
        bean.setCom("google");
        String result = "";
        try {
            result = translate.translate(txt, TranslateOption.sourceLanguage(src), TranslateOption.targetLanguage(dst)).getTranslatedText(); 
            bean.setCde("ok");
        }catch (Exception e) {
            bean.setCde("err");
        }finally {
            bean.setMsg(result);
        }
        return Mono.just(bean);
    }

    /**
     * 한국어 -> 영어
     */ 
    public Mono<NovTranslate> ko2En(String txt) {
        return getTransltate(txt, GoogleLang.KOR, GoogleLang.ENG);
    }

    /**
     * 한국어 -> 중국어
     */ 
    public Mono<NovTranslate> ko2Cn(String txt) {
        return getTransltate(txt, GoogleLang.KOR, GoogleLang.CHN);
    }

    /**
     * 한국어 -> 일본어
     */ 
    public Mono<NovTranslate> ko2Ja(String txt) {
        return getTransltate(txt, GoogleLang.KOR, GoogleLang.JPN);
    }

    /**
     * 영어 -> 한국어
     */ 
    public Mono<NovTranslate> en2Ko(String txt) {
        return getTransltate(txt, GoogleLang.ENG, GoogleLang.KOR);
    }

    /**
     * 영어 -> 중국어
     */ 
    public Mono<NovTranslate> en2Cn(String txt) {
        return getTransltate(txt, GoogleLang.ENG, GoogleLang.CHN);
    }

    /**
     * 영어 -> 일본어
     */ 
    public Mono<NovTranslate> en2Ja(String txt) {
        return getTransltate(txt, GoogleLang.ENG, GoogleLang.JPN);
    }

    /**
     * 중국어 -> 한국어
     */ 
    public Mono<NovTranslate> cn2Ko(String txt) {
        return getTransltate(txt, GoogleLang.CHN, GoogleLang.KOR);
    }

    /**
     * 중국어 -> 영어
     */ 
    public Mono<NovTranslate> cn2En(String txt) {
        return getTransltate(txt, GoogleLang.CHN, GoogleLang.ENG);
    }

    /**
     * 중국어 -> 일본어
     */ 
    public Mono<NovTranslate> cn2Ja(String txt) {
        return getTransltate(txt, GoogleLang.CHN, GoogleLang.JPN);
    }

    /**
     * 일본어 -> 한국어
     */ 
    public Mono<NovTranslate> ja2Ko(String txt) {
        return getTransltate(txt, GoogleLang.JPN, GoogleLang.KOR);
    }

    /**
     * 일본어 -> 영어
     */ 
    public Mono<NovTranslate> ja2En(String txt) {
        return getTransltate(txt, GoogleLang.JPN, GoogleLang.ENG);
    }

    /**
     * 일본어 -> 중국어
     */ 
    public Mono<NovTranslate> ja2Cn(String txt) {
        return getTransltate(txt, GoogleLang.JPN, GoogleLang.CHN);
    }

}