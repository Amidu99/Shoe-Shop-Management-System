package lk.ijse.HelloShoesBE.service;

import lk.ijse.HelloShoesBE.dto.InventoryDTO;
import java.util.List;

public interface InventoryService {
    boolean existsByItemCode(String itemCode);
    InventoryDTO saveInventory(InventoryDTO inventoryDTO);
    InventoryDTO getInventoryByItemCode(String itemCode);
    List<InventoryDTO> getAllInventories();
    void updateInventory(InventoryDTO inventoryDTO);
    void deleteInventory(String itemCode);
    String getLastItemCode();
}