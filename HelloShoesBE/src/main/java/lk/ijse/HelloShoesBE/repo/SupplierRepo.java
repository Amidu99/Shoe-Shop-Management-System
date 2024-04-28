package lk.ijse.HelloShoesBE.repo;

import lk.ijse.HelloShoesBE.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SupplierRepo extends JpaRepository<Supplier, String> {
    boolean existsBySupplierCode(String supplierCode);
    Supplier getSupplierBySupplierCode(String supplierCode);
    @Query("SELECT MAX (s.supplierCode) FROM Supplier s")
    String getLastSupplierCode();
}