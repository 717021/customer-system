package com.dreamfish.customersystem.services.impl;

import com.dreamfish.customersystem.entity.Customer;
import com.dreamfish.customersystem.entity.CustomerIndustry;
import com.dreamfish.customersystem.mapper.CustomerMapper;
import com.dreamfish.customersystem.repository.CustomerRepository;
import com.dreamfish.customersystem.services.CustomerService;
import com.dreamfish.customersystem.utils.Result;
import com.dreamfish.customersystem.utils.ResultCodeEnum;
import com.dreamfish.customersystem.utils.auth.PublicAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerMapper customerMapper = null;
    @Autowired
    private CustomerRepository customerRepository = null;


    @Override
    public Result getCustomersPageable(Integer pageIndex, Integer pageSize, Boolean search, Customer searchParam) {
        Pageable pageable = PageRequest.of(pageIndex, pageSize, Sort.by(Sort.Direction.ASC, "name"));
        if(!search) {
            return Result.success(customerRepository.findAll(pageable));
        }else{
            ExampleMatcher matcher = ExampleMatcher.matching()
                    .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.startsWith())
                    .withMatcher("address" ,ExampleMatcher.GenericPropertyMatchers.contains())
                    .withMatcher("source" ,ExampleMatcher.GenericPropertyMatchers.exact())
                    .withMatcher("industry" ,ExampleMatcher.GenericPropertyMatchers.exact())
                    .withMatcher("level" ,ExampleMatcher.GenericPropertyMatchers.exact());
            Example<Customer> sample = Example.of(searchParam, matcher);
            return Result.success(customerRepository.findAll(sample, pageable));
        }
    }

    @Override
    public Result getCustomerIndustry() {
        return Result.success(customerMapper.getCustomerIndustry());
    }

    @Override
    public Result addCustomerIndustry(CustomerIndustry customerIndustry) {
        String name = customerIndustry.getName();
        CustomerIndustry newIndustry = customerMapper.getCustomerIndustryByName(name);

        if(newIndustry!=null)
            return Result.success(name);

        customerMapper.addCustomerIndustry(name);
        return Result.success(name);
    }

    @Override
    public Result deleteCustomer(Integer userId, Integer customerId) {
        if(!customerRepository.existsById(customerId))
            return Result.failure(ResultCodeEnum.NOT_FOUNT);

        Customer customer = customerRepository.findById(userId).get();
        if(customer.getCreateId().intValue() != userId)
            return Result.failure(ResultCodeEnum.FORIBBEN);

        customerRepository.deleteById(customerId);
        return Result.success();
    }

    @Override
    public Result newCustomer(Integer userId, Customer customer) {
        customer.setUserId(userId);
        customer.setCreateId(userId);
        customer.setCreatetime(new Date());
        return Result.success(customerRepository.saveAndFlush(customer));
    }

    @Override
    public Result updateCustomer(Integer userId, Integer customerId, Customer customer) {

        Optional<Customer> customer1 = customerRepository.findById(userId);
        if(!customer1.isPresent())
            return Result.failure(ResultCodeEnum.NOT_FOUNT);
        Customer customer2 = customer1.get();
        if(customer2.getCreateId().intValue() != userId)
            return Result.failure(ResultCodeEnum.FORIBBEN);

        customer = customerRepository.saveAndFlush(customer);
        return Result.success(customer);
    }

    @Override
    public Result getCustomer(Integer customerId) {

        if(!customerRepository.existsById(customerId)) return Result.failure(ResultCodeEnum.NOT_FOUNT);

        return Result.success(customerMapper.getCustomerById(customerId));
    }

    @Override
    public Result getCustomerCount() {
        return Result.success(customerRepository.count());
    }

    @Override
    public Result getCustomerCountByUserId(Integer userId) {
        return Result.success(customerMapper.getCustomersByUserId(userId).size());
    }

}
