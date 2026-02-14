package com.maliroso.url_shortener.controller;

import com.maliroso.url_shortener.domain.dto.LongUrlRequest;
import com.maliroso.url_shortener.domain.dto.ShortCodeResponse;
import com.maliroso.url_shortener.service.UrlService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/urls")
@Validated
@AllArgsConstructor
class UrlController {

    private final UrlService urlService;

    @PostMapping
    public ResponseEntity<ShortCodeResponse> createShortUrl(@RequestBody @Valid LongUrlRequest longUrlRequest) {
        ShortCodeResponse shortCodeResponse = urlService.createShortUrl(longUrlRequest.longUrl());

        return ResponseEntity.ok().body(shortCodeResponse);
    }

}
