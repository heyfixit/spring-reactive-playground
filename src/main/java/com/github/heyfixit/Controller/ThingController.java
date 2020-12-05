package com.github.heyfixit.Controller;

import com.github.heyfixit.Service.ThingService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@AllArgsConstructor
public class ThingController {
    private final ThingService thingService;

    @GetMapping(value = "/")
    public Mono<String> getThings() {
        return thingService.retryTwice();
    }
}
