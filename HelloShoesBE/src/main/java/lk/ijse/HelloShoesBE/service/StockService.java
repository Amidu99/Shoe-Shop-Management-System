package lk.ijse.HelloShoesBE.service;

import lk.ijse.HelloShoesBE.dto.SupplierInventoriesDTO;

public interface StockService {
    boolean existsByStockCode(String stockCode);
    SupplierInventoriesDTO saveStock(SupplierInventoriesDTO supplierInventoriesDTO);
}