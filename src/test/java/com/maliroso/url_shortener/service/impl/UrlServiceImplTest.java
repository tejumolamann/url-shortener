package com.maliroso.url_shortener.service.impl;

import com.maliroso.url_shortener.domain.dto.ShortCodeMetadata;
import com.maliroso.url_shortener.domain.dto.ShortCodeResponse;
import com.maliroso.url_shortener.domain.entity.ShortUrl;
import com.maliroso.url_shortener.domain.mapper.ShortCodeMapper;
import com.maliroso.url_shortener.exception.UrlExpiredException;
import com.maliroso.url_shortener.exception.UrlNotFoundException;
import com.maliroso.url_shortener.metrics.ShortenerMetrics;
import com.maliroso.url_shortener.repository.ShortUrlRepository;
import com.maliroso.url_shortener.service.CodeGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UrlServiceImplTest {

    @Mock
    private CodeGenerator codeGenerator;

    @Mock
    private ShortUrlRepository repository;

    @Mock
    private ShortCodeMapper mapper;

    @Mock
    private ShortenerMetrics metrics;

    @InjectMocks
    private UrlServiceImpl urlService;

    private final String testCode = "abc12345";
    private final String testLongUrl = "https://example.com/very/long/url";

    @Test
    void createShortUrl_Success() throws UnknownHostException {
        // Given
        when(codeGenerator.generateCode(testLongUrl)).thenReturn(testCode);
        String expectedHostname = InetAddress.getLocalHost().getHostName();

        // When
        ShortCodeResponse response = urlService.createShortUrl(testLongUrl);

        // Then
        assertNotNull(response);
        assertEquals(testCode, response.code());
        assertTrue(response.shortUrl().contains(expectedHostname));
        assertTrue(response.shortUrl().endsWith(testCode));

        verify(repository).save(any(ShortUrl.class));
        verify(metrics).incrementCreate();
    }

    @Test
    void createShortUrl_UnknownHost_ReturnsLocalhost() throws UnknownHostException {
        // Given
        try (MockedStatic<InetAddress> mockedInetAddress = mockStatic(InetAddress.class)) {
            when(codeGenerator.generateCode(testLongUrl)).thenReturn(testCode);
            mockedInetAddress.when(InetAddress::getLocalHost).thenThrow(new UnknownHostException());

            // When
            ShortCodeResponse response = urlService.createShortUrl(testLongUrl);

            // Then
            assertNotNull(response);
            assertEquals("localhost/r/" + testCode, response.shortUrl());
            verify(metrics).incrementCreate();
        }
    }

    @Test
    void resolveCode_Success() {
        // Given
        ShortUrl shortUrl = new ShortUrl();
        shortUrl.setCode(testCode);
        shortUrl.setLongUrl(testLongUrl);
        shortUrl.setHitCount(5L);
        shortUrl.setExpiresAt(Instant.now().plus(1, ChronoUnit.DAYS));

        when(repository.findByCode(testCode)).thenReturn(Optional.of(shortUrl));

        // When
        Optional<String> result = urlService.resolveCode(testCode);

        // Then
        assertTrue(result.isPresent());
        assertEquals(testLongUrl, result.get());
        assertEquals(6L, shortUrl.getHitCount());
        
        verify(repository).save(shortUrl);
        verify(metrics).incrementRedirect();
    }

    @Test
    void resolveCode_NotFound_ThrowsException() {
        // Given
        when(repository.findByCode(testCode)).thenReturn(Optional.empty());

        // When & Then
        UrlNotFoundException exception = assertThrows(UrlNotFoundException.class, () -> {
            urlService.resolveCode(testCode);
        });

        assertEquals("code not found:" + testCode, exception.getMessage());
        verify(repository, never()).save(any());
        verify(metrics, never()).incrementRedirect();
    }

    @Test
    void resolveCode_Expired_ThrowsException() {
        // Given
        ShortUrl shortUrl = new ShortUrl();
        shortUrl.setCode(testCode);
        shortUrl.setExpiresAt(Instant.now().minus(1, ChronoUnit.DAYS));

        when(repository.findByCode(testCode)).thenReturn(Optional.of(shortUrl));

        // When & Then
        UrlExpiredException exception = assertThrows(UrlExpiredException.class, () -> {
            urlService.resolveCode(testCode);
        });

        assertEquals("code expired:" + testCode, exception.getMessage());
        verify(repository, never()).save(any());
        verify(metrics, never()).incrementRedirect();
    }

    @Test
    void fetchCodeMetadata_Success() {
        // Given
        ShortUrl shortUrl = new ShortUrl();
        shortUrl.setCode(testCode);
        ShortCodeMetadata metadata = new ShortCodeMetadata(testCode, testLongUrl, 0, 0, 0, false);
        
        when(repository.findByCode(testCode)).thenReturn(Optional.of(shortUrl));
        when(mapper.toShortCodeMetadata(shortUrl)).thenReturn(metadata);

        // When
        ShortCodeMetadata result = urlService.fetchCodeMetadata(testCode);

        // Then
        assertNotNull(result);
        assertEquals(metadata, result);
        verify(repository).findByCode(testCode);
        verify(mapper).toShortCodeMetadata(shortUrl);
    }

    @Test
    void fetchCodeMetadata_NotFound_ThrowsException() {
        // Given
        when(repository.findByCode(testCode)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(UrlNotFoundException.class, () -> {
            urlService.fetchCodeMetadata(testCode);
        });
    }
}
