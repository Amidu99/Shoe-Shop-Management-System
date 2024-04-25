package lk.ijse.HelloShoesBE.controller;

import lk.ijse.HelloShoesBE.dto.CustomerDTO;
import lk.ijse.HelloShoesBE.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/v1/customer")
@RequiredArgsConstructor
public class Customer {
    private final CustomerService customerService;

    @GetMapping("/health")
    public String healthTest(){
        System.out.println("Customer Health Test Passed.");
        return "Customer Health Test Passed.";
    }

    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveCustomer(@RequestBody CustomerDTO customerDTO) {
        try {
            if (customerService.existsByCustomerCode(customerDTO.getCustomerCode())) {
                System.out.println("Exists Customer.");
                return ResponseEntity.badRequest().body("This customer already exists.");
            }
            customerService.saveCustomer(customerDTO);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}