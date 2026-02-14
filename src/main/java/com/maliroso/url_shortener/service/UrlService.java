package com.maliroso.url_shortener.service;

import com.maliroso.url_shortener.domain.dto.ShortCodeMetadata;
import com.maliroso.url_shortener.domain.dto.ShortCodeResponse;
import org.springframework.stereotype.Service;

/**
 * The UrlService interface defines the core contract for URL shortening functionality.
 * Implementations of this interface are responsible for providing the logic to create
 * short URLs, resolve short URL codes to their original long URLs, and fetch metadata
 * related to a given short URL code.
 * <p>
 * Responsibilities:
 * - Shortening a given long URL to a short URL code.
 * - Resolving a short URL code back to the associated long URL.
 * - Retrieving metadata about a short URL code, such as creation date, expiration status,
 *   and the number of times the short URL has been accessed.
 * <p>
 * Implementing classes of this service are expected to handle persistence, code generation,
 * and other required operations internally. Typical use cases include enabling URL
 * redirection services, tracking URL usage, and managing URL lifecycles.
 */
@Service
public interface UrlService {
    /**
     * Creates a short URL from a given long URL.
     * This method generates a unique short code corresponding to the provided long URL
     * and returns a response containing the short code along with its associated short URL.
     *
     * @param longUrl the original long URL to be shortened
     * @return a {@code ShortCodeResponse} object containing the generated short URL and code
     */
    ShortCodeResponse createShortUrl(String longUrl);

    /**
     * This method resolves the short URL code to the original long URL.
     * @param code the short URL code
     */
    void resolveCode(String code);

    /**
     * Retrieves metadata associated with a given short URL code.
     * The metadata includes details such as the original long URL, the number of clicks,
     * creation and expiration timestamps, and the expiration status of the short URL.
     *
     * @param code the short URL code for which metadata is to be retrieved
     * @return a {@code ShortCodeMetadata} object containing metadata about the specified short URL code
     */
    ShortCodeMetadata fetchCodeMetadata(String code);
}
