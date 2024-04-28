package lk.ijse.HelloShoesBE.controller;

import lk.ijse.HelloShoesBE.service.SupplierService;
import lombok.RequiredArgsConstructor;
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
}