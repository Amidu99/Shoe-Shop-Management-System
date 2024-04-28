package lk.ijse.HelloShoesBE.dto;

import lk.ijse.HelloShoesBE.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SupplierDTO implements SuperDTO {
    private String supplierCode;
    private String supplierName;
    private Category category;
    private String addLine1;
    private String addLine2;
    private String addLine3;
    private String addLine4;
    private String addLine5;
    private String addLine6;
    private String contactNo1;
    private String contactNo2;
    private String email;
}