package lk.ijse.HelloShoesBE.service.impl;

import jakarta.transaction.Transactional;
import lk.ijse.HelloShoesBE.dto.CustomerDTO;
import lk.ijse.HelloShoesBE.entity.Customer;
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
        repo.deleteById(customerCode);
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
    public void updateCustomer(CustomerDTO customerDTO) {
        Customer existingCustomer = repo.getCustomerByCustomerCode(customerDTO.getCustomerCode());
        // Update the existing customer's information
        existingCustomer.setCustomerName(customerDTO.getCustomerName());
        existingCustomer.setGender(customerDTO.getGender());
        existingCustomer.setJoinDate(customerDTO.getJoinDate());
        existingCustomer.setLevel(customerDTO.getLevel());
        existingCustomer.setTotalPoints(customerDTO.getTotalPoints());
        existingCustomer.setDob(customerDTO.getDob());
        existingCustomer.setAddLine1(customerDTO.getAddLine1());
        existingCustomer.setAddLine2(customerDTO.getAddLine2());
        existingCustomer.setAddLine3(customerDTO.getAddLine3());
        existingCustomer.setAddLine4(customerDTO.getAddLine4());
        existingCustomer.setAddLine5(customerDTO.getAddLine5());
        existingCustomer.setContactNo(customerDTO.getContactNo());
        existingCustomer.setEmail(customerDTO.getEmail());
        existingCustomer.setRpDateTime(customerDTO.getRpDateTime());
        // Save the updated customer entity back to the db
        repo.save(existingCustomer);
    }

    @Override
    public Boolean existsByCustomerCode(String customerCode) {
        return repo.existsByCustomerCode(customerCode);
    }
}