package com.maliroso.url_shortener.service.impl;

import com.maliroso.url_shortener.service.CodeGenerator;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * This class is responsible for generating short codes using the Base62 algorithm.
 */
@Component
public class Base62CodeGeneratorImpl implements CodeGenerator {

    private static final char[] BASE62_CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private static final int CODE_LENGTH = 6;

    @Override
    public String generateCode(String longUrl) {
        try {
            // Step 1: Normalize input
            String normalizedUrl = longUrl.trim();

            // Step 2: Hash using SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(normalizedUrl.getBytes(StandardCharsets.UTF_8));

            // Step 3: Convert hash to positive BigInteger
            BigInteger number = new BigInteger(1, hash);

            // Step 4: Convert to Base62
            String base62 = toBase62(number);

            // Step 5: Return first 6 characters
            return base62.substring(0, CODE_LENGTH);

        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 algorithm not available", e);
        }
    }

    private static String toBase62(BigInteger number) {
        StringBuilder sb = new StringBuilder();

        while (number.compareTo(BigInteger.ZERO) > 0) {
            BigInteger[] divmod = number.divideAndRemainder(BigInteger.valueOf(62));
            sb.append(BASE62_CHARS[divmod[1].intValue()]);
            number = divmod[0];
        }

        return sb.reverse().toString();
    }
}
