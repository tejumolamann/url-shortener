package com.maliroso.url_shortener.domain.entity;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

/**
 * Represents a shortened URL entity for use in a URL shortening service.
 * <p>
 * This class maps to the "short_urls" table in the database and contains
 * relevant information about the shortened URL, including the original long
 * URL, the shortened code, and metadata such as creation/expiration timestamps
 * and hit count.
 * <p>
 * Features:
 * - Stores the original long URL and its corresponding shortened code.
 * - Tracks creation and optional expiration timestamps.
 * - Maintains a hit count to monitor usage.
 * - Supports functionality to determine whether the URL has expired.
 * <p>
 * Equality and Hashing:
 * - Equality and hashcode are based on all fields.
 * <p>
 * String Representation:
 * - A string representation of the entity is provided using Google Guava's
 *   MoreObjects.ToStringHelper.
 */
@Entity
@Table(name = "short_urls")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ShortUrl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The shortened URL code.
     */
    @Column(unique = true, nullable = false)
    private String code;

    /**
     * The long URL.
     */
    @Column(nullable = false)
    private String longUrl;

    /**
     * Creation datetime.
     */
    @Column(nullable = false)
    private Instant createdAt;

    /**
     * The expiration datetime.
     */
    @Column
    private Instant expiresAt;

    @Column(nullable = false)
    private Long hitCount;

    public boolean isExpired() {
        return expiresAt != null && expiresAt.isBefore(Instant.now());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ShortUrl shortUrl = (ShortUrl) o;
        return Objects.equal(id, shortUrl.id)
                && Objects.equal(code, shortUrl.code)
                && Objects.equal(longUrl, shortUrl.longUrl)
                && Objects.equal(createdAt, shortUrl.createdAt)
                && Objects.equal(expiresAt, shortUrl.expiresAt)
                && Objects.equal(hitCount, shortUrl.hitCount);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, code, longUrl, createdAt, expiresAt, hitCount);
    }

    @Override
    public String toString() {
        MoreObjects.ToStringHelper stringHelper = MoreObjects.toStringHelper(this);

        stringHelper.add("code", this.code)
                .add("longUrl", this.longUrl)
                .add("createdAt", this.createdAt)
                .add("expiresAt", this.expiresAt)
                .add("hitCount", this.hitCount);

        return stringHelper.toString();
    }
}
