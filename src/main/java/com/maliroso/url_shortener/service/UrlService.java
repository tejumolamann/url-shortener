package com.maliroso.url_shortener.service;

import com.maliroso.url_shortener.domain.dto.ShortCodeResponse;
import org.springframework.stereotype.Service;

@Service
public interface UrlService {
    /**
     * This creates a short URL code for the given long URL in the argument.
     *
     * @param longUrl - the long URL to be shortened
     * @return
     */
    ShortCodeResponse createShortUrl(String longUrl);

    /**
     * This method resolves the short URL code to the original long URL.
     * @param code the short URL code
     */
    void resolveCode(String code);

    /**
     * This method fetches the metadata for short URL code.
     * @param code - the short URL code
     */
    void fetchCodeMetadata(String code);
}
