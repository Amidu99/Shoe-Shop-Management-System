package lk.ijse.HelloShoesBE.util;

import lk.ijse.HelloShoesBE.dto.*;
import lk.ijse.HelloShoesBE.entity.*;
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

    // Inventory Mapping
    public InventoryDTO toInventoryDTO(Inventory inventory) {
        return  mapper.map(inventory, InventoryDTO.class);
    }
    public Inventory toInventoryEntity(InventoryDTO inventoryDTO) {
        return  mapper.map(inventoryDTO, Inventory.class);
    }
    public List<InventoryDTO> toInventoryDTOList(List<Inventory> inventories) {
        return mapper.map(inventories, List.class);
    }

    // SupplierInventories Mapping
    public SupplierInventoriesDTO toSupplierInventoriesDTO(SupplierInventories supplierInventories) {
        return  mapper.map(supplierInventories, SupplierInventoriesDTO.class);
    }
    public SupplierInventories toSupplierInventoriesEntity(SupplierInventoriesDTO supplierInventoriesDTO) {
        return  mapper.map(supplierInventoriesDTO, SupplierInventories.class);
    }
    public List<SupplierInventoriesDTO> toSupplierInventoriesDTOList(List<SupplierInventories> supplierInventories) {
        return mapper.map(supplierInventories, List.class);
    }

    // User Mapping
    public UserDTO toUserDTO(User user) {
        return  mapper.map(user, UserDTO.class);
    }
    public User toUserEntity(UserDTO userDTO) {
        return  mapper.map(userDTO, User.class);
    }
    public List<UserDTO> toUserDTOList(List<User> users) {
        return mapper.map(users, List.class);
    }
}