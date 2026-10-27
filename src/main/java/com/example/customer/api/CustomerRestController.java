package com.example.customer.api;

import com.example.customer.entity.CustomerEntity;
import com.example.customer.entity.PlanEntity;
import com.example.customer.model.CustomerResponse;
import com.example.customer.model.LoginRequest;
import com.example.customer.service.CustomerService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CustomerRestController {

    private static final String PLAN_URL = "http://PLANAPP/plans/{id}";

    private static final String FRIEND_URL = "http://FRIENDAPP/friend/{phoneNumber}";
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    CustomerService service;

    @PostMapping(value = "/customer/register")
    public boolean addCustomer(@RequestBody CustomerEntity customer) {
        return service.registerCustomer(customer);
    }

    @PostMapping(value = "/customer/login")
    public boolean loginCustomer(@RequestBody LoginRequest loginRequest) {
        return service.loginCustomer(loginRequest);
    }

    @GetMapping("/customer/profile/{phoneNumber}")
    @CircuitBreaker(name="profile-circuit", fallbackMethod = "showProfileFallback")

    public CustomerResponse showProfile(@PathVariable Long phoneNumber) {

        CustomerEntity customerEntity = service.readCustomer(phoneNumber);

        CustomerResponse customerResponse = new CustomerResponse();

        BeanUtils.copyProperties(customerEntity, customerResponse);

        

        // calling plan microservice
        PlanEntity planEntity = callPlanService(customerEntity.getPlanId());

        BeanUtils.copyProperties(planEntity, customerResponse);

        // calling friend microservice
        List<Object[]> friendsContactNumbers = callFriendService(phoneNumber);
        customerResponse.setFriendsContactNumbers(friendsContactNumbers);

        return customerResponse;
    }

    public PlanEntity callPlanService(String planId) {
        ResponseEntity<PlanEntity> re = restTemplate.getForEntity(PLAN_URL, PlanEntity.class,
                planId);

        PlanEntity planEntity = re.getBody();
        return planEntity;
    }

    public List<Object[]>  callFriendService(long phoneNumber) {
        ParameterizedTypeReference<List<Object[]>> typeRef = new ParameterizedTypeReference<List<Object[]>>() {};

        ResponseEntity<List<Object[]>> re2 = restTemplate.exchange(FRIEND_URL, HttpMethod.GET, null, typeRef, phoneNumber);
        List<Object[]> friendsContactNumbers = re2.getBody();
        return friendsContactNumbers;
    }
    //  fall back method for entire show profile
   public CustomerResponse showProfileFallback(Long phoneNumber, Throwable t) {

        CustomerEntity customerEntity = service.readCustomer(phoneNumber);

        CustomerResponse customerResponse = new CustomerResponse();
        
        BeanUtils.copyProperties(customerEntity, customerResponse);

        // calling plan microservice
        PlanEntity planEntity = callPlanService(customerEntity.getPlanId());
        BeanUtils.copyProperties(planEntity, customerResponse);

        // calling friend microservice
        List<Object[]> friendsContactNumbers = callFriendServiceFallback(phoneNumber);
        customerResponse.setFriendsContactNumbers(friendsContactNumbers);

        return customerResponse;
    }
    public List<Object[]> callFriendServiceFallback(long phoneNumber) {
        return new ArrayList<Object[]>();
    }
}