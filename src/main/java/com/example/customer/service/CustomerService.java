package com.example.customer.service;

import com.example.customer.entity.CustomerEntity;
import com.example.customer.model.LoginRequest;
import com.example.customer.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    @Autowired
    CustomerRepository repository;

    public boolean registerCustomer(CustomerEntity customer){
        if (repository.existsById(customer.getPhoneNumber())==false){
            repository.save(customer);
            return true;
        }else {
            return false;
        }
    }

    public  boolean loginCustomer(LoginRequest loginRequest){

        if (repository.checkLogin(loginRequest.getPhoneNumber(),loginRequest.getPassword())==1){
            return true;
        }else {
            return false;
        }
    }

    public CustomerEntity readCustomer(Long phoneNumber) {

        CustomerEntity customer = repository.findById(phoneNumber).get();
        return customer;
    }



}
