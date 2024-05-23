package lk.ijse.HelloShoesBE.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SaleDTO implements SuperDTO {
    private String orderCode;
    private String customerName;
    private double totalPrice;
    private Timestamp date;
    private String payMethod;
    private double addedPoints;
    private String cashierName;
    private String userCode;
    private String customerCode;
    private Set<SaleInventoriesDTO> saleInventories = new HashSet<>();

    public SaleDTO (String orderCode, String customerName, double totalPrice, Timestamp date,
                    String payMethod, double addedPoints, String cashierName, String userCode, String customerCode) {
        this.orderCode = orderCode;
        this.customerName = customerName;
        this.totalPrice = totalPrice;
        this.date = date;
        this.payMethod = payMethod;
        this.addedPoints = addedPoints;
        this.cashierName = cashierName;
        this.userCode = userCode;
        this.customerCode = customerCode;
    }
}