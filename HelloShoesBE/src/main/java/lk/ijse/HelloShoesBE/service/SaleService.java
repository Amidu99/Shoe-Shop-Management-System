package lk.ijse.HelloShoesBE.service;

import lk.ijse.HelloShoesBE.dto.MostSoldItemDTO;
import lk.ijse.HelloShoesBE.dto.SaleDTO;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

public interface SaleService {
    SaleDTO saveSale(SaleDTO saleDTO);
    boolean existsByOrderCode(String orderCode);
    Optional<SaleDTO> getOrderByOrderCode(String orderCode);
    List<SaleDTO> getAllSales();
    String getLastOrderCode();
    void updateSale(SaleDTO saleDTO);
    int getSaleCountByDate(Date date);
    Optional<MostSoldItemDTO> getMostSoldItemByDate(Date day);
}