package lk.ijse.HelloShoesBE.controller;

import jakarta.annotation.security.RolesAllowed;
import lk.ijse.HelloShoesBE.dto.SupplierInventoriesDTO;
import lk.ijse.HelloShoesBE.service.StockService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/v1/stock")
@RequiredArgsConstructor
public class Stock {
    final static Logger logger = LoggerFactory.getLogger(Stock.class);
    private final StockService stockService;

    @GetMapping("/health")
    @RolesAllowed({"ADMIN", "USER"})
    public String healthTest(){
        logger.info("Stock Health Test Passed.");
        return "Stock Health Test Passed.";
    }

    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<?> saveStock(@RequestBody SupplierInventoriesDTO supplierInventoriesDTO) {
        try {
            validateStock(supplierInventoriesDTO);
            if (stockService.existsByStockCode(supplierInventoriesDTO.getStockCode())) {
                logger.info("Exists Stock.");
                return ResponseEntity.badRequest().body("This stock is already exists.");
            }
            if (stockService.existsByItemCodeAndSize(supplierInventoriesDTO.getItemCode(), supplierInventoriesDTO.getSize())) {
                logger.info("Exists Stock.");
                return ResponseEntity.badRequest().body("This stock is already exists.");
            }
            stockService.saveStock(supplierInventoriesDTO);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/get")
    @RolesAllowed({"ADMIN", "USER"})
    public ResponseEntity<?> getOneStock(@RequestHeader String stockCode){
        boolean isExists = stockService.existsByStockCode(stockCode);
        if (!isExists){
            logger.info("Not Exists Stock.");
            return ResponseEntity.noContent().build();
        }
        SupplierInventoriesDTO stock = stockService.getStockByStockCode(stockCode);
        logger.info("Stock founded: "+stock);
        return ResponseEntity.ok(stock);
    }

    @GetMapping("/getStock")
    @RolesAllowed({"ADMIN", "USER"})
    public ResponseEntity<?> getItemSizeStock(@RequestHeader String itemCode, @RequestHeader int size){
        boolean isExists = stockService.existsByItemCodeAndSize(itemCode, size);
        if (!isExists){
            logger.info("Not Exists Stock.");
            return ResponseEntity.noContent().build();
        }
        logger.info("This stock is already exists.");
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getStockDetail")
    @RolesAllowed({"ADMIN", "USER"})
    public ResponseEntity<?> getItemSizeStockDetail(@RequestHeader String itemCode, @RequestHeader int size){
        SupplierInventoriesDTO stock = stockService.getExistsByItemCodeAndSize(itemCode, size);
        if (stock!=null){
            logger.info("Found the stock.");
            return ResponseEntity.ok(stock);
        }
        logger.info("Not Exists Stock.");
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/checkThisStock")
    @RolesAllowed({"ADMIN", "USER"})
    public ResponseEntity<?> checkThisStock(@RequestHeader String stockCode, @RequestHeader String itemCode, @RequestHeader int size){
        boolean isExists = stockService.existsByStockCodeItemCodeSize(stockCode, itemCode, size);
        if (!isExists){
            logger.info("Not Exists Stock.");
            return ResponseEntity.noContent().build();
        }
        logger.info("This stock is already exists.");
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getAll")
    @RolesAllowed({"ADMIN", "USER"})
    public ResponseEntity<?> getAllStocks(){
        List<SupplierInventoriesDTO> allStocks = stockService.getAllStocks();
        logger.info("No of all stocks: "+allStocks.size());
        if (allStocks.size() == 0) return ResponseEntity.ok().body("No stocks found");
        return ResponseEntity.ok().body(allStocks);
    }

    @GetMapping("/getLowStocks")
    @RolesAllowed({"ADMIN", "USER"})
    public ResponseEntity<?> getLowStocks(){
        List<SupplierInventoriesDTO> lowStocks = stockService.getLowStocks();
        logger.info("No of low stocks: "+lowStocks.size());
        if (lowStocks.size() == 0) return ResponseEntity.ok().body("No low stocks found");
        return ResponseEntity.ok().body(lowStocks);
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
            logger.info("Not Exists Stock.");
            return ResponseEntity.badRequest().body("This stock is not exists.");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<?> deleteStock(@RequestHeader String stockCode){
        boolean isExists = stockService.existsByStockCode(stockCode);
        if (!isExists){
            logger.info("Not Exists Stock.");
            return ResponseEntity.badRequest().body("Stock not found!");
        }
        stockService.deleteStock(stockCode);
        logger.info("Stock deleted.");
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getNextCode")
    @RolesAllowed({"ADMIN", "USER"})
    public ResponseEntity<?> getNextStockCode(){
        String lastStockCode = stockService.getLastStockCode();
        logger.info("Last StockCode: "+lastStockCode);
        if (lastStockCode==null) return ResponseEntity.ok("ST-0001");
        int nextCode = Integer.parseInt(lastStockCode.replace("ST-", "")) + 1;
        logger.info("Next StockCode: "+nextCode);
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
        logger.info("Stock validated.");
    }
}