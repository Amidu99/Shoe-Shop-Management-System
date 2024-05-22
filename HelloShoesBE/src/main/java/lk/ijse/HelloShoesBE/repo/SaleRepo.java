package lk.ijse.HelloShoesBE.repo;

import lk.ijse.HelloShoesBE.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleRepo extends JpaRepository<Sale, String> {
}