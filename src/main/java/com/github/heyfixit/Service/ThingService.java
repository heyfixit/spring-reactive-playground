package com.github.heyfixit.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.heyfixit.Model.Thing;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Service
public class ThingService {
    ObjectMapper objectMapper;

    public ThingService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
    }

    public Flux<String> getThings() throws JsonProcessingException {
        return Flux.just("Message One", "Message Two", 2, objectMapper.writeValueAsString(Thing.builder().field1("Field 1").field2("Field 2").build()))
                .map(msg -> msg + "<br/>")
                .delayElements(Duration.ofSeconds(1));
    }
}
