package com.maliroso.url_shortener.controller;

import com.maliroso.url_shortener.service.UrlService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ResolveController.class)
class ResolveControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UrlService urlService;

    @Test
    void resolveCode_WithExistingCode_ShouldRedirect() throws Exception {
        String code = "abcdef";
        String longUrl = "https://www.example.com";

        when(urlService.resolveCode(code)).thenReturn(Optional.of(longUrl));

        mockMvc.perform(get("/r/{code}", code))
                .andExpect(status().isFound())
                .andExpect(header().string(HttpHeaders.LOCATION, longUrl))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, "text/html; charset=UTF-8"))
                .andExpect(header().string(HttpHeaders.CONTENT_LENGTH, "0"));
    }

    @Test
    void resolveCode_WithNonExistentCode_ShouldReturnNotFound() throws Exception {
        String code = "nonext";

        when(urlService.resolveCode(code)).thenReturn(Optional.empty());

        mockMvc.perform(get("/r/{code}", code))
                .andExpect(status().isNotFound());
    }

    @Test
    void resolveCode_WithInvalidCodeSize_ShouldReturnBadRequest() throws Exception {
        String invalidCode = "abc"; // Too short, min 6

        mockMvc.perform(get("/r/{code}", invalidCode))
                .andExpect(status().isBadRequest());
    }
}
