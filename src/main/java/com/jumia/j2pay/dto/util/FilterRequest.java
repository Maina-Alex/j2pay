package com.jumia.j2pay.dto.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.lang.Nullable;

/**
 * @author Alex Maina
 * @created 19/01/2022
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FilterRequest {
    @Nullable
    private String state;
    @Nullable
    private String country;
    @Nullable
    private String code;
    @Nullable
    private String number;
    private int page;
    private int size;
}
