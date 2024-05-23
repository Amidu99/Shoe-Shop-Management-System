package lk.ijse.HelloShoesBE.service.impl;

import jakarta.transaction.Transactional;
import lk.ijse.HelloShoesBE.dto.EmployeeDTO;
import lk.ijse.HelloShoesBE.entity.Employee;
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
    public EmployeeDTO getEmployeeByEmail(String email) {
        return mapping.toEmployeeDTO(repo.getEmployeeByEmail(email));
    }

    @Override
    public List<EmployeeDTO> getAllEmployees() {
        return mapping.toEmployeeDTOList(repo.findAll());
    }

    @Override
    public void updateEmployee(EmployeeDTO employeeDTO) {
        Employee existingEmployee = repo.getEmployeeByEmployeeCode(employeeDTO.getEmployeeCode());
        existingEmployee.setEmployeeName(employeeDTO.getEmployeeName());
        existingEmployee.setEmployeePic(employeeDTO.getEmployeePic());
        existingEmployee.setGender(employeeDTO.getGender());
        existingEmployee.setStatus(employeeDTO.getStatus());
        existingEmployee.setDesignation(employeeDTO.getDesignation());
        existingEmployee.setRole(employeeDTO.getRole());
        existingEmployee.setDob(employeeDTO.getDob());
        existingEmployee.setJoinDate(employeeDTO.getJoinDate());
        existingEmployee.setBranch(employeeDTO.getBranch());
        existingEmployee.setAddLine1(employeeDTO.getAddLine1());
        existingEmployee.setAddLine2(employeeDTO.getAddLine2());
        existingEmployee.setAddLine3(employeeDTO.getAddLine3());
        existingEmployee.setAddLine4(employeeDTO.getAddLine4());
        existingEmployee.setAddLine5(employeeDTO.getAddLine5());
        existingEmployee.setContactNo(employeeDTO.getContactNo());
        existingEmployee.setEmail(employeeDTO.getEmail());
        existingEmployee.setGuardian(employeeDTO.getGuardian());
        existingEmployee.setGuardianContactNo(employeeDTO.getGuardianContactNo());
        repo.save(existingEmployee);
    }

    @Override
    public void deleteEmployee(String employeeCode) {
        repo.deleteById(employeeCode);
    }

    @Override
    public String getLastEmployeeCode() {
        return repo.getLastEmployeeCode();
    }

    @Override
    public boolean existsByEmail(String email) {
        return repo.existsByEmail(email);
    }
}