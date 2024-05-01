package lk.ijse.HelloShoesBE.service.impl;

import jakarta.transaction.Transactional;
import lk.ijse.HelloShoesBE.dto.InventoryDTO;
import lk.ijse.HelloShoesBE.repo.InventoryRepo;
import lk.ijse.HelloShoesBE.service.InventoryService;
import lk.ijse.HelloShoesBE.util.Mapping;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}