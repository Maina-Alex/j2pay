package com.jumia.j2pay.repository;

import com.jumia.j2pay.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Alex Maina
 * @created 17/01/2022
 */
public interface CustomerRepository extends JpaRepository<Customer,Integer> {
    Optional<Customer> findByPhone(String phone);
    Page<Customer> findAll(Pageable pageable);

}
