package lk.ijse.HelloShoesBE.repo;

import lk.ijse.HelloShoesBE.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface InventoryRepo extends JpaRepository<Inventory, String> {
    boolean existsByItemCode(String itemCode);
    Inventory getInventoryByItemCode(String itemCode);
    @Query(value = "SELECT MAX(CAST(SUBSTRING(itemcode, 6) AS UNSIGNED)) FROM inventory WHERE itemcode REGEXP '^[A-Za-z]{4}-[0-9]{4}$'", nativeQuery = true)
    String getLastItemCode();
}