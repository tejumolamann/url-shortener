package com.maliroso.url_shortener.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maliroso.url_shortener.domain.dto.LongUrlRequest;
import com.maliroso.url_shortener.domain.dto.ShortCodeMetadata;
import com.maliroso.url_shortener.domain.dto.ShortCodeResponse;
import com.maliroso.url_shortener.exception.UrlNotFoundException;
import com.maliroso.url_shortener.service.UrlService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UrlController.class)
class UrlControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UrlService urlService;

    @Test
    void createShortUrl_WithValidUrl_ShouldReturnShortCodeResponse() throws Exception {
        String longUrl = "https://www.example.com";
        ShortCodeResponse response = new ShortCodeResponse("abcdef", "http://localhost/r/abcdef");
        
        when(urlService.createShortUrl(longUrl)).thenReturn(response);

        mockMvc.perform(post("/api/urls")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new LongUrlRequest(longUrl))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("abcdef"))
                .andExpect(jsonPath("$.shortUrl").value("http://localhost/r/abcdef"));
    }

    @Test
    void createShortUrl_WithInvalidUrl_ShouldReturnBadRequest() throws Exception {
        // Since validation happens in the constructor of LongUrlRequest, 
        // passing an invalid URL to the constructor will throw an IllegalArgumentException.
        // However, in a WebMvcTest, if the request body cannot be deserialized, it returns 400.
        
        String invalidUrl = "not-a-url";
        
        mockMvc.perform(post("/api/urls")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"longUrl\": \"" + invalidUrl + "\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getCodeData_WithExistingCode_ShouldReturnMetadata() throws Exception {
        String code = "abcdef";
        ShortCodeMetadata metadata = new ShortCodeMetadata(
                code, 
                "https://www.example.com", 
                10, 
                System.currentTimeMillis(), 
                System.currentTimeMillis() + 100000, 
                false
        );

        when(urlService.fetchCodeMetadata(code)).thenReturn(metadata);

        mockMvc.perform(get("/api/urls/{code}", code))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(code))
                .andExpect(jsonPath("$.longUrl").value("https://www.example.com"))
                .andExpect(jsonPath("$.clicks").value(10));
    }

    @Test
    void getCodeData_WithNonExistentCode_ShouldReturnNotFound() throws Exception {
        String code = "nonext";
        when(urlService.fetchCodeMetadata(code)).thenThrow(new UrlNotFoundException("URL not found"));

        mockMvc.perform(get("/api/urls/{code}", code))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("URL Not Found"))
                .andExpect(jsonPath("$.detail").value("URL not found"));
    }

    @Test
    void getCodeData_WithInvalidCodeSize_ShouldReturnBadRequest() throws Exception {
        String invalidCode = "abc"; // Too short, min 6

        mockMvc.perform(get("/api/urls/{code}", invalidCode))
                .andExpect(status().isBadRequest());
    }
}
