package lk.ijse.HelloShoesBE.controller;

import lk.ijse.HelloShoesBE.dto.SupplierInventoriesDTO;
import lk.ijse.HelloShoesBE.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/v1/stock")
@RequiredArgsConstructor
public class Stock {
    private final StockService stockService;

    @GetMapping("/health")
    public String healthTest(){
        System.out.println("Stock Health Test Passed.");
        return "Stock Health Test Passed.";
    }

    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveStock(@RequestBody SupplierInventoriesDTO supplierInventoriesDTO) {
        try {
            validateStock(supplierInventoriesDTO);
            if (stockService.existsByStockCode(supplierInventoriesDTO.getStockCode())) {
                System.out.println("Exists Stock.");
                return ResponseEntity.badRequest().body("This stock is already exists.");
            }
            if (stockService.existsByItemCodeAndSize(supplierInventoriesDTO.getItemCode(), supplierInventoriesDTO.getSize())) {
                System.out.println("Exists Stock.");
                return ResponseEntity.badRequest().body("This stock is already exists.");
            }
            stockService.saveStock(supplierInventoriesDTO);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private void validateStock(SupplierInventoriesDTO supplierInventoriesDTO) {
        if (!Pattern.compile("^ST-\\d{4}$").matcher(supplierInventoriesDTO.getStockCode()).matches()) {
            throw new RuntimeException("Invalid Stock Code.");
        }
        if (!Pattern.compile("^[1-9]\\d*$").matcher(Integer.toString(supplierInventoriesDTO.getSize())).matches()) {
            throw new RuntimeException("Invalid Size.");
        }
        if (!Pattern.compile("^[1-9]\\d*$").matcher(Integer.toString(supplierInventoriesDTO.getOriginalQty())).matches()) {
            throw new RuntimeException("Invalid Original Qty.");
        }
        if (!Pattern.compile("^[1-9]\\d*$").matcher(Integer.toString(supplierInventoriesDTO.getAvailableQty())).matches()) {
            throw new RuntimeException("Invalid Available Qty.");
        }
        if (!Pattern.compile("^[A-Za-z]{4}-\\d{4}$").matcher(supplierInventoriesDTO.getItemCode()).matches()) {
            throw new RuntimeException("Invalid Item Code.");
        }
        if (!Pattern.compile("^[S]-\\d{4}$").matcher(supplierInventoriesDTO.getSupplierCode()).matches()) {
            throw new RuntimeException("Invalid Supplier Code.");
        }
        System.out.println("Stock validated.");
    }
}