package com.maliroso.url_shortener.service.impl;

import com.maliroso.url_shortener.service.CodeGenerator;
import org.springframework.stereotype.Component;

/**
 * This class is responsible for generating short codes using the Base62 algorithm.
 */
@Component
public class Base62CodeGeneratorImpl implements CodeGenerator {

    @Override
    public String generateCode(String longUrl) {
        return null;
    }
}
