package com.example.rqchallenge.employees;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Employee {

    private int id;
    private String employeeName;
    private int employeeSalary;
    private int employeeAge;
    private String profileImage;

}
