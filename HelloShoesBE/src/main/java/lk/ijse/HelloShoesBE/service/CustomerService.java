package lk.ijse.HelloShoesBE.service;

import lk.ijse.HelloShoesBE.dto.CustomerDTO;
import java.sql.Timestamp;
import java.util.List;

public interface CustomerService {
    CustomerDTO saveCustomer(CustomerDTO customerDTO);
    void deleteCustomer(String customerCode);
    CustomerDTO getCustomerByCustomerCode(String customerCode);
    List<CustomerDTO> getAllCustomers();
    void updateCustomer(CustomerDTO customerDTO);
    Boolean existsByCustomerCode(String customerCode);
    String getLastCustomerCode();
    void updateCustomerPoints(String customerCode, double totalPoints, Timestamp rpDateTime);
}