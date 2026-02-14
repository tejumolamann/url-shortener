package com.maliroso.url_shortener.repository;

import com.maliroso.url_shortener.domain.entity.ShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing {@link ShortUrl} entities in the database.
 * <p>
 * This interface enables CRUD operations, query execution, and other database
 * interactions for the {@link ShortUrl} entity. It extends the {@link JpaRepository}
 * interface, leveraging Spring Data JPA to provide functionality such as:
 * - Saving and updating entities
 * - Finding entities by their primary key or other fields
 * - Deleting entities
 * - Custom query creation
 * <p>
 * Features:
 * - Automatically implemented at runtime by Spring Data JPA.
 * - Reduces the need for boilerplate code for database operations.
 * - Supports integration with the {@link ShortUrl} entity, which represents
 *   a shortened URL and its associated metadata within a URL shortening service.
 */
@Repository
public interface ShortUrlRepository extends JpaRepository<ShortUrl, Long> {
    Optional<ShortUrl> findByCode(String code);
}