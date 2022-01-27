package com.jumia.j2pay.resource;

import com.jumia.j2pay.dto.response.UniversalResponse;
import com.jumia.j2pay.dto.util.FilterRequest;
import com.jumia.j2pay.services.interfaces.ICustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * @author Alex Maina
 * @created 17/01/2022
 */
@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
@CrossOrigin
public class CustomerResource {
    private final ICustomerService countryService;

    @GetMapping("/customers")
    public Mono<ResponseEntity<UniversalResponse>> listCountries(@RequestParam(defaultValue = "0", name = "page") int page, @RequestParam(defaultValue = "10",name="size") int size) {
        Pageable pageable= PageRequest.of(page,size);
        return countryService.listCustomers(pageable)
                .map(res -> ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(res));
    }

    @GetMapping("/customer/filters")
    public Mono<ResponseEntity<UniversalResponse>> filterPhoneManager(
                                                   @RequestParam Optional<String> country,
                                                   @RequestParam Optional<String> state,
                                                   @RequestParam Optional<String> code,
                                                   @RequestParam Optional<String> number,
                                                   @RequestParam(defaultValue = "0") int page ,
                                                   @RequestParam (defaultValue = "40")int size
                                                   ) {
        var filterRequest = FilterRequest.builder()
                .code(code.orElse(null))
                .state(state.orElse(null))
                .number(number.orElse(null))
                .country(country.orElse(null))
                .size(size)
                .page(page)
                .build();
        return countryService.filterPhoneNumbers(filterRequest)
                .map(res -> ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(res));
    }


}
