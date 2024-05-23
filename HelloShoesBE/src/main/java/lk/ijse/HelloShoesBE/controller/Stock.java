package lk.ijse.HelloShoesBE.controller;

import jakarta.annotation.security.RolesAllowed;
import lk.ijse.HelloShoesBE.dto.SupplierInventoriesDTO;
import lk.ijse.HelloShoesBE.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/v1/stock")
@RequiredArgsConstructor
public class Stock {
    private final StockService stockService;

    @GetMapping("/health")
    @RolesAllowed({"ADMIN", "USER"})
    public String healthTest(){
        System.out.println("Stock Health Test Passed.");
        return "Stock Health Test Passed.";
    }

    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({"ADMIN"})
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

    @GetMapping("/get")
    @RolesAllowed({"ADMIN", "USER"})
    public ResponseEntity<?> getOneStock(@RequestHeader String stockCode){
        boolean isExists = stockService.existsByStockCode(stockCode);
        if (!isExists){
            System.out.println("Not Exists Stock.");
            return ResponseEntity.noContent().build();
        }
        SupplierInventoriesDTO stock = stockService.getStockByStockCode(stockCode);
        System.out.println("Stock founded: "+stock);
        return ResponseEntity.ok(stock);
    }

    @GetMapping("/getStock")
    @RolesAllowed({"ADMIN", "USER"})
    public ResponseEntity<?> getItemSizeStock(@RequestHeader String itemCode, @RequestHeader int size){
        boolean isExists = stockService.existsByItemCodeAndSize(itemCode, size);
        if (!isExists){
            System.out.println("Not Exists Stock.");
            return ResponseEntity.noContent().build();
        }
        System.out.println("This stock is already exists.");
        return ResponseEntity.ok().build();
    }

    @GetMapping("/checkThisStock")
    @RolesAllowed({"ADMIN", "USER"})
    public ResponseEntity<?> checkThisStock(@RequestHeader String stockCode, @RequestHeader String itemCode, @RequestHeader int size){
        boolean isExists = stockService.existsByStockCodeItemCodeSize(stockCode, itemCode, size);
        if (!isExists){
            System.out.println("Not Exists Stock.");
            return ResponseEntity.noContent().build();
        }
        System.out.println("This stock is already exists.");
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getAll")
    @RolesAllowed({"ADMIN", "USER"})
    public ResponseEntity<?> getAllStocks(){
        List<SupplierInventoriesDTO> allStocks = stockService.getAllStocks();
        System.out.println("No of all stocks: "+allStocks.size());
        if (allStocks.size() == 0) return ResponseEntity.ok().body("No stocks found");
        return ResponseEntity.ok().body(allStocks);
    }

    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<?> updateStock(@RequestBody SupplierInventoriesDTO supplierInventoriesDTO) {
        try {
            validateStock(supplierInventoriesDTO);
            if (stockService.existsByStockCode(supplierInventoriesDTO.getStockCode())) {
                stockService.updateStock(supplierInventoriesDTO);
                return ResponseEntity.ok().build();
            }
            System.out.println("Not Exists Stock.");
            return ResponseEntity.badRequest().body("This stock is not exists.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<?> deleteStock(@RequestHeader String stockCode){
        boolean isExists = stockService.existsByStockCode(stockCode);
        if (!isExists){
            System.out.println("Not Exists Stock.");
            return ResponseEntity.badRequest().body("Stock not found!");
        }
        stockService.deleteStock(stockCode);
        System.out.println("Stock deleted.");
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getNextCode")
    @RolesAllowed({"ADMIN", "USER"})
    public ResponseEntity<?> getNextStockCode(){
        String lastStockCode = stockService.getLastStockCode();
        System.out.println("Last StockCode: "+lastStockCode);
        if (lastStockCode==null) return ResponseEntity.ok("ST-0001");
        int nextCode = Integer.parseInt(lastStockCode.replace("ST-", "")) + 1;
        System.out.println("Next StockCode: "+nextCode);
        return ResponseEntity.ok(String.format("ST-%04d", nextCode));
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
        if (!Pattern.compile("^[0-9]\\d*$").matcher(Integer.toString(supplierInventoriesDTO.getAvailableQty())).matches()) {
            throw new RuntimeException("Invalid Available Qty.");
        }
        if (supplierInventoriesDTO.getAvailableQty() > supplierInventoriesDTO.getOriginalQty()) {
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