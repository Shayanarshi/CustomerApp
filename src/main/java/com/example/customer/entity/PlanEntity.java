package com.example.customer.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PlanEntity {

    private String planId;

    private String planName;

    private String description;

    private String validity;


}
