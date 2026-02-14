package com.maliroso.url_shortener.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

/**
 * Represents a request containing a short code used to retrieve metadata or validate the code.
 * <p>
 * The {@code ShortCodeRequest} record encapsulates a single field, {@code shortCode},
 * which is a unique identifier for the corresponding long URL or data registered in the system.
 * <p>
 * Validation rules:
 * - The {@code shortCode} field must not be blank or empty.
 * - The {@code shortCode} field must be exactly 6 characters long.
 * <p>
 * This record is typically used in endpoints where short codes are passed
 * as input, such as querying or validating short URLs.
 * <p>
 * Thread Safety:
 * - Instances of this record are immutable and therefore thread-safe.
 */
public record ShortCodeRequest(
        @NotBlank
        @NotEmpty
        @Size(min = 6, max = 6)
        String shortCode
) {
}
