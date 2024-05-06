package lk.ijse.HelloShoesBE.controller;

import lk.ijse.HelloShoesBE.dto.SupplierInventoriesDTO;
import lk.ijse.HelloShoesBE.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

    private void validateStock(SupplierInventoriesDTO supplierInventoriesDTO) {
        if (!Pattern.compile("^[ST]-\\d{4}$").matcher(supplierInventoriesDTO.getStockCode()).matches()) {
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
        if (!Pattern.compile("^[S]-\\d{4}$").matcher(supplierInventoriesDTO.getStockCode()).matches()) {
            throw new RuntimeException("Invalid Supplier Code.");
        }
        System.out.println("Stock validated.");
    }
}