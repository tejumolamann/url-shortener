package com.maliroso.url_shortener.service.impl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Base62CodeGeneratorImplTest {

    @Test
    void givenValidUrl_whenGenerateCode_thenValidShortCodeIsReturned() {
        Base62CodeGeneratorImpl codeGenerator = new Base62CodeGeneratorImpl();

        // URL to this application's GitHub repo
        String code = codeGenerator.generateCode("https://github.com/tejumolamann/url-shortener");

        assertNotNull(code);
        assertEquals(6, code.length());
    }

}