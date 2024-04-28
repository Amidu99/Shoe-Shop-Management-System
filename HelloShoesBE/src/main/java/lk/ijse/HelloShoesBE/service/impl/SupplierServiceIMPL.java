package lk.ijse.HelloShoesBE.service.impl;

import jakarta.transaction.Transactional;
import lk.ijse.HelloShoesBE.dto.SupplierDTO;
import lk.ijse.HelloShoesBE.entity.Supplier;
import lk.ijse.HelloShoesBE.repo.SupplierRepo;
import lk.ijse.HelloShoesBE.service.SupplierService;
import lk.ijse.HelloShoesBE.util.Mapping;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

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

    @Override
    public boolean existsBySupplierCode(String supplierCode) {
        return repo.existsBySupplierCode(supplierCode);
    }

    @Override
    public SupplierDTO getSupplierBySupplierCode(String supplierCode) {
        return mapping.toSupplierDTO(repo.getSupplierBySupplierCode(supplierCode));
    }

    @Override
    public List<SupplierDTO> getAllSuppliers() {
        return mapping.toSupplierDTOList(repo.findAll());
    }

    @Override
    public void updateSupplier(SupplierDTO supplierDTO) {
        Supplier existingSupplier = repo.getSupplierBySupplierCode(supplierDTO.getSupplierCode());
        existingSupplier.setSupplierName(supplierDTO.getSupplierName());
        existingSupplier.setCategory(supplierDTO.getCategory());
        existingSupplier.setAddLine1(supplierDTO.getAddLine1());
        existingSupplier.setAddLine2(supplierDTO.getAddLine2());
        existingSupplier.setAddLine3(supplierDTO.getAddLine3());
        existingSupplier.setAddLine4(supplierDTO.getAddLine4());
        existingSupplier.setAddLine5(supplierDTO.getAddLine5());
        existingSupplier.setAddLine6(supplierDTO.getAddLine6());
        existingSupplier.setContactNo1(supplierDTO.getContactNo1());
        existingSupplier.setContactNo2(supplierDTO.getContactNo2());
        existingSupplier.setEmail(supplierDTO.getEmail());
        repo.save(existingSupplier);
    }
}