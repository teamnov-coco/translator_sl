package com.teamnov.translator.controller.translator.naver;

import com.teamnov.translator.dto.NovTranslate;
import com.teamnov.translator.service.translator.naver.NaverService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/naver")
public class NaverTranslatorController {
    
    @Autowired
    private NaverService naverService;
    
    /**
     * 한국어 -> 영어
     */
    @GetMapping(path = "/ko2en", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<NovTranslate> ko2En(@RequestParam("txt") String txt) {
        return naverService.ko2En(txt);
    }

    /**
     * 한국어 -> 중국어
     */ 
    @GetMapping(path = "/ko2cn", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<NovTranslate> ko2Cn(@RequestParam("txt") String txt) {
        return naverService.ko2Cn(txt);
    }

    /**
     * 한국어 -> 일본어
     */ 
    @GetMapping(path = "/ko2ja", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<NovTranslate> ko2Ja(@RequestParam("txt") String txt) {
        return naverService.ko2Ja(txt);
    }

    /**
     * 영어 -> 한국어
     */ 
    @GetMapping(path = "/en2ko", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<NovTranslate> en2Ko(@RequestParam("txt") String txt) {
        return naverService.en2Ko(txt);
    }

    /**
     * 영어 -> 중국어
     */ 
    @GetMapping(path = "/en2cn", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<NovTranslate> en2Cn(@RequestParam("txt") String txt) {
        return naverService.en2Cn(txt);
    }

    /**
     * 영어 -> 일본어
     */ 
    @GetMapping(path = "/en2ja", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<NovTranslate> en2Ja(@RequestParam("txt") String txt) {
        return naverService.en2Ja(txt);
    }

    /**
     * 중국어 -> 한국어
     */ 
    @GetMapping(path = "/cn2ko", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<NovTranslate> cn2Ko(@RequestParam("txt") String txt) {
        return naverService.cn2Ko(txt);
    }

    /**
     * 중국어 -> 영어
     */ 
    @GetMapping(path = "/cn2en", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<NovTranslate> cn2En(@RequestParam("txt") String txt) {
        return naverService.cn2En(txt);
    }

    /**
     * 중국어 -> 일본어
     */ 
    @GetMapping(path = "/cn2ja", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<NovTranslate> cn2Ja(@RequestParam("txt") String txt) {
        return naverService.cn2Ja(txt);
    }

    /**
     * 일본어 -> 한국어
     */ 
    @GetMapping(path = "/ja2ko", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<NovTranslate> ja2Ko(@RequestParam("txt") String txt) {
        return naverService.ja2Ko(txt);
    }

    
    /**
     * 일본어 -> 중국어
     */ 
    @GetMapping(path = "/ja2cn", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<NovTranslate> ja2Cn(@RequestParam("txt") String txt) {
        return naverService.ja2Cn(txt);
    }

    /**
     * 일본어 -> 영어
     */ 
    @GetMapping(path = "/ja2En", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<NovTranslate> ja2En(@RequestParam("txt") String txt) {
        return naverService.ja2En(txt);
    }

}