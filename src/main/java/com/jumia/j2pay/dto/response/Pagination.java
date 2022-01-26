package com.jumia.j2pay.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Alex Maina
 * @created 26/01/2022
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
public class Pagination {
    private int page;
    private int size;
    private long recordSize;
    private int pages;
}
