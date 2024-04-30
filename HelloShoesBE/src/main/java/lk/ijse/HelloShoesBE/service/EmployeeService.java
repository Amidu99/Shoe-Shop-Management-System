package lk.ijse.HelloShoesBE.service;

import lk.ijse.HelloShoesBE.dto.EmployeeDTO;

public interface EmployeeService {
    EmployeeDTO saveEmployee(EmployeeDTO employeeDTO);
    boolean existsByEmployeeCode(String employeeCode);
    EmployeeDTO getEmployeeByEmployeeCode(String employeeCode);
}