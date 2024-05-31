package lk.ijse.HelloShoesBE.controller;

import jakarta.annotation.security.RolesAllowed;
import lk.ijse.HelloShoesBE.dto.MostSoldItemDTO;
import lk.ijse.HelloShoesBE.dto.SaleDTO;
import lk.ijse.HelloShoesBE.service.SaleService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/v1/sale")
@RequiredArgsConstructor
public class Sale {
    final static Logger logger = LoggerFactory.getLogger(Sale.class);
    private final SaleService saleService;

    @GetMapping("/health")
    @RolesAllowed({"ADMIN", "USER"})
    public String healthTest(){
        logger.info("Sale Health Test Passed.");
        return "Sale Health Test Passed.";
    }

    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<?> saveSale(@RequestBody SaleDTO saleDTO) {
        try {
            validateSale(saleDTO);
            if (saleService.existsByOrderCode(saleDTO.getOrderCode())) {
                logger.info("Exists Order.");
                return ResponseEntity.badRequest().body("This order already exists.");
            }
            saleService.saveSale(saleDTO);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/get")
    @RolesAllowed({"ADMIN", "USER"})
    public ResponseEntity<?> getOneSale(@RequestHeader String orderCode){
        boolean isExists = saleService.existsByOrderCode(orderCode);
        if (!isExists){
            logger.info("Not Exists Order.");
            return ResponseEntity.noContent().build();
        }
        Optional<SaleDTO> saleDTO = saleService.getOrderByOrderCode(orderCode);
        logger.info("Order founded.");
        return ResponseEntity.ok(saleDTO);
    }

    @GetMapping("/getAll")
    @RolesAllowed({"ADMIN", "USER"})
    public ResponseEntity<?> getAllSales(){
        List<SaleDTO> allSales = saleService.getAllSales();
        logger.info("No of all sales: "+allSales.size());
        if (allSales.size() == 0) return ResponseEntity.ok().body("No sales found");
        return ResponseEntity.ok().body(allSales);
    }

    @GetMapping("/getNextCode")
    @RolesAllowed({"ADMIN", "USER"})
    public ResponseEntity<?> getNextOrderCode(){
        String lastOrderCode = saleService.getLastOrderCode();
        logger.info("Last OrderCode: "+lastOrderCode);
        if (lastOrderCode==null) return ResponseEntity.ok("O-0001");
        int nextCode = Integer.parseInt(lastOrderCode.replace("O-", "")) + 1;
        logger.info("Next OrderCode: "+nextCode);
        return ResponseEntity.ok(String.format("O-%04d", nextCode));
    }

    @GetMapping("/getSaleCount")
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<?> getSaleCountByDate(@RequestHeader Date day){
        int count = saleService.getSaleCountByDate(day);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/getMostSaleItem")
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<?> getMostSoldItemByDate(@RequestHeader Date day){
        Optional<MostSoldItemDTO> mostSoldItem = saleService.getMostSoldItemByDate(day);
        if (mostSoldItem.isPresent()) {
            return ResponseEntity.ok(mostSoldItem.get());
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<?> updateSale(@RequestBody SaleDTO saleDTO) {
        try {
            validateSale(saleDTO);
            if (saleService.existsByOrderCode(saleDTO.getOrderCode())) {
                saleService.updateSale(saleDTO);
                return ResponseEntity.ok().build();
            }
            logger.info("Not Exists Order.");
            return ResponseEntity.badRequest().body("This order is not exists.");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private void validateSale(SaleDTO saleDTO) {
        if (!Pattern.compile("^[O]-\\d{4}$").matcher(saleDTO.getOrderCode()).matches()) {
            throw new RuntimeException("Invalid Order Code.");
        }
        if (!Pattern.compile("^[A-Za-z\\s]{3,}$").matcher(saleDTO.getCustomerName()).matches()) {
            throw new RuntimeException("Invalid Customer Name.");
        }
        if (saleDTO.getUserCode()==null) {
            throw new RuntimeException("Invalid User Code.");
        }
        if (saleDTO.getSaleInventories().isEmpty()) {
            throw new RuntimeException("Empty SaleInventories.");
        }
        if (saleDTO.getTotalPrice()<0) {
            throw new RuntimeException("Invalid Total Price.");
        }
        logger.info("Sale validated.");
    }
}