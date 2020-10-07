package com.github.heyfixit.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.heyfixit.Service.ThingService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@AllArgsConstructor
public class ThingController {
    private final ThingService thingService;

    @GetMapping(value = "/")
    public Flux<String> getThings() throws JsonProcessingException {
        return thingService.getThings();
    }
}
