package org.example.library.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * Кэш каталога библиотеки.
 *
 * Логика:
 *  - books:   список всех книг с авторами и жанрами (тяжёлый JOIN FETCH) — 10 мин TTL
 *  - authors: список всех авторов                                — 30 мин TTL
 *  - genres:  список жанров                                      — 1 час  TTL
 *
 * Инвалидация:
 *  - @CacheEvict на createBook/updateBook/deleteBook  → books
 *  - @CacheEvict на createAuthor/updateAuthor         → authors
 *  - @CacheEvict на issueBook/returnBook              → books (меняется available_copies)
 */
@Configuration
@EnableCaching
public class CacheConfig {

    public static final String BOOKS_CACHE   = "books";
    public static final String AUTHORS_CACHE = "authors";
    public static final String GENRES_CACHE  = "genres";

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager manager = new CaffeineCacheManager(
                BOOKS_CACHE, AUTHORS_CACHE, GENRES_CACHE);

        manager.setCaffeine(
                Caffeine.newBuilder()
                        .maximumSize(1000)                          // макс. записей в каждом кэше
                        .expireAfterWrite(Duration.ofMinutes(10))   // TTL
                        .recordStats()                              // статистика для метрик
        );

        // Не кэшировать null — иначе "не нашли" залипнет на TTL
        manager.setAllowNullValues(false);

        return manager;
    }
}