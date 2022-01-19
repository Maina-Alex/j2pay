package com.jumia.j2pay.repository;

import com.jumia.j2pay.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Alex Maina
 * @created 18/01/2022
 */
public interface CountryRepository extends JpaRepository<Country,Long> {
}
