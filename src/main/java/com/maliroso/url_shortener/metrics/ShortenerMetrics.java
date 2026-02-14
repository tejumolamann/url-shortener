package com.maliroso.url_shortener.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

/**
 * This class is responsible for collecting metrics related to the application's business logic.
 */
@Component
public class ShortenerMetrics {

    /**
     * Counter for tracking the successful redirects or short code resolutions.
     */
    private final Counter redirectCounter;

    /**
     * Counter for tracking the number of shortened URLs created.
     */
    private final Counter createCounter;

    /**
     * Counter for tracking the total number of collisions that occur
     * when generating short codes for URLs. A collision happens when
     * a newly generated short code matches an existing one, indicating
     * a hash conflict or duplication.
     */
    private final Counter collisionCounter;

    public ShortenerMetrics(MeterRegistry meterRegistry) {
        this.redirectCounter = Counter.builder("shortener_redirect_total")
                .description("Total number of URL redirects")
                .register(meterRegistry);

        this.createCounter = Counter.builder("shortener_create_total")
                .description("Total number of shortened URLs created")
                .register(meterRegistry);

        this.collisionCounter = Counter.builder("shortener_code_collision_total")
                .description("Total number of collisions that occurred when generating short codes")
                .register(meterRegistry);
    }

    /**
     * Increments the counter that tracks the total number of successful URL redirects
     * or short code resolutions. This method is typically called whenever a redirect
     * occurs in the application.
     */
    public void incrementRedirect() {
        redirectCounter.increment();
    }

    /**
     * Increments the counter that tracks the total number of shortened URLs created.
     * This method should be called whenever a new short URL is successfully generated
     * by the application. It updates the relevant metric for monitoring purposes.
     */
    public void incrementCreate() {
        createCounter.increment();
    }

    /**
     * Increments the counter that tracks the total number of collisions
     * occurring during the generation of short codes for URLs.
     * A collision happens when a newly generated short code matches
     * an existing one, indicating a hash conflict or duplication.
     * This method should be invoked whenever such an event is detected
     * in the application to update the corresponding metric.
     */
    public void incrementCollision() {
        collisionCounter.increment();
    }

}

