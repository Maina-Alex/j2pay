package com.jumia.j2pay.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author Alex Maina
 * @created 17/01/2022
 */
@Entity
@Getter
@Setter
public class Customer {
    @Id
    private long id;
    private String name;
    private String phone;
}
