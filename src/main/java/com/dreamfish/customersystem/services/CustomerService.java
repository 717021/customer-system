package com.dreamfish.customersystem.services;

import com.dreamfish.customersystem.entity.Customer;
import com.dreamfish.customersystem.entity.CustomerIndustry;
import com.dreamfish.customersystem.utils.Result;

public interface CustomerService {
    Result getCustomerIndustry();
    Result addCustomerIndustry(CustomerIndustry customerIndustry);
    Result getCustomersPageable(Integer pageIndex, Integer pageSize, Boolean search, Customer searchParam);
    Result deleteCustomer(Integer userId, Integer customerId);
    Result newCustomer(Integer userId, Customer customer);
    Result updateCustomer(Integer userId, Integer customerId, Customer customer);
    Result getCustomer(Integer customerId);

    Result getCustomerCount();
    Result getCustomerCountByUserId(Integer userId);
}
