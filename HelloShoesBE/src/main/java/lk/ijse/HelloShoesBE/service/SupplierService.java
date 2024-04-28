package lk.ijse.HelloShoesBE.service;

import lk.ijse.HelloShoesBE.dto.SupplierDTO;

public interface SupplierService {
    SupplierDTO saveSupplier(SupplierDTO supplierDTO);
    boolean existsBySupplierCode(String supplierCode);
    SupplierDTO getSupplierBySupplierCode(String supplierCode);
}