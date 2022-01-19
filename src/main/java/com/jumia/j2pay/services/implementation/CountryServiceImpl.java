package com.jumia.j2pay.services.implementation;

import com.jumia.j2pay.dto.response.UniversalResponse;
import com.jumia.j2pay.dto.util.FilterRequest;
import com.jumia.j2pay.model.Country;
import com.jumia.j2pay.model.Customer;
import com.jumia.j2pay.repository.CustomerRepository;
import com.jumia.j2pay.services.interfaces.ICountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Alex Maina
 * @created 17/01/2022
 */
@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements ICountryService {
    private final CustomerRepository customerRepository;


    @Override
    public UniversalResponse listCountries() {
        List<String> dataList= Arrays.stream(Country.values()).map(Country::getName).collect(Collectors.toList());
        return new UniversalResponse("success","List of countries",dataList);
    }

    @Override
    public UniversalResponse listPhoneNumbers() {
        List<String> dataList= customerRepository.findAll().stream().map(Customer::getPhone).collect(Collectors.toList());
        return new UniversalResponse("success","Customers list",dataList);
    }
    Predicate<String> validatePhone(){
        return phone-> {
            String countryCode= phone.split(" ")[0]
                    .replace("(","")
                    .replace(")","")
                    .trim();
            Country countryByCode= Arrays.stream(Country.values())
                    .filter(country -> country.getCode().equals(countryCode))
                    .findAny()
                    .orElse(null);
            if(countryByCode==null) return false;
            String countryRegex= countryByCode.getRegex();
            Pattern pattern= Pattern.compile(countryRegex);
            Matcher matcher= pattern.matcher(phone);
            return matcher.matches();
        };
    }
    BiPredicate<String,String> validateByCountryName(){
        return (phone,name)-> {
            String countryCode= phone.split(" ")[0]
                    .replace("(","")
                    .replace(")","")
                    .trim();
            Country country= Arrays.stream(Country.values())
                    .filter(c -> c.getCode().equals(countryCode))
                    .findAny()
                    .orElse(null);
            if(country==null) return false;
            if(name.equalsIgnoreCase(country.getName())) return true;
            return false;
        };
    }
    @Override
    public UniversalResponse filterPhoneNumbers(FilterRequest filterRequest) {
        List<Customer> customerList= new ArrayList<>();
        if(filterRequest.getNumber()!=null){
            customerRepository.findByPhone(filterRequest.getNumber()).ifPresent(customerList::add);
        }else{
                int page= filterRequest.getPage();
                int size= filterRequest.getSize();
                Pageable pageable= PageRequest.of(page,size);
            customerList=customerRepository.findAll(pageable).toList();
        }

        if(filterRequest.getState()!=null){
            boolean isValid= filterRequest.getState().equalsIgnoreCase("valid");
            customerList=customerList.stream()
                    .filter(customer->validatePhone().test(customer.getPhone())==isValid)
                    .collect(Collectors.toList());
        }

        if(filterRequest.getCountry()!=null){
            String countryName= filterRequest.getCountry();
            String phone= filterRequest.getNumber();
            customerList=customerList.stream()
                    .filter(customer -> validateByCountryName().test(phone,countryName))
                    .collect(Collectors.toList());
        }


        }

}
