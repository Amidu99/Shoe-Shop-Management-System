package lk.ijse.HelloShoesBE.service;

import lk.ijse.HelloShoesBE.dto.CustomerDTO;
import java.util.List;

public interface CustomerService {
    CustomerDTO saveCustomer(CustomerDTO customerDTO);
    void deleteCustomer(String customerCode);
    CustomerDTO getSelectedCustomer(String customerCode);
    List<CustomerDTO> getAllCustomers();
    void updateCustomer(String customerCode, CustomerDTO customerDTO);
}