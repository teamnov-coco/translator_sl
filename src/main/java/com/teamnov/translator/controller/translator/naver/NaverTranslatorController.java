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


}