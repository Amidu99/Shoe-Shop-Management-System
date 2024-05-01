package lk.ijse.HelloShoesBE.controller;

import lk.ijse.HelloShoesBE.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
public class Inventory {
    private final InventoryService inventoryService;

    @GetMapping("/health")
    public String healthTest(){
        System.out.println("Inventory Health Test Passed.");
        return "Inventory Health Test Passed.";
    }
}