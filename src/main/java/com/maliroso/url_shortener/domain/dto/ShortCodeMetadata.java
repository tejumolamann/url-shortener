package com.maliroso.url_shortener.domain.dto;

/**
 * Represents the metadata associated with a short code in a URL shortening service.
 * <p>
 * The {@code ShortCodeMetadata} record encapsulates key properties related to a short code,
 * offering insights into its behavior and lifecycle. It is primarily used for fetching
 * details about a specific short code within the system.
 * <p>
 * Fields:
 * - {@code code}: The unique identifier representing the short code associated with a long URL.
 * - {@code longUrl}: The original, full-length URL that corresponds to the short code.
 * - {@code clicks}: The number of times the short code has been accessed or resolved.
 * - {@code createdAt}: The creation timestamp of the short code, represented in epoch milliseconds.
 * - {@code expiresAt}: The expiration timestamp of the short code, represented in epoch milliseconds.
 * - {@code isExpired}: A boolean field indicating whether the short code has expired.
 * <p>
 * Typical Use Cases:
 * - Providing metadata for administrative or analytics purposes.
 * - Tracking the usage or status of a specific short code.
 * - Validating the lifecycle of a short code based on its expiration status.
 * <p>
 * Thread Safety:
 * - Instances of this record are immutable and therefore thread-safe.
 * <p>
 * Implementation Notes:
 * - The {@code createdAt} and {@code expiresAt} fields store timestamps in epoch milliseconds
 *   format for interoperability with various time-based operations.
 * - The {@code isExpired} field is typically derived from the logical rules,
 *   such as comparing the {@code expiresAt} field with the current time.
 */
public record ShortCodeMetadata(String code, String longUrl, long clicks, long createdAt, long expiresAt, boolean isExpired) {
}
