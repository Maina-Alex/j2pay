package com.jumia.j2pay.resource;

import com.jumia.j2pay.dto.response.UniversalResponse;
import com.jumia.j2pay.dto.util.FilterRequest;
import com.jumia.j2pay.services.interfaces.ICountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * @author Alex Maina
 * @created 17/01/2022
 */
@Controller
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class CustomerResource {
    private final ICountryService countryService;

    @GetMapping("/countries")
    public Mono<ServerResponse> listCountries() {
        return Mono.fromCallable(countryService::listCountries)
                .flatMap(res -> ServerResponse.ok().body(res, UniversalResponse.class));

    }


    @GetMapping("/phone")
    public Mono<ServerResponse> getPhoneNumbers(@RequestParam int size, @RequestParam int page) {
        return Mono.fromCallable(countryService::listPhoneNumbers)
                .flatMap(res -> ServerResponse.ok().body(res, UniversalResponse.class));
    }

    @GetMapping("/phone/filters")
    public Mono<ServerResponse> filterPhoneManager(@RequestParam Optional<String> country,
                                                   @RequestParam Optional<String> state,
                                                   @RequestParam Optional<String> code,
                                                   @RequestParam Optional<String> number,
                                                   @RequestParam(defaultValue = "0") int page ,
                                                   @RequestParam (defaultValue = "10")int size
                                                   ) {
        var filterRequest = FilterRequest.builder()
                .code(code.orElse(null))
                .state(state.orElse(null))
                .number(number.orElse(null))
                .country(country.orElse(null))
                .size(size)
                .page(page)
                .build();
        return Mono.fromCallable(() -> countryService.filterPhoneNumbers(filterRequest))
                .flatMap(res -> ServerResponse.ok().body(res, UniversalResponse.class));
    }

}
