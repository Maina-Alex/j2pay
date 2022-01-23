package com.jumia.j2pay.services.interfaces;

import com.jumia.j2pay.dto.response.UniversalResponse;
import com.jumia.j2pay.dto.util.FilterRequest;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * @author Alex Maina
 * @created 17/01/2022
 */
public interface ICustomerService {
    Mono<UniversalResponse> listCustomers(Pageable pageable);
    Mono<UniversalResponse> filterPhoneNumbers(FilterRequest filterRequest);
}
