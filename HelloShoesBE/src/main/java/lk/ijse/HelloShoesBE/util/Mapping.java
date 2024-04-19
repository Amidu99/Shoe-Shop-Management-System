package lk.ijse.HelloShoesBE.util;

import lk.ijse.HelloShoesBE.dto.CustomerDTO;
import lk.ijse.HelloShoesBE.entity.Customer;
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
}