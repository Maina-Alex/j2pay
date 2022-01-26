package com.jumia.j2pay.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Alex Maina
 * @created 18/01/2022
 */
@Getter
@Setter
@Builder
public class CustomerDto {
    private long id;
    private String name;
    private String phone;
    private String valid;
    private String country;
}
