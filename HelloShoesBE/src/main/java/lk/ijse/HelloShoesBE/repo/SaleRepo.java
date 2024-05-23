package lk.ijse.HelloShoesBE.repo;

import lk.ijse.HelloShoesBE.dto.SaleDTO;
import lk.ijse.HelloShoesBE.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface SaleRepo extends JpaRepository<Sale, String> {
    boolean existsByOrderCode(String oderCode);
    @Query("SELECT NEW lk.ijse.HelloShoesBE.dto.SaleDTO(s.orderCode, s.customerName, s.totalPrice, s.date, s.payMethod, s.addedPoints, s.cashierName, s.user.userCode, s.customer.customerCode) FROM Sale s WHERE s.orderCode = :orderCode")
    Optional<SaleDTO> findSaleInfoByOrderCode(String orderCode);
    @Query("SELECT NEW lk.ijse.HelloShoesBE.dto.SaleDTO(s.orderCode, s.customerName, s.totalPrice, s.date, s.payMethod, s.addedPoints, s.cashierName, s.user.userCode, s.customer.customerCode) FROM Sale s")
    List<SaleDTO> findAllSales();
    @Query("SELECT MAX (s.orderCode) FROM Sale s")
    String getLastOrderCode();
}