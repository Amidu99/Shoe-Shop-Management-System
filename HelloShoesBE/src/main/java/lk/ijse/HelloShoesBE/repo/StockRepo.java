package lk.ijse.HelloShoesBE.repo;

import lk.ijse.HelloShoesBE.entity.SupplierInventories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StockRepo extends JpaRepository<SupplierInventories, String> {
    boolean existsByStockCode(String stockCode);
    @Query("SELECT si FROM SupplierInventories si WHERE si.inventory.itemCode = :itemCode AND si.size = :size")
    SupplierInventories existsByItemCodeAndSize(@Param("itemCode") String itemCode, @Param("size") int size);
    SupplierInventories getStockByStockCode(String stockCode);
    @Query("SELECT MAX (s.stockCode) FROM SupplierInventories s")
    String getLastStockCode();
}