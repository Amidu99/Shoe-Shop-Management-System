package lk.ijse.HelloShoesBE.controller;

import jakarta.annotation.security.RolesAllowed;
import lk.ijse.HelloShoesBE.dto.InventoryDTO;
import lk.ijse.HelloShoesBE.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
public class Inventory {
    final static Logger logger = LoggerFactory.getLogger(Inventory.class);
    private final InventoryService inventoryService;

    @GetMapping("/health")
    @RolesAllowed({"ADMIN", "USER"})
    public String healthTest(){
        logger.info("Inventory Health Test Passed.");
        return "Inventory Health Test Passed.";
    }

    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<?> saveInventory(@RequestBody InventoryDTO inventoryDTO) {
        try {
            validateInventory(inventoryDTO);
            if (inventoryService.existsByItemCode(inventoryDTO.getItemCode())) {
                logger.info("Exists Item.");
                return ResponseEntity.badRequest().body("This item already exists.");
            }
            inventoryService.saveInventory(inventoryDTO);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/get")
    @RolesAllowed({"ADMIN", "USER"})
    public ResponseEntity<?> getOneInventory(@RequestHeader String itemCode){
        boolean isExists = inventoryService.existsByItemCode(itemCode);
        if (!isExists){
            logger.info("Not Exists Item.");
            return ResponseEntity.noContent().build();
        }
        InventoryDTO inventoryDTO = inventoryService.getInventoryByItemCode(itemCode);
        logger.info("Item founded: "+inventoryDTO.getItemCode());
        return ResponseEntity.ok(inventoryDTO);
    }

    @GetMapping("/getAll")
    @RolesAllowed({"ADMIN", "USER"})
    public ResponseEntity<?> getAllInventories(){
        List<InventoryDTO> allItems = inventoryService.getAllInventories();
        logger.info("No of all items: "+allItems.size());
        if (allItems.size() == 0) return ResponseEntity.ok().body("No items found");
        return ResponseEntity.ok().body(allItems);
    }

    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<?> updateInventory(@RequestBody InventoryDTO inventoryDTO) {
        try {
            validateInventory(inventoryDTO);
            if (inventoryService.existsByItemCode(inventoryDTO.getItemCode())) {
                inventoryService.updateInventory(inventoryDTO);
                return ResponseEntity.ok().build();
            }
            logger.info("Not Exists Item.");
            return ResponseEntity.badRequest().body("This item is not exists.");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<?> deleteInventory(@RequestHeader String itemCode){
        boolean isExists = inventoryService.existsByItemCode(itemCode);
        if (!isExists){
            logger.info("Not Exists Item.");
            return ResponseEntity.badRequest().body("Item not found!");
        }
        inventoryService.deleteInventory(itemCode);
        logger.info("Item deleted.");
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getNextCode")
    @RolesAllowed({"ADMIN", "USER"})
    public ResponseEntity<?> getNextInventoryCode(){
        String lastItemCode = inventoryService.getLastItemCode();
        logger.info("Last ItemCode: "+lastItemCode);
        if (lastItemCode==null) return ResponseEntity.ok("0001");
        int nextCode = Integer.parseInt(lastItemCode) + 1;
        logger.info("Next ItemCode: "+nextCode+" : "+String.format("%04d", nextCode));
        return ResponseEntity.ok(String.format("%04d", nextCode));
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
        logger.info("Inventory validated.");
    }
}