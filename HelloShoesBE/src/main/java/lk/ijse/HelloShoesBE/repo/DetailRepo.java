package lk.ijse.HelloShoesBE.repo;

import lk.ijse.HelloShoesBE.dto.MostSoldItemDTO;
import lk.ijse.HelloShoesBE.dto.SaleInventoriesDTO;
import lk.ijse.HelloShoesBE.entity.SaleInventories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import java.sql.Date;
import java.util.List;
import java.util.Set;

public interface DetailRepo extends JpaRepository<SaleInventories, String> {
    @Query("SELECT NEW lk.ijse.HelloShoesBE.dto.SaleInventoriesDTO(si.orderDetailCode, si.inventory.itemCode, si.orderCode, si.date, si.size, si.qty) FROM SaleInventories si WHERE si.orderCode = :orderCode")
    Set<SaleInventoriesDTO> findAllByOrderCode(String orderCode);
    @Query("SELECT NEW lk.ijse.HelloShoesBE.dto.SaleInventoriesDTO(si.orderDetailCode, si.inventory.itemCode, si.orderCode, si.date, si.size, si.qty) FROM SaleInventories si WHERE si.orderCode IN :orderCodes")
    List<SaleInventoriesDTO> findAllByOrderCodes(List<String> orderCodes);
    @Modifying
    @Transactional
    @Query("DELETE FROM SaleInventories si WHERE si.orderCode = :orderCode")
    void removeAllByOrderCode(String orderCode);
    @Query("SELECT NEW lk.ijse.HelloShoesBE.dto.MostSoldItemDTO(si.inventory.itemCode, SUM(si.qty)) " +
            "FROM SaleInventories si " +
            "WHERE si.date = :date " +
            "GROUP BY si.inventory.itemCode " +
            "ORDER BY SUM(si.qty) DESC")
    List<MostSoldItemDTO> getMostSoldItemByDate(Date date);
}