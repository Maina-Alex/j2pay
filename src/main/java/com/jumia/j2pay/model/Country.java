package com.jumia.j2pay.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author Alex Maina
 * @created 18/01/2022
 */
@Getter
public enum Country {
    CAMEROON("Cameroon","237","\\(237\\)\\ ?[2368]\\d{7,8}$"),
    ETHIOPIA("Ethiopia","251","\\(251\\)\\ ?[1-59]\\d{8}$"),
    MOROCCO("Morocco","212","\\(212\\)\\ ?[5-9]\\d{8}$"),
    MOZAMBIQUE("Mozambique","258","\\(258\\)\\ ?[28]\\d{7,8}$"),
    UGANDA("Uganda","256","\\(256\\)\\ ?\\d{9}$");
   @Getter
    private final String name;
    private final String code;
    private final String regex;

    Country(String name,String code,String regex){
        this.name= name;
        this.code=code;
        this.regex=regex;
    }

}
