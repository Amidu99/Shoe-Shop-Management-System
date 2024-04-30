package lk.ijse.HelloShoesBE.controller;

import lk.ijse.HelloShoesBE.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/employee")
@RequiredArgsConstructor
public class Employee {
    private final EmployeeService employeeService;

    @GetMapping("/health")
    public String healthTest(){
        System.out.println("Employee Health Test Passed.");
        return "Employee Health Test Passed.";
    }
}