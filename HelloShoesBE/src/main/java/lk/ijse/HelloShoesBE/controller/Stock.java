package lk.ijse.HelloShoesBE.controller;

import lk.ijse.HelloShoesBE.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/stock")
@RequiredArgsConstructor
public class Stock {
    private final StockService stockService;

    @GetMapping("/health")
    public String healthTest(){
        System.out.println("Stock Health Test Passed.");
        return "Stock Health Test Passed.";
    }
}