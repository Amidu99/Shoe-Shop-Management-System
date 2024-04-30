package lk.ijse.HelloShoesBE.service.impl;

import jakarta.transaction.Transactional;
import lk.ijse.HelloShoesBE.dto.EmployeeDTO;
import lk.ijse.HelloShoesBE.repo.EmployeeRepo;
import lk.ijse.HelloShoesBE.service.EmployeeService;
import lk.ijse.HelloShoesBE.util.Mapping;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class EmployeeServiceIMPL implements EmployeeService {
    private final EmployeeRepo repo;
    private final Mapping mapping;

    @Override
    public EmployeeDTO saveEmployee(EmployeeDTO employeeDTO) {
        return mapping.toEmployeeDTO(repo.save(mapping.toEmployeeEntity(employeeDTO)));
    }

    @Override
    public boolean existsByEmployeeCode(String employeeCode) {
        return repo.existsByEmployeeCode(employeeCode);
    }

    @Override
    public EmployeeDTO getEmployeeByEmployeeCode(String employeeCode) {
        return mapping.toEmployeeDTO(repo.getEmployeeByEmployeeCode(employeeCode));
    }

    @Override
    public List<EmployeeDTO> getAllEmployees() {
        return mapping.toEmployeeDTOList(repo.findAll());
    }
}