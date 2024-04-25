package lk.ijse.HelloShoesBE.repo;

import lk.ijse.HelloShoesBE.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepo extends JpaRepository<Customer, String> {
    boolean existsByCustomerCode(String customerCode);
}