package lk.ijse.HelloShoesBE.service.impl;

import jakarta.transaction.Transactional;
import lk.ijse.HelloShoesBE.dto.CustomerDTO;
import lk.ijse.HelloShoesBE.entity.Customer;
import lk.ijse.HelloShoesBE.entity.Level;
import lk.ijse.HelloShoesBE.repo.CustomerRepo;
import lk.ijse.HelloShoesBE.service.CustomerService;
import lk.ijse.HelloShoesBE.util.Mapping;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.util.List;
import static lk.ijse.HelloShoesBE.entity.Level.*;

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
        // Save the updated customer entity back to the db
        repo.save(existingCustomer);
    }

    @Override
    public Boolean existsByCustomerCode(String customerCode) {
        return repo.existsByCustomerCode(customerCode);
    }

    @Override
    public String getLastCustomerCode() {
        return repo.getLastCustomerCode();
    }

    @Override
    public void updateCustomerPoints(String customerCode, double totalPoints, Timestamp rpDateTime) {
        int intTotalPoints = (int) Math.round(totalPoints);
        Customer existingCustomer = repo.getCustomerByCustomerCode(customerCode);
        int oldPoints = existingCustomer.getTotalPoints();
        int newPoints = (oldPoints+intTotalPoints);
        Level level = NEW;
        if (newPoints > 49 && newPoints < 100) { level = BRONZE; }
        if (newPoints > 99 && newPoints < 200) { level = SILVER; }
        if (newPoints > 200) { level = GOLD; }
        existingCustomer.setLevel(level);
        existingCustomer.setTotalPoints(newPoints);
        existingCustomer.setRpDateTime(rpDateTime);
        repo.save(existingCustomer);
    }
}