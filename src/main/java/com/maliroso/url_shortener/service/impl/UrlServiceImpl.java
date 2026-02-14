package com.maliroso.url_shortener.service.impl;

import com.maliroso.url_shortener.domain.dto.ShortCodeMetadata;
import com.maliroso.url_shortener.domain.dto.ShortCodeResponse;
import com.maliroso.url_shortener.domain.entity.ShortUrl;
import com.maliroso.url_shortener.domain.mapper.ShortCodeMapper;
import com.maliroso.url_shortener.repository.ShortUrlRepository;
import com.maliroso.url_shortener.service.CodeGenerator;
import com.maliroso.url_shortener.service.UrlService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Optional;

@AllArgsConstructor
@Component
public class UrlServiceImpl implements UrlService {

    private final CodeGenerator codeGenerator;

    private final ShortUrlRepository repository;

    private final ShortCodeMapper mapper;

    /**
     * Creates short URL; persists it; returns response
     */
    @Override
    public ShortCodeResponse createShortUrl(String longUrl) {
        String code = codeGenerator.generateCode(longUrl);

        // Persist the short URL
        ShortUrl newShortUrl = new ShortUrl();
        newShortUrl.setCode(code);
        newShortUrl.setLongUrl(longUrl);
        repository.save(newShortUrl);
        
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
    public Optional<String> resolveCode(String code) {
        Optional<ShortUrl> optionalShortUrl = repository.findByCode(code);

        return optionalShortUrl.map(ShortUrl::getLongUrl);
    }

    @Override
    public ShortCodeMetadata fetchCodeMetadata(String code) {
        Optional<ShortUrl> optionalShortUrl = repository.findByCode(code);

        ShortUrl shortUrl = optionalShortUrl.orElseThrow(() -> new IllegalArgumentException("Code not found: " + code));

        return mapper.toShortCodeMetadata(shortUrl);
    }
}
