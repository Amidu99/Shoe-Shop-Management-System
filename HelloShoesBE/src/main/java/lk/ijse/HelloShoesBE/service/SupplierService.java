package lk.ijse.HelloShoesBE.service;

import lk.ijse.HelloShoesBE.dto.SupplierDTO;
import java.util.List;

public interface SupplierService {
    SupplierDTO saveSupplier(SupplierDTO supplierDTO);
    boolean existsBySupplierCode(String supplierCode);
    SupplierDTO getSupplierBySupplierCode(String supplierCode);
    List<SupplierDTO> getAllSuppliers();
    void updateSupplier(SupplierDTO supplierDTO);
}