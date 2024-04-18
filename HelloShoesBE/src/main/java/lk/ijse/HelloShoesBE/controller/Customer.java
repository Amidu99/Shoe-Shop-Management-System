package lk.ijse.HelloShoesBE.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/customer")
public class Customer {
    @GetMapping("/health")
    public String healthTest(){
        System.out.println("Customer Health Test Passed.");
        return "Customer Health Test Passed.";
    }
}