package lk.ijse.HelloShoesBE.repo;

import lk.ijse.HelloShoesBE.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CustomerRepo extends JpaRepository<Customer, String> {
    boolean existsByCustomerCode(String customerCode);
    Customer getCustomerByCustomerCode(String customerCode);
    @Query("SELECT MAX (c.customerCode) FROM Customer c")
    String getLastCustomerCode();
}