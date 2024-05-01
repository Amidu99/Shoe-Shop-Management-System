package lk.ijse.HelloShoesBE.controller;

import lk.ijse.HelloShoesBE.dto.InventoryDTO;
import lk.ijse.HelloShoesBE.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
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

    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveInventory(@RequestBody InventoryDTO inventoryDTO) {
        try {
            validateInventory(inventoryDTO);
            if (inventoryService.existsByItemCode(inventoryDTO.getItemCode())) {
                System.out.println("Exists Item.");
                return ResponseEntity.badRequest().body("This item already exists.");
            }
            inventoryService.saveInventory(inventoryDTO);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/get")
    public ResponseEntity<?> getOneInventory(@RequestHeader String itemCode){
        boolean isExists = inventoryService.existsByItemCode(itemCode);
        if (!isExists){
            System.out.println("Not Exists Item.");
            return ResponseEntity.noContent().build();
        }
        InventoryDTO inventoryDTO = inventoryService.getInventoryByItemCode(itemCode);
        System.out.println("Item founded: "+inventoryDTO);
        return ResponseEntity.ok(inventoryDTO);
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllInventories(){
        List<InventoryDTO> allItems = inventoryService.getAllInventories();
        System.out.println("No of all items: "+allItems.size());
        if (allItems.size() == 0) return ResponseEntity.ok().body("No items found");
        return ResponseEntity.ok().body(allItems);
    }

    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateInventory(@RequestBody InventoryDTO inventoryDTO) {
        try {
            validateInventory(inventoryDTO);
            if (inventoryService.existsByItemCode(inventoryDTO.getItemCode())) {
                inventoryService.updateInventory(inventoryDTO);
                return ResponseEntity.ok().build();
            }
            System.out.println("Not Exists Item.");
            return ResponseEntity.badRequest().body("This item is not exists.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
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