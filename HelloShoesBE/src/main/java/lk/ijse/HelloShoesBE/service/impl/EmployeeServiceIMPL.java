package lk.ijse.HelloShoesBE.service.impl;

import jakarta.transaction.Transactional;
import lk.ijse.HelloShoesBE.repo.EmployeeRepo;
import lk.ijse.HelloShoesBE.service.EmployeeService;
import lk.ijse.HelloShoesBE.util.Mapping;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class EmployeeServiceIMPL implements EmployeeService {
    private final EmployeeRepo repo;
    private final Mapping mapping;

}