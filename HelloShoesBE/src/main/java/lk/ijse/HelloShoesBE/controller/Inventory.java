package lk.ijse.HelloShoesBE.controller;

import lk.ijse.HelloShoesBE.dto.InventoryDTO;
import lk.ijse.HelloShoesBE.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.regex.Pattern;

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

    private void validateInventory(InventoryDTO inventoryDTO) {
        if (!Pattern.compile("^[S]-\\d{4}$").matcher(inventoryDTO.getSupplierCode()).matches()) {
            throw new RuntimeException("Invalid Supplier Code.");
        }
        if (!Pattern.compile("^\\d*\\.?\\d+$").matcher(Double.toString(inventoryDTO.getBuyPrice())).matches()) {
            throw new RuntimeException("Invalid Buy Price.");
        }
        if (!Pattern.compile("^\\d*\\.?\\d+$").matcher(Double.toString(inventoryDTO.getSellPrice())).matches()) {
            throw new RuntimeException("Invalid Sell Price.");
        }
        if (!Pattern.compile("^\\d*\\.?\\d+$").matcher(Double.toString(inventoryDTO.getProfit())).matches()) {
            throw new RuntimeException("Invalid Profit.");
        }
        if (!Pattern.compile("^\\d*\\.?\\d+$").matcher(Double.toString(inventoryDTO.getProfitMargin())).matches()) {
            throw new RuntimeException("Invalid Profit Margin.");
        }
        System.out.println("Inventory validated.");
    }
}