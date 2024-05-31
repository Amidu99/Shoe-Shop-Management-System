package lk.ijse.HelloShoesBE.controller;

import jakarta.annotation.security.RolesAllowed;
import lk.ijse.HelloShoesBE.dto.SupplierDTO;
import lk.ijse.HelloShoesBE.service.SupplierService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/v1/supplier")
@RequiredArgsConstructor
public class Supplier {
    final static Logger logger = LoggerFactory.getLogger(Supplier.class);
    private final SupplierService supplierService;

    @GetMapping("/health")
    @RolesAllowed({"ADMIN", "USER"})
    public String healthTest(){
        logger.info("Supplier Health Test Passed.");
        return "Supplier Health Test Passed.";
    }

    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<?> saveSupplier(@RequestBody SupplierDTO supplierDTO) {
        try {
            validateSupplier(supplierDTO);
            if (supplierService.existsBySupplierCode(supplierDTO.getSupplierCode())) {
                logger.info("Exists Supplier.");
                return ResponseEntity.badRequest().body("This supplier already exists.");
            }
            supplierService.saveSupplier(supplierDTO);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/get")
    @RolesAllowed({"ADMIN", "USER"})
    public ResponseEntity<?> getOneSupplier(@RequestHeader String supplierCode){
        boolean isExists = supplierService.existsBySupplierCode(supplierCode);
        if (!isExists){
            logger.info("Not Exists Supplier.");
            return ResponseEntity.noContent().build();
        }
        SupplierDTO supplierDTO = supplierService.getSupplierBySupplierCode(supplierCode);
        logger.info("Supplier founded: "+supplierDTO);
        return ResponseEntity.ok(supplierDTO);
    }

    @GetMapping("/getAll")
    @RolesAllowed({"ADMIN", "USER"})
    public ResponseEntity<?> getAllSuppliers(){
        List<SupplierDTO> allSuppliers = supplierService.getAllSuppliers();
        logger.info("No of all suppliers: "+allSuppliers.size());
        if (allSuppliers.size() == 0) return ResponseEntity.ok().body("No suppliers found");
        return ResponseEntity.ok().body(allSuppliers);
    }

    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<?> updateSupplier(@RequestBody SupplierDTO supplierDTO) {
        try {
            validateSupplier(supplierDTO);
            if (supplierService.existsBySupplierCode(supplierDTO.getSupplierCode())) {
                supplierService.updateSupplier(supplierDTO);
                return ResponseEntity.ok().build();
            }
            logger.info("Not Exists Supplier.");
            return ResponseEntity.badRequest().body("This supplier not exists.");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<?> deleteSupplier(@RequestHeader String supplierCode){
        boolean isExists = supplierService.existsBySupplierCode(supplierCode);
        if (!isExists){
            logger.info("Not Exists Supplier.");
            return ResponseEntity.badRequest().body("Supplier not found!");
        }
        supplierService.deleteSupplier(supplierCode);
        logger.info("Supplier deleted.");
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getNextCode")
    @RolesAllowed({"ADMIN", "USER"})
    public ResponseEntity<?> getNextSupplierCode(){
        String lastSupplierCode = supplierService.getLastSupplierCode();
        logger.info("Last SupplierCode: "+lastSupplierCode);
        if (lastSupplierCode==null) return ResponseEntity.ok("S-0001");
        int nextCode = Integer.parseInt(lastSupplierCode.replace("S-", "")) + 1;
        logger.info("Next SupplierCode: "+nextCode);
        return ResponseEntity.ok(String.format("S-%04d", nextCode));
    }

    private void validateSupplier(SupplierDTO supplierDTO) {
        if (!Pattern.compile("^[S]-\\d{4}$").matcher(supplierDTO.getSupplierCode()).matches()) {
            throw new RuntimeException("Invalid Supplier Code.");
        }
        if (!Pattern.compile("^[A-Za-z\\s]{3,}$").matcher(supplierDTO.getSupplierName()).matches()) {
            throw new RuntimeException("Invalid Supplier Name.");
        }
        if (!Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$").matcher(supplierDTO.getEmail()).matches()) {
            throw new RuntimeException("Invalid Supplier Email.");
        }
        logger.info("Supplier validated.");
    }
}