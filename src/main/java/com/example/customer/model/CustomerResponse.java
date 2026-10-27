package com.example.customer.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CustomerResponse {
    private  Long  phoneNumber;
    private  String userName;
    private  String email;
    private  String planId;
    private  String description;
    private  String validity;
    private  List<Object[]> friendsContactNumbers;
}