package lk.ijse.HelloShoesBE.service.impl;

import jakarta.transaction.Transactional;
import lk.ijse.HelloShoesBE.dto.SupplierDTO;
import lk.ijse.HelloShoesBE.repo.SupplierRepo;
import lk.ijse.HelloShoesBE.service.SupplierService;
import lk.ijse.HelloShoesBE.util.Mapping;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class SupplierServiceIMPL implements SupplierService {
    private final SupplierRepo repo;
    private final Mapping mapping;

    @Override
    public SupplierDTO saveSupplier(SupplierDTO supplierDTO) {
        return mapping.toSupplierDTO(repo.save(mapping.toSupplierEntity(supplierDTO)));
    }
}