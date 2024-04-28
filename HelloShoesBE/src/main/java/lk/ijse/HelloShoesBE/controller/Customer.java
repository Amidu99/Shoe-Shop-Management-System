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
            return ResponseEntity.noContent().build();
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

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteCustomer(@RequestHeader String customerCode){
        Boolean isExists = customerService.existsByCustomerCode(customerCode);
        if (!isExists){
            System.out.println("Not Exists Customer.");
            return ResponseEntity.badRequest().body("Customer not found!");
        }
        customerService.deleteCustomer(customerCode);
        System.out.println("Customer deleted.");
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getNextCode")
    public ResponseEntity<?> getNextCustomerCode(){
        String lastCustomerCode = customerService.getLastCustomerCode();
        System.out.println("Last CustomerCode: "+lastCustomerCode);
        if (lastCustomerCode==null) return ResponseEntity.ok("C-0001");
        int nextCode = Integer.parseInt(lastCustomerCode.replace("C-", "")) + 1;
        System.out.println("Next CustomerCode: "+nextCode);
        return ResponseEntity.ok(String.format("C-%04d", nextCode));
    }

    private void validateCustomer(CustomerDTO customerDTO) {
        if (!Pattern.compile("^[C]-\\d{4}$").matcher(customerDTO.getCustomerCode()).matches()) {
            throw new RuntimeException("Invalid Customer Code.");
        }
        if (!Pattern.compile("^[A-Za-z\\s]{3,}$").matcher(customerDTO.getCustomerName()).matches()) {
            throw new RuntimeException("Invalid Customer Name.");
        }
        if (!Pattern.compile("^(?:0|94|\\+94|0094)?(?:(11|21|23|24|25|26|27|31|32|33|34|35|36|37|38|41|45|47|51|52|54|55|57|63|65|66|67|81|91)(0|2|3|4|5|7|9)|7(0|1|2|4|5|6|7|8)\\d)\\d{6}$").matcher(customerDTO.getContactNo()).matches()) {
            throw new RuntimeException("Invalid Customer Contact Number.");
        }
        System.out.println("Customer validated.");
    }
}