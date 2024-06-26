package lk.ijse.HelloShoesBE.service;

import lk.ijse.HelloShoesBE.dto.SupplierInventoriesDTO;
import java.util.List;

public interface StockService {
    boolean existsByStockCode(String stockCode);
    SupplierInventoriesDTO saveStock(SupplierInventoriesDTO supplierInventoriesDTO);
    boolean existsByItemCodeAndSize(String itemCode, int size);
    boolean existsByStockCodeItemCodeSize(String stockCode, String itemCode, int size);
    SupplierInventoriesDTO getStockByStockCode(String stockCode);
    List<SupplierInventoriesDTO> getAllStocks();
    void updateStock(SupplierInventoriesDTO supplierInventoriesDTO);
    void deleteStock(String stockCode);
    String getLastStockCode();
    SupplierInventoriesDTO getExistsByItemCodeAndSize(String itemCode, int size);
    List<SupplierInventoriesDTO> getLowStocks();
}