package lk.ijse.HelloShoesBE.service.impl;

import jakarta.transaction.Transactional;
import lk.ijse.HelloShoesBE.repo.StockRepo;
import lk.ijse.HelloShoesBE.service.StockService;
import lk.ijse.HelloShoesBE.util.Mapping;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class StockServiceIMPL implements StockService {
    private final StockRepo repo;
    private final Mapping mapping;

}