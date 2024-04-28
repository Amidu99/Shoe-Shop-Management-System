package lk.ijse.HelloShoesBE.controller;

import lk.ijse.HelloShoesBE.service.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/supplier")
@RequiredArgsConstructor
public class Supplier {
    private final SupplierService supplierService;

}