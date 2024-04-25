package lk.ijse.HelloShoesBE.controller;

import lk.ijse.HelloShoesBE.dto.CustomerDTO;
import lk.ijse.HelloShoesBE.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
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
            validateCustomer(customerDTO);
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

    @GetMapping("/get")
    public ResponseEntity<?> getOneCustomer(@RequestHeader String customerCode){
        Boolean isExists = customerService.existsByCustomerCode(customerCode);
        if (!isExists){
            System.out.println("Not Exists Customer.");
            return ResponseEntity.badRequest().body("Customer not found!");
        }
        CustomerDTO customerDTO = customerService.getCustomerByCustomerCode(customerCode);
        System.out.println("Customer founded: "+customerDTO);
        return ResponseEntity.ok(customerDTO);
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllCustomers(){
        List<CustomerDTO> allCustomers = customerService.getAllCustomers();
        System.out.println("No of all customers: "+allCustomers.size());
        if (allCustomers.size() == 0) return ResponseEntity.ok().body("No customers found");
        return ResponseEntity.ok().body(allCustomers);
    }

    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateCustomer(@RequestBody CustomerDTO customerDTO) {
        try {
            validateCustomer(customerDTO);
            if (customerService.existsByCustomerCode(customerDTO.getCustomerCode())) {
                customerService.updateCustomer(customerDTO);
                return ResponseEntity.ok().build();
            }
            System.out.println("Not Exists Customer.");
            return ResponseEntity.badRequest().body("This customer not exists.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private void validateCustomer(CustomerDTO customerDTO) {
        if (!Pattern.compile("^[C]-\\d{4}$").matcher(customerDTO.getCustomerCode()).matches()) {
            throw new RuntimeException("Invalid Customer Code.");
        }
        if (!Pattern.compile("^[A-Za-z\\s]{3,}$").matcher(customerDTO.getCustomerName()).matches()) {
            throw new RuntimeException("Invalid Customer Name.");
        }
        if (!Pattern.compile("^\\d{10}$").matcher(customerDTO.getContactNo()).matches()) {
            throw new RuntimeException("Invalid Customer Contact Number.");
        }
        System.out.println("Customer validated.");
    }
}