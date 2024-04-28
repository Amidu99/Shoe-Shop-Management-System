package lk.ijse.HelloShoesBE.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "supplier")
@Entity
public class Supplier implements SuperEntity{
    @Id
    private String supplierCode;
    private String supplierName;
    @Enumerated(EnumType.STRING)
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