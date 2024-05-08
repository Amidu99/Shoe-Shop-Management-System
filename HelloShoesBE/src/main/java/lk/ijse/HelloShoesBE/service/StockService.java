package lk.ijse.HelloShoesBE.service;

import lk.ijse.HelloShoesBE.dto.SupplierInventoriesDTO;
import java.util.List;

public interface StockService {
    boolean existsByStockCode(String stockCode);
    SupplierInventoriesDTO saveStock(SupplierInventoriesDTO supplierInventoriesDTO);
    boolean existsByItemCodeAndSize(String itemCode, int size);
    SupplierInventoriesDTO getStockByStockCode(String stockCode);
    List<SupplierInventoriesDTO> getAllStocks();
}