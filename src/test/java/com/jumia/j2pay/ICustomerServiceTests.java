package com.jumia.j2pay;

import com.jumia.j2pay.dto.response.UniversalResponse;
import com.jumia.j2pay.dto.util.FilterRequest;
import com.jumia.j2pay.model.Customer;
import com.jumia.j2pay.services.interfaces.ICustomerService;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import reactor.test.StepVerifier;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * @author Alex Maina
 * @created 22/01/2022
 */
@SpringBootTest
public class ICustomerServiceTests {
    @Autowired
    private ICustomerService customerService;
    private final Pageable pageable= PageRequest.of(1,15);

    @Test
    public void listCustomersTest() {
        Publisher<UniversalResponse> monoResponse = customerService.listCustomers(pageable);
        StepVerifier
                .create(monoResponse)
                .consumeNextWith(res -> {
                    assertThat(res.getStatus()).isEqualTo("success");
                    assertThat(res.getResponse()).isInstanceOf(List.class);
                    System.out.println(res.getResponse());
                    assertThat(((List<Customer>) res.getResponse()).size()).isGreaterThan(0);
                })
                .verifyComplete();
    }

    @Test
    public void filterByCountryCodeTest(){
        FilterRequest filterRequest= FilterRequest.builder().code("237").build();
        Publisher<UniversalResponse> monoResponse= customerService.filterPhoneNumbers(filterRequest);

        StepVerifier
                .create(monoResponse)
                .consumeNextWith(res->{
                    assertThat(res.getStatus()).isEqualTo("success");
                    assertThat(res.getResponse()).isInstanceOf(List.class);
                    assertThat(((List<Customer>) res.getResponse()).size()).isLessThan(11);
                })
                .verifyComplete();
    }

    @Test
    public void filterByPhoneNumberTest(){
        FilterRequest filterRequest= FilterRequest.builder().number("(258) 823747618").build();

        Publisher<UniversalResponse> monoResponse= customerService.filterPhoneNumbers(filterRequest);

        StepVerifier
                .create(monoResponse)
                .consumeNextWith(res-> {
                    assertThat(res.getStatus()).isEqualTo("success");
                    assertThat(res.getResponse()).isInstanceOf(List.class);
                    assertThat((List<Customer>) res.getResponse()).size().isEqualTo(1);
                })
                .verifyComplete();
    }

    @Test
    public void filterByValidPhoneTest(){
        FilterRequest filterRequest= FilterRequest.builder().state("valid").build();
        Publisher<UniversalResponse> monoResponse= customerService.filterPhoneNumbers(filterRequest);

        StepVerifier.create(monoResponse)
                .consumeNextWith(res-> {
                    assertThat(res.getStatus()).isEqualTo("success");
                    assertThat(res.getResponse()).isInstanceOf(List.class);
                    assertThat(res.getResponse()).isNotNull();
                })
                .verifyComplete();
    }

    @Test
    public void filterByCountryName(){
        FilterRequest filterRequest= FilterRequest.builder().country("Ethiopia").build();
        Publisher<UniversalResponse> monoResponse= customerService.filterPhoneNumbers(filterRequest);

        StepVerifier.create(monoResponse)
                .consumeNextWith(res-> {
                    assertThat(res.getStatus()).isEqualTo("success");
                    assertThat(res.getResponse()).isInstanceOf(List.class);
                    assertThat((List<Customer>) res.getResponse()).size().isGreaterThan(0);
                })
                .verifyComplete();
    }
}
