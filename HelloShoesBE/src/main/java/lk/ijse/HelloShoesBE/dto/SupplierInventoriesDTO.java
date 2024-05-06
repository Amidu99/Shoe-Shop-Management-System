package lk.ijse.HelloShoesBE.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SupplierInventoriesDTO implements SuperDTO{
    private String stockCode;
    private int size;
    private int originalQty;
    private int availableQty;
    private String status;
    private String itemCode;
    private String supplierCode;
}