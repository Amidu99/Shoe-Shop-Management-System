package lk.ijse.HelloShoesBE.controller;

import lk.ijse.HelloShoesBE.dto.SupplierDTO;
import lk.ijse.HelloShoesBE.service.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
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
            if (supplierService.existsBySupplierCode(supplierDTO.getSupplierCode())) {
                System.out.println("Exists Supplier.");
                return ResponseEntity.badRequest().body("This supplier already exists.");
            }
            supplierService.saveSupplier(supplierDTO);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/get")
    public ResponseEntity<?> getOneSupplier(@RequestHeader String supplierCode){
        boolean isExists = supplierService.existsBySupplierCode(supplierCode);
        if (!isExists){
            System.out.println("Not Exists Supplier.");
            return ResponseEntity.noContent().build();
        }
        SupplierDTO supplierDTO = supplierService.getSupplierBySupplierCode(supplierCode);
        System.out.println("Supplier founded: "+supplierDTO);
        return ResponseEntity.ok(supplierDTO);
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllSuppliers(){
        List<SupplierDTO> allSuppliers = supplierService.getAllSuppliers();
        System.out.println("No of all suppliers: "+allSuppliers.size());
        if (allSuppliers.size() == 0) return ResponseEntity.ok().body("No suppliers found");
        return ResponseEntity.ok().body(allSuppliers);
    }

    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateSupplier(@RequestBody SupplierDTO supplierDTO) {
        try {
            validateSupplier(supplierDTO);
            if (supplierService.existsBySupplierCode(supplierDTO.getSupplierCode())) {
                supplierService.updateSupplier(supplierDTO);
                return ResponseEntity.ok().build();
            }
            System.out.println("Not Exists Supplier.");
            return ResponseEntity.badRequest().body("This supplier not exists.");
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