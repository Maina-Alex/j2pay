package com.jumia.j2pay.services.implementation;

import com.jumia.j2pay.dto.response.CustomerDto;
import com.jumia.j2pay.dto.response.Pagination;
import com.jumia.j2pay.dto.response.UniversalResponse;
import com.jumia.j2pay.dto.util.FilterRequest;
import com.jumia.j2pay.model.Country;
import com.jumia.j2pay.model.Customer;
import com.jumia.j2pay.repository.CustomerRepository;
import com.jumia.j2pay.services.interfaces.ICustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

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
public class CustomerServiceImpl implements ICustomerService {
    private final CustomerRepository customerRepository;

    @Override
    public Mono<UniversalResponse> listCustomers(Pageable pageable) {
        return Mono.fromCallable(()->{
            List<Customer> customers= customerRepository.findAll(pageable).toList();
            long pageNumber= customerRepository.count();
            Pagination pagination= Pagination
                            .builder()
                            .page(pageable.getPageNumber())
                            .size(pageable.getPageSize())
                            .recordSize(pageNumber)
                    .pages((int) Math.ceil(pageNumber/pageable.getPageSize()))
                    .build();

            List<CustomerDto> customerDtos= customers.stream()
                    .map(customer -> {
                        boolean isValid= validatePhone().test(customer.getPhone());
                        String countryCode= customer.getPhone().split(" ")[0]
                                .replace("(","")
                                .replace(")","")
                                .trim();
                        Country countryByCode= Arrays.stream(Country.values())
                                .filter(country -> country.getCode().equals(countryCode))
                                .findAny()
                                .orElse(null);
                        String countryName= countryByCode!=null?countryByCode.getName():"";
                        return CustomerDto.builder().id(customer.getId())
                                .country(countryName)
                                .valid(isValid? "Valid":"Invalid")
                                .phone(customer.getPhone())
                                .name(customer.getName())
                                .build();
                    })
                    .collect(Collectors.toList());
            return new UniversalResponse("success","List of countries",customerDtos,pagination);
        })
                .publishOn(Schedulers.boundedElastic());
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
    BiPredicate<String,String> filterByCountryName(){
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
            return name.equalsIgnoreCase(country.getName());
        };
    }

    BiPredicate<String,Customer> filterByCountryCode(){
        return (code,customer)->{
            List<String> countryCodeList= Arrays.stream(Country.values())
                    .map(Country::getCode)
                    .collect(Collectors.toList());
            if(!countryCodeList.contains(code))return  false;
            String customerCode= customer.getPhone().split(" ")[0]
                    .replace("(","")
                    .replace(")","")
                    .trim();
            return customerCode.equalsIgnoreCase(code);
        };
    }

    @Override
    public Mono<UniversalResponse> filterPhoneNumbers(FilterRequest filterRequest) {
        return Mono.fromCallable(()->{
            List<Customer> customerList= new ArrayList<>();
            if(filterRequest.getNumber()!=null){
                customerRepository.findByPhone(filterRequest.getNumber()).ifPresent(customerList::add);
            }else{
                int page= filterRequest.getPage()!=0?filterRequest.getPage():1 ;
                int size= filterRequest.getSize()!=0? filterRequest.getSize():15;
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
                customerList=customerList.stream()
                        .filter(customer -> filterByCountryName().test(customer.getPhone(),countryName))
                        .collect(Collectors.toList());
            }
            if(filterRequest.getCode()!=null){
                String code= filterRequest.getCode();
                customerList= customerList.stream()
                        .filter(customer-> filterByCountryCode().test(code,customer))
                        .collect(Collectors.toList());
            }
            Pagination pagination= Pagination.builder()
                    .page(filterRequest.getPage())
                    .size(filterRequest.getSize())
                    .recordSize(customerList.size())
                    .pages((int) Math.ceil( (filterRequest.getSize()!=0)?customerList.size()/filterRequest.getSize():0))
                    .build();
            List<CustomerDto> customerDtos= customerList.stream()
                            .map(customer -> {
                                boolean isValid= validatePhone().test(customer.getPhone());
                                String countryCode= customer.getPhone().split(" ")[0]
                                        .replace("(","")
                                        .replace(")","")
                                        .trim();
                                Country countryByCode= Arrays.stream(Country.values())
                                        .filter(country -> country.getCode().equals(countryCode))
                                        .findAny()
                                        .orElse(null);
                                String countryName= countryByCode!=null?countryByCode.getName():"";
                                return CustomerDto.builder().id(customer.getId())
                                        .country(countryName)
                                        .valid(isValid? "Valid":"Invalid")
                                        .phone(customer.getPhone())
                                        .name(customer.getName())
                                        .build();
                            })
                            .collect(Collectors.toList());
            return new UniversalResponse("success","filtered search", customerDtos,pagination);
        })
                .publishOn(Schedulers.boundedElastic());

        }

}
