package lk.ijse.HelloShoesBE.service;

import lk.ijse.HelloShoesBE.dto.EmployeeDTO;
import java.util.List;

public interface EmployeeService {
    EmployeeDTO saveEmployee(EmployeeDTO employeeDTO);
    boolean existsByEmployeeCode(String employeeCode);
    EmployeeDTO getEmployeeByEmployeeCode(String employeeCode);
    List<EmployeeDTO> getAllEmployees();
    void updateEmployee(EmployeeDTO employeeDTO);
    void deleteEmployee(String employeeCode);
}