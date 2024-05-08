package lk.ijse.HelloShoesBE.service.impl;

import jakarta.transaction.Transactional;
import lk.ijse.HelloShoesBE.dto.SupplierInventoriesDTO;
import lk.ijse.HelloShoesBE.entity.Inventory;
import lk.ijse.HelloShoesBE.entity.Supplier;
import lk.ijse.HelloShoesBE.entity.SupplierInventories;
import lk.ijse.HelloShoesBE.repo.InventoryRepo;
import lk.ijse.HelloShoesBE.repo.StockRepo;
import lk.ijse.HelloShoesBE.repo.SupplierRepo;
import lk.ijse.HelloShoesBE.service.StockService;
import lk.ijse.HelloShoesBE.util.Mapping;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class StockServiceIMPL implements StockService {
    private final StockRepo stockRepo;
    private final InventoryRepo inventoryRepo;
    private final SupplierRepo supplierRepo;
    private final Mapping mapping;

    @Override
    public boolean existsByStockCode(String stockCode) {
        return stockRepo.existsByStockCode(stockCode);
    }

    @Override
    public SupplierInventoriesDTO saveStock(SupplierInventoriesDTO supplierInventoriesDTO) {
        Inventory inventory = inventoryRepo.getInventoryByItemCode(supplierInventoriesDTO.getItemCode());
        if (inventory == null) {
            throw new IllegalArgumentException("Inventory not found for itemCode: " + supplierInventoriesDTO.getItemCode());
        }
        Supplier supplier = supplierRepo.getSupplierBySupplierCode(supplierInventoriesDTO.getSupplierCode());
        if (supplier == null) {
            throw new IllegalArgumentException("Supplier not found for supplierCode: " + supplierInventoriesDTO.getSupplierCode());
        }
        SupplierInventories supplierInventoriesEntity = mapping.toSupplierInventoriesEntity(supplierInventoriesDTO);
        supplierInventoriesEntity.setInventory(inventory);
        supplierInventoriesEntity.setSupplier(supplier);
        SupplierInventories savedEntity = stockRepo.save(supplierInventoriesEntity);
        return mapping.toSupplierInventoriesDTO(savedEntity);
    }

    @Override
    public boolean existsByItemCodeAndSize(String itemCode, int size) {
        return stockRepo.existsByItemCodeAndSize(itemCode, size) != null;
    }

    @Override
    public SupplierInventoriesDTO getStockByStockCode(String stockCode) {
        SupplierInventories stock = stockRepo.getStockByStockCode(stockCode);
        SupplierInventoriesDTO stockDTO = mapping.toSupplierInventoriesDTO(stock);
        stockDTO.setItemCode(stock.getInventory().getItemCode());
        stockDTO.setSupplierCode(stock.getSupplier().getSupplierCode());
        return stockDTO;
    }

    @Override
    public List<SupplierInventoriesDTO> getAllStocks() {
        return mapping.toSupplierInventoriesDTOList(stockRepo.findAll());
    }

    @Override
    public void updateStock(SupplierInventoriesDTO supplierInventoriesDTO) {
        SupplierInventories existingStock = stockRepo.getStockByStockCode(supplierInventoriesDTO.getStockCode());
        existingStock.setOriginalQty(supplierInventoriesDTO.getOriginalQty());
        existingStock.setAvailableQty(supplierInventoriesDTO.getAvailableQty());
        existingStock.setStatus(supplierInventoriesDTO.getStatus());
        existingStock.setSupplier(supplierRepo.getSupplierBySupplierCode(supplierInventoriesDTO.getSupplierCode()));
        stockRepo.save(existingStock);
    }

    @Override
    public void deleteStock(String stockCode) {
        stockRepo.deleteById(stockCode);
    }

    @Override
    public String getLastStockCode() {
        return stockRepo.getLastStockCode();
    }
}