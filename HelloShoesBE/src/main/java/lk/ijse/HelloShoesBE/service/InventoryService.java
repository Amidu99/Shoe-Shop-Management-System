package lk.ijse.HelloShoesBE.service;

import lk.ijse.HelloShoesBE.dto.InventoryDTO;

public interface InventoryService {
    boolean existsByItemCode(String itemCode);
    InventoryDTO saveInventory(InventoryDTO inventoryDTO);
}