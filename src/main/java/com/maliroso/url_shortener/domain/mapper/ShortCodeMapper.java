package com.maliroso.url_shortener.domain.mapper;

import com.maliroso.url_shortener.domain.dto.ShortCodeMetadata;
import com.maliroso.url_shortener.domain.entity.ShortUrl;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper interface for converting instances of the {@link ShortUrl} entity into
 * {@link ShortCodeMetadata} DTOs and performing related utility operations.
 * <p>
 * This mapper is implemented using MapStruct and automatically generates the
 * implementation at runtime. It is configured with the Spring component model
 * to enable seamless dependency injection.
 * <p>
 * Key Responsibilities:
 * - Maps the fields from a ShortUrl entity to a ShortCodeMetadata record.
 * - Provides utility methods for date-time conversion within mapping operations.
 * <p>
 * Mapping Details:
 * - The `hitCount` field in ShortUrl is mapped to the `clicks` field in ShortCodeMetadata.
 * - A derived field `isExpired` in ShortCodeMetadata is calculated using the `isExpired`
 *   method of ShortUrl.
 * - The `createdAt` and `expiresAt` timestamps in ShortCodeMetadata are derived
 *   from ShortUrl's corresponding fields and converted to epoch milliseconds
 *   using the `convertInstantToMilli` method.
 */
@Mapper(componentModel = "spring")
public interface ShortCodeMapper {

    @Mapping(target = "clicks", source = "hitCount")
    @Mapping(target = "isExpired", expression = "java(shortUrl.isExpired())")
    @Mapping(target = "createdAt", source = "shortUrl",qualifiedByName = "convertInstantToMilli")
    @Mapping(target = "expiresAt", source = "shortUrl",qualifiedByName = "convertInstantToMilli")
    ShortCodeMetadata toShortCodeMetadata(ShortUrl shortUrl);

    @Named( "convertInstantToMilli")
    default long convertInstantToMilli(ShortUrl shortUrl) {
        return shortUrl.getCreatedAt().toEpochMilli();
    }
}
