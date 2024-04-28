package lk.ijse.HelloShoesBE.controller;

import lk.ijse.HelloShoesBE.dto.SupplierDTO;
import lk.ijse.HelloShoesBE.service.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/v1/supplier")
@RequiredArgsConstructor
public class Supplier {
    private final SupplierService supplierService;

    @GetMapping("/health")
    public String healthTest(){
        System.out.println("Supplier Health Test Passed.");
        return "Supplier Health Test Passed.";
    }

    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveSupplier(@RequestBody SupplierDTO supplierDTO) {
        try {
            validateSupplier(supplierDTO);
            supplierService.saveSupplier(supplierDTO);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private void validateSupplier(SupplierDTO supplierDTO) {
        if (!Pattern.compile("^[S]-\\d{4}$").matcher(supplierDTO.getSupplierCode()).matches()) {
            throw new RuntimeException("Invalid Supplier Code.");
        }
        if (!Pattern.compile("^[A-Za-z\\s]{3,}$").matcher(supplierDTO.getSupplierName()).matches()) {
            throw new RuntimeException("Invalid Supplier Name.");
        }
        if (!Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$").matcher(supplierDTO.getEmail()).matches()) {
            throw new RuntimeException("Invalid Supplier Email.");
        }
        System.out.println("Supplier validated.");
    }
}