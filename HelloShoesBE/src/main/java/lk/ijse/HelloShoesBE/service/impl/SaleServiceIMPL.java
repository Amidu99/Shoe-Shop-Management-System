package lk.ijse.HelloShoesBE.service.impl;

import jakarta.transaction.Transactional;
import lk.ijse.HelloShoesBE.dto.MostSoldItemDTO;
import lk.ijse.HelloShoesBE.dto.SaleDTO;
import lk.ijse.HelloShoesBE.dto.SaleInventoriesDTO;
import lk.ijse.HelloShoesBE.entity.*;
import lk.ijse.HelloShoesBE.repo.*;
import lk.ijse.HelloShoesBE.service.SaleService;
import lk.ijse.HelloShoesBE.util.Mapping;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class SaleServiceIMPL implements SaleService {
    private final InventoryRepo inventoryRepo;
    private final CustomerRepo customerRepo;
    private final DetailRepo detailRepo;
    private final UserRepo userRepo;
    private final SaleRepo saleRepo;
    private final Mapping mapping;

    @Override
    public SaleDTO saveSale(SaleDTO saleDTO) {
        Customer customer = customerRepo.getCustomerByCustomerCode(saleDTO.getCustomerCode());
        User user = userRepo.getReferenceById(saleDTO.getUserCode());
        Date dateToDetail = new Date(saleDTO.getDate().getTime());

        Set<SaleInventories> saleInventories = new HashSet<>();
        for (SaleInventoriesDTO saleInventoriesDTO : saleDTO.getSaleInventories()) {
            SaleInventories saleInventory = new SaleInventories();
            saleInventory.setOrderDetailCode(UUID.randomUUID().toString());
            Inventory inventory = inventoryRepo.getReferenceById(saleInventoriesDTO.getItemCode());
            saleInventory.setInventory(inventory);
            saleInventory.setOrderCode(saleInventoriesDTO.getOrderCode());
            saleInventory.setDate(dateToDetail);
            saleInventory.setSize(saleInventoriesDTO.getSize());
            saleInventory.setQty(saleInventoriesDTO.getQty());
            saleInventories.add(saleInventory);
        }

        Sale sale = mapping.toSaleEntity(saleDTO);
        if(customer!=null){ sale.setCustomerName(customer.getCustomerName()); }
        sale.setUser(user);
        sale.setCustomer(customer);
        sale.setCashierName(user.getEmployee().getEmployeeName());
        sale.setSaleInventories(saleInventories);
        Sale savedSale = saleRepo.save(sale);
        detailRepo.saveAll(saleInventories);
        return mapping.toSaleDTO(savedSale);
    }

    @Override
    public boolean existsByOrderCode(String orderCode) {
        return saleRepo.existsByOrderCode(orderCode);
    }

    @Override
    public Optional<SaleDTO> getOrderByOrderCode(String orderCode) {
        Optional<SaleDTO> saleDTO = saleRepo.findSaleInfoByOrderCode(orderCode);
        if (saleDTO.isPresent()) {
            Set<SaleInventoriesDTO> orderDetails = detailRepo.findAllByOrderCode(orderCode);
            for (SaleInventoriesDTO detail : orderDetails) {
                Inventory inventory = inventoryRepo.getReferenceById(detail.getItemCode());
                detail.setItemDesc(inventory.getItemDesc());
                detail.setItemPrice(inventory.getSellPrice());
                System.out.println(detail);
            }
            saleDTO.get().setSaleInventories(orderDetails);
        }
        return saleDTO;
    }

    @Override
    public List<SaleDTO> getAllSales() {
        List<SaleDTO> sales = saleRepo.findAllSales();
        List<String> orderCodes = sales.stream()
                .map(SaleDTO::getOrderCode)
                .collect(Collectors.toList());
        List<SaleInventoriesDTO> inventories = detailRepo.findAllByOrderCodes(orderCodes);

        Map<String, Set<SaleInventoriesDTO>> inventoriesByOrderCode = inventories.stream()
                .collect(Collectors.groupingBy(SaleInventoriesDTO::getOrderCode, Collectors.toSet()));
        // Extract all item codes
        Set<String> itemCodes = inventories.stream()
                .map(SaleInventoriesDTO::getItemCode)
                .collect(Collectors.toSet());
        // Fetch all inventory details for these item codes
        List<Inventory> inventoryList = inventoryRepo.findAllById(itemCodes);
        Map<String, Inventory> inventoryMap = inventoryList.stream()
                .collect(Collectors.toMap(Inventory::getItemCode, inventory -> inventory));
        // Set item description and price for each SaleInventoriesDTO
        for (SaleInventoriesDTO detail : inventories) {
            Inventory inventory = inventoryMap.get(detail.getItemCode());
            if (inventory != null) {
                detail.setItemDesc(inventory.getItemDesc());
                detail.setItemPrice(inventory.getSellPrice());
            }
        }
        for (SaleDTO sale : sales) {
            sale.setSaleInventories(inventoriesByOrderCode.getOrDefault(sale.getOrderCode(), Set.of()));
        }
        return sales;
    }

    @Override
    public String getLastOrderCode() {
        return saleRepo.getLastOrderCode();
    }

    @Override
    public void updateSale(SaleDTO saleDTO) {
        Optional<SaleDTO> existingSaleDTO = saleRepo.findSaleInfoByOrderCode(saleDTO.getOrderCode());

        if (existingSaleDTO.isPresent()) {
            detailRepo.removeAllByOrderCode(saleDTO.getOrderCode());
            Customer customer = customerRepo.getCustomerByCustomerCode(saleDTO.getCustomerCode());
            User user = userRepo.getReferenceById(saleDTO.getUserCode());
            Date dateToDetail = new Date(saleDTO.getDate().getTime());

            Set<SaleInventories> saleInventories = new HashSet<>();
            for (SaleInventoriesDTO saleInventoriesDTO : saleDTO.getSaleInventories()) {
                SaleInventories saleInventory = new SaleInventories();
                saleInventory.setOrderDetailCode(UUID.randomUUID().toString());
                Inventory inventory = inventoryRepo.getReferenceById(saleInventoriesDTO.getItemCode());
                saleInventory.setInventory(inventory);
                saleInventory.setOrderCode(saleInventoriesDTO.getOrderCode());
                saleInventory.setDate(dateToDetail);
                saleInventory.setSize(saleInventoriesDTO.getSize());
                saleInventory.setQty(saleInventoriesDTO.getQty());
                saleInventories.add(saleInventory);
            }

            Sale sale = mapping.toSaleEntity(saleDTO);
            if(customer!=null){ sale.setCustomerName(customer.getCustomerName()); }
            sale.setUser(user);
            sale.setCustomer(customer);
            sale.setCashierName(user.getEmployee().getEmployeeName());
            sale.setSaleInventories(saleInventories);
            saleRepo.save(sale);
            detailRepo.saveAll(saleInventories);
        }
    }

    @Override
    public int getSaleCountByDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        // Set time to start of the day
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date startOfDay = new Date(calendar.getTimeInMillis());
        // Set time to end of the day
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        Date endOfDay = new Date(calendar.getTimeInMillis());
        return saleRepo.getSaleCountByDate(startOfDay, endOfDay);
    }

    @Override
    public Optional<MostSoldItemDTO> getMostSoldItemByDate(Date day) {
        List<MostSoldItemDTO> items = detailRepo.getMostSoldItemByDate(day);
        if (items.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(items.get(0));
    }
}