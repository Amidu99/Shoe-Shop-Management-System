package lk.ijse.HelloShoesBE.repo;

import lk.ijse.HelloShoesBE.entity.SupplierInventories;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepo extends JpaRepository<SupplierInventories, String> {
    boolean existsByStockCode(String stockCode);
}