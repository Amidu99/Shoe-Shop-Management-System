package lk.ijse.HelloShoesBE.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SaleInventoriesDTO implements SuperDTO {
    private String orderDetailCode;
    private String itemCode;
    private String orderCode;
    private int size;
    private int qty;
}