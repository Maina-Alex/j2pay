package com.jumia.j2pay.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Alex Maina
 * @created 18/01/2022
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UniversalResponse {
    private String status;
    private String message;
    private Object response;
    private Pagination pagination;
}
