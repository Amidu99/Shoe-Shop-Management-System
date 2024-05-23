package lk.ijse.HelloShoesBE.service;

import lk.ijse.HelloShoesBE.dto.SaleDTO;
import java.util.List;
import java.util.Optional;

public interface SaleService {
    SaleDTO saveSale(SaleDTO saleDTO);
    boolean existsByOrderCode(String oderCode);
    Optional<SaleDTO> getOrderByOrderCode(String oderCode);
    List<SaleDTO> getAllSales();
    String getLastOrderCode();
    void updateSale(SaleDTO saleDTO);
}