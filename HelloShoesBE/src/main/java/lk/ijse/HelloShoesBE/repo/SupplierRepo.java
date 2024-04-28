package lk.ijse.HelloShoesBE.repo;

import lk.ijse.HelloShoesBE.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepo extends JpaRepository<Supplier, String> {
    boolean existsBySupplierCode(String supplierCode);
    Supplier getSupplierBySupplierCode(String supplierCode);
}