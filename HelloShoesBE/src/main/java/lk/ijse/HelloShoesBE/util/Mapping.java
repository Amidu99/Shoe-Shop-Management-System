package lk.ijse.HelloShoesBE.util;

import lk.ijse.HelloShoesBE.dto.CustomerDTO;
import lk.ijse.HelloShoesBE.dto.EmployeeDTO;
import lk.ijse.HelloShoesBE.dto.SupplierDTO;
import lk.ijse.HelloShoesBE.entity.Customer;
import lk.ijse.HelloShoesBE.entity.Employee;
import lk.ijse.HelloShoesBE.entity.Supplier;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Mapping {
    private final ModelMapper mapper;

    // Customer Mapping
    public CustomerDTO toCustomerDTO(Customer customer) {
       return  mapper.map(customer, CustomerDTO.class);
    }
    public Customer toCustomerEntity(CustomerDTO customerDTO) {
        return  mapper.map(customerDTO, Customer.class);
    }
    public List<CustomerDTO> toCustomerDTOList(List<Customer> customers) {
       return mapper.map(customers, List.class);
    }

    // Supplier Mapping
    public SupplierDTO toSupplierDTO(Supplier supplier) {
        return  mapper.map(supplier, SupplierDTO.class);
    }
    public Supplier toSupplierEntity(SupplierDTO supplierDTO) {
        return  mapper.map(supplierDTO, Supplier.class);
    }
    public List<SupplierDTO> toSupplierDTOList(List<Supplier> suppliers) {
        return mapper.map(suppliers, List.class);
    }

    // Employee Mapping
    public EmployeeDTO toEmployeeDTO(Employee employee) {
        return  mapper.map(employee, EmployeeDTO.class);
    }
    public Employee toEmployeeEntity(EmployeeDTO employeeDTO) {
        return  mapper.map(employeeDTO, Employee.class);
    }
    public List<EmployeeDTO> toEmployeeDTOList(List<Employee> employees) {
        return mapper.map(employees, List.class);
    }
}