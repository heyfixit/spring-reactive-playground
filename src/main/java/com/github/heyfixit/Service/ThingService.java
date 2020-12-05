package com.github.heyfixit.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.heyfixit.Model.Thing;
import io.github.resilience4j.reactor.retry.RetryOperator;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Random;

@Service
@Slf4j
public class ThingService {
    ObjectMapper objectMapper;
    RetryRegistry globalRegistry;

    public ThingService(ObjectMapper objectMapper, RetryRegistry globalRegistry) {
        this.objectMapper = objectMapper;
        this.objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        this.globalRegistry = globalRegistry;
    }

    public Flux<String> getThings() throws JsonProcessingException {
        return Flux.just("Message One", "Message Two", 2, objectMapper.writeValueAsString(Thing.builder().field1("Field 1").field2("Field 2").build()))
                .map(msg -> msg + "<br/>")
                .delayElements(Duration.ofSeconds(1));
    }

    public Mono<String> retryTwice() {
        WebClient webClient = WebClient.builder().build();
        RetryConfig config = RetryConfig.custom()
                .maxAttempts(2)
                .waitDuration(Duration.ofMillis(10000))
                .build();

        return Mono.just("Something").flatMap(body -> (new Random().nextInt() % 2) == 0 ? Mono.error(new Exception("Broke!")) : Mono.just(body))
                .doOnError(throwable -> log.info("Error! {}", throwable.toString()))
                .transformDeferred(RetryOperator.of(globalRegistry.retry("Test Retry")));
    }
}
