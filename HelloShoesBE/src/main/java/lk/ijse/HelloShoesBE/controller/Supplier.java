package lk.ijse.HelloShoesBE.controller;

import lk.ijse.HelloShoesBE.dto.SupplierDTO;
import lk.ijse.HelloShoesBE.service.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            supplierService.saveSupplier(supplierDTO);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}