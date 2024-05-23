package lk.ijse.HelloShoesBE.controller;

import jakarta.annotation.security.RolesAllowed;
import lk.ijse.HelloShoesBE.dto.SaleDTO;
import lk.ijse.HelloShoesBE.service.SaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/v1/sale")
@RequiredArgsConstructor
public class Sale {
    private final SaleService saleService;

    @GetMapping("/health")
    @RolesAllowed({"ADMIN", "USER"})
    public String healthTest(){
        System.out.println("Sale Health Test Passed.");
        return "Sale Health Test Passed.";
    }

    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<?> saveSale(@RequestBody SaleDTO saleDTO) {
        try {
            validateSale(saleDTO);
            if (saleService.existsByOrderCode(saleDTO.getOrderCode())) {
                System.out.println("Exists Order.");
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
    public ResponseEntity<?> getOneSale(@RequestHeader String oderCode){
        boolean isExists = saleService.existsByOrderCode(oderCode);
        if (!isExists){
            System.out.println("Not Exists Order.");
            return ResponseEntity.noContent().build();
        }
        Optional<SaleDTO> saleDTO = saleService.getOrderByOrderCode(oderCode);
        System.out.println("Order founded: "+saleDTO);
        return ResponseEntity.ok(saleDTO);
    }

    @GetMapping("/getAll")
    @RolesAllowed({"ADMIN", "USER"})
    public ResponseEntity<?> getAllSales(){
        List<SaleDTO> allSales = saleService.getAllSales();
        System.out.println("No of all sales: "+allSales.size());
        if (allSales.size() == 0) return ResponseEntity.ok().body("No sales found");
        return ResponseEntity.ok().body(allSales);
    }

    @GetMapping("/getNextCode")
    @RolesAllowed({"ADMIN", "USER"})
    public ResponseEntity<?> getNextOrderCode(){
        String lastOrderCode = saleService.getLastOrderCode();
        System.out.println("Last OrderCode: "+lastOrderCode);
        if (lastOrderCode==null) return ResponseEntity.ok("O-0001");
        int nextCode = Integer.parseInt(lastOrderCode.replace("O-", "")) + 1;
        System.out.println("Next OrderCode: "+nextCode);
        return ResponseEntity.ok(String.format("O-%04d", nextCode));
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
            System.out.println("Not Exists Order.");
            return ResponseEntity.badRequest().body("This order is not exists.");
        } catch (Exception e) {
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
        System.out.println("Sale validated.");
    }
}