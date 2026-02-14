package com.maliroso.url_shortener.service.impl;

import com.maliroso.url_shortener.domain.dto.ShortCodeResponse;
import com.maliroso.url_shortener.service.CodeGenerator;
import com.maliroso.url_shortener.service.UrlService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

@AllArgsConstructor
@Component
public class UrlServiceImpl implements UrlService {

    private final CodeGenerator codeGenerator;

    @Override
    public ShortCodeResponse createShortUrl(String longUrl) {
        String code = codeGenerator.generateCode(longUrl);
        
        // Get server's hostname
        String hostname;
        try {
            hostname = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            hostname = "localhost";
        }

        return new ShortCodeResponse(code, hostname + "/r/" + code);
    }

    @Override
    public void resolveCode(String code) {
        // to be implemented
    }

    @Override
    public void fetchCodeMetadata(String code) {
        // to be implemented
    }
}
