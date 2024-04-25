package lk.ijse.HelloShoesBE.service.impl;

import jakarta.transaction.Transactional;
import lk.ijse.HelloShoesBE.dto.CustomerDTO;
import lk.ijse.HelloShoesBE.repo.CustomerRepo;
import lk.ijse.HelloShoesBE.service.CustomerService;
import lk.ijse.HelloShoesBE.util.Mapping;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomerServiceIMPL implements CustomerService {
    private final CustomerRepo repo;
    private final Mapping mapping;

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        return mapping.toCustomerDTO(repo.save(mapping.toCustomerEntity(customerDTO)));
    }

    @Override
    public void deleteCustomer(String customerCode) {

    }

    @Override
    public CustomerDTO getCustomerByCustomerCode(String customerCode) {
        return mapping.toCustomerDTO(repo.getCustomerByCustomerCode(customerCode));
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        return mapping.toCustomerDTOList(repo.findAll());
    }

    @Override
    public void updateCustomer(String customerCode, CustomerDTO customerDTO) {

    }

    @Override
    public Boolean existsByCustomerCode(String customerCode) {
        return repo.existsByCustomerCode(customerCode);
    }
}