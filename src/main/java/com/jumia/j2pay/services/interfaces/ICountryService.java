package com.jumia.j2pay.services.interfaces;

import com.jumia.j2pay.dto.response.UniversalResponse;
import com.jumia.j2pay.dto.util.FilterRequest;

import java.util.Optional;

/**
 * @author Alex Maina
 * @created 17/01/2022
 */
public interface ICountryService {
    UniversalResponse listCountries();

    UniversalResponse listPhoneNumbers();
    UniversalResponse filterPhoneNumbers(FilterRequest filterRequest);
}
