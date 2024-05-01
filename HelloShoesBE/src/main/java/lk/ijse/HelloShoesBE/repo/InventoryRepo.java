package lk.ijse.HelloShoesBE.repo;

import lk.ijse.HelloShoesBE.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepo extends JpaRepository<Inventory, String> {
}