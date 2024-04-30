package lk.ijse.HelloShoesBE.repo;

import lk.ijse.HelloShoesBE.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepo extends JpaRepository<Employee, String> {
}