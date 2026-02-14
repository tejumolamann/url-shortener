package com.maliroso.url_shortener.service;

import org.springframework.stereotype.Component;

@Component
public interface CodeGenerator {

    /**
     * This method generates a short code for the given long URL represented as a string.
     * @param longUrl - the long URL to be shortened
     * @return - the generated short code.
     */
    String generateCode(String longUrl);
}
