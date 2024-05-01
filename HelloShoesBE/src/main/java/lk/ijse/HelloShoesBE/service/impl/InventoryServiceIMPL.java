package lk.ijse.HelloShoesBE.service.impl;

import jakarta.transaction.Transactional;
import lk.ijse.HelloShoesBE.dto.InventoryDTO;
import lk.ijse.HelloShoesBE.entity.Inventory;
import lk.ijse.HelloShoesBE.repo.InventoryRepo;
import lk.ijse.HelloShoesBE.service.InventoryService;
import lk.ijse.HelloShoesBE.util.Mapping;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class InventoryServiceIMPL implements InventoryService {
    private final InventoryRepo repo;
    private final Mapping mapping;

    @Override
    public boolean existsByItemCode(String itemCode) {
        return repo.existsByItemCode(itemCode);
    }

    @Override
    public InventoryDTO saveInventory(InventoryDTO inventoryDTO) {
        return mapping.toInventoryDTO(repo.save(mapping.toInventoryEntity(inventoryDTO)));
    }

    @Override
    public InventoryDTO getInventoryByItemCode(String itemCode) {
        return mapping.toInventoryDTO(repo.getInventoryByItemCode(itemCode));
    }

    @Override
    public List<InventoryDTO> getAllInventories() {
        return mapping.toInventoryDTOList(repo.findAll());
    }

    @Override
    public void updateInventory(InventoryDTO inventoryDTO) {
        Inventory existingInventory = repo.getInventoryByItemCode(inventoryDTO.getItemCode());
        existingInventory.setItemDesc(inventoryDTO.getItemDesc());
        existingInventory.setItemPic(inventoryDTO.getItemPic());
        existingInventory.setCategory(inventoryDTO.getCategory());
        existingInventory.setSize(inventoryDTO.getSize());
        existingInventory.setSupplierCode(inventoryDTO.getSupplierCode());
        existingInventory.setSupplierName(inventoryDTO.getSupplierName());
        existingInventory.setBuyPrice(inventoryDTO.getBuyPrice());
        existingInventory.setSellPrice(inventoryDTO.getSellPrice());
        existingInventory.setProfit(inventoryDTO.getProfit());
        existingInventory.setProfitMargin(inventoryDTO.getProfitMargin());
        existingInventory.setStatus(inventoryDTO.getStatus());
        repo.save(existingInventory);
    }

    @Override
    public void deleteInventory(String itemCode) {
        repo.deleteById(itemCode);
    }
}