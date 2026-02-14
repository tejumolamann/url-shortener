package com.maliroso.url_shortener.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.apache.commons.validator.routines.UrlValidator;

/**
 * Represents a request to create a short URL from a given long URL.
 *
 * The {@code CreateUrlRequest} record encapsulates a single field, {@code longUrl},
 * which represents the URL to be shortened. The input URL is validated during
 * initialization to ensure it is a properly formatted and valid URL.
 *
 * Validation rules:
 * - The {@code longUrl} field must not be blank or empty.
 * - The {@code longUrl} must comply with URL format standards. URLs with fragments
 *   or unsupported structures are considered invalid.
 *
 * Upon validation failure, an {@code IllegalArgumentException} is thrown.
 *
 * Thread Safety:
 * - Instances of this record are immutable and thus thread-safe.
 *
 * Use this record as part of the URL shortening flow where client applications
 * supply a URL to be shortened.
 */
public record LongUrlRequest(
        @NotBlank
        @NotEmpty
        String longUrl
) {
        public LongUrlRequest {
                // Check the string is a valid URL
                UrlValidator urlValidator = new UrlValidator(UrlValidator.ALLOW_2_SLASHES + UrlValidator.NO_FRAGMENTS);

                if (!urlValidator.isValid(longUrl)) {
                        throw new IllegalArgumentException("Invalid URL");
                }
        }
}
