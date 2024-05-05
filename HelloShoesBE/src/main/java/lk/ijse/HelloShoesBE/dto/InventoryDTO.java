package lk.ijse.HelloShoesBE.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class InventoryDTO implements SuperDTO {
    private String itemCode;
    private String itemDesc;
    private String itemPic;
    private String category;
    private String supplierCode;
    private String supplierName;
    private double buyPrice;
    private double sellPrice;
    private double profit;
    private double profitMargin;
}