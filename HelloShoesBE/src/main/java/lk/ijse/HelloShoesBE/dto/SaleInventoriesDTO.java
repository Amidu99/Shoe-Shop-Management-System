package lk.ijse.HelloShoesBE.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SaleInventoriesDTO implements SuperDTO {
    private String orderDetailCode;
    private String itemCode;
    private String itemDesc;
    private double itemPrice;
    private String orderCode;
    private Date date;
    private int size;
    private int qty;

    public SaleInventoriesDTO(String orderDetailCode, String itemCode, String orderCode, Date date, int size, int qty){
        this.orderDetailCode = orderDetailCode;
        this.itemCode = itemCode;
        this.orderCode = orderCode;
        this.date = date;
        this.size = size;
        this.qty = qty;
    }
}