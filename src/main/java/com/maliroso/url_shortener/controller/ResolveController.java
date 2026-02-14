package com.maliroso.url_shortener.controller;

import com.maliroso.url_shortener.domain.dto.ShortCodeRequest;
import com.maliroso.url_shortener.service.UrlService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

/**
 * This class is the controller for resolving short URLs. A pre-existing short code will be resolved to its
 * corresponding long URL.
 */
@RestController
@RequestMapping("/r")
@Validated
@AllArgsConstructor
class ResolveController {

    private final UrlService urlService;

    /**
     * This method resolves the short URL code by sending a redirect response (HTTP Status 302) to the client.
     *
     * @param code - the short code, which is part of the short URL path
     * @return - a redirect response sent to the client containing the resolved long URL
     */
    @GetMapping("/{code}")
    ResponseEntity<Void> resolveCode(@PathVariable @Valid ShortCodeRequest code) {
        Optional<String> optionalLongUrl = urlService.resolveCode(code.shortCode());

        // Builds redirect 302 response or returns 404 not found
        return optionalLongUrl.<ResponseEntity<Void>>map(
                longUrl -> ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", longUrl)
                .header("Content-Type", "text/html; charset=UTF-8")
                .header("Content-Length", "0")
                .build()
        ).orElseGet(
                () -> ResponseEntity.notFound().build()
        );
    }

}
