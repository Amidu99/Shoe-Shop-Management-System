package lk.ijse.HelloShoesBE.repo;

import lk.ijse.HelloShoesBE.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface CustomerRepo extends JpaRepository<Customer, String> {
    boolean existsByCustomerCode(String customerCode);
    Customer getCustomerByCustomerCode(String customerCode);
    @Query("SELECT MAX (c.customerCode) FROM Customer c")
    String getLastCustomerCode();
    @Query("SELECT c FROM Customer c WHERE FUNCTION('MONTH', c.dob) = :month AND FUNCTION('DAY', c.dob) = :day")
    List<Customer> findByMonthAndDay(@Param("month") int month, @Param("day") int day);
}