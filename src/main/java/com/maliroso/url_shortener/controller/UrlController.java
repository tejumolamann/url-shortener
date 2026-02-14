package com.maliroso.url_shortener.controller;

import com.maliroso.url_shortener.domain.dto.LongUrlRequest;
import com.maliroso.url_shortener.domain.dto.ShortCodeMetadata;
import com.maliroso.url_shortener.domain.dto.ShortCodeRequest;
import com.maliroso.url_shortener.domain.dto.ShortCodeResponse;
import com.maliroso.url_shortener.service.UrlService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * The UrlController class provides RESTful endpoints for URL shortening operations.
 * It acts as the entry point for handling HTTP requests related to creating short URLs
 * and retrieving metadata for existing short URL codes.
 * <p>
 * Endpoints:
 * - POST /api/urls: Creates a new short URL for a given long URL.
 * - GET /api/urls/{code}: Fetches metadata for a short URL code.
 * <p>
 * The class is annotated with:
 * - {@code @RestController} to designate it as a RESTful controller.
 * - {@code @RequestMapping("/api/urls")} to define the base URL path for all endpoints.
 * - {@code @Validated} to enable validation of input request data.
 * - {@code @AllArgsConstructor} for dependency injection of {@code UrlService}.
 * <p>
 * Dependencies:
 * - {@code UrlService}: Provides core logic for URL shortening and metadata retrieval.
 * <p>
 * Thread Safety:
 * - This class is thread-safe as it is stateless and depends on properly configured thread-safe services.
 * <p>
 * Validation:
 * - Both the {@code @RequestBody} and {@code @PathVariable} input parameters are validated using
 *   annotations such as {@code @Valid}, {@code @NotBlank}, and {@code @Size}.
 */
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

    @GetMapping("/{code}")
    public ResponseEntity<ShortCodeMetadata> getCodeData(@PathVariable @Valid ShortCodeRequest code) {
        ShortCodeMetadata shortCodeMetadata = urlService.fetchCodeMetadata(code.shortCode());

        return ResponseEntity.ok().body(shortCodeMetadata);
    }

}
