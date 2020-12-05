package com.github.heyfixit.Configuration;

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.RetryRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class ResilienceConfig {
    @Bean
    public RetryRegistry getGlobalRetryConfig() {
        RetryRegistry registry = RetryRegistry.ofDefaults();
        registry.getEventPublisher().onEntryAdded(event -> {
            log.info("Entry added {}", event.getAddedEntry().getName());
            event.getAddedEntry().getEventPublisher().onRetry(event1 -> log.info("Retry on {}", event.getAddedEntry().getName()));
        });
        return registry;
    }

    @Bean
    public CircuitBreakerRegistry getGlobalCircuitBreakerRegistry() {
        return CircuitBreakerRegistry.ofDefaults();
    }
}
