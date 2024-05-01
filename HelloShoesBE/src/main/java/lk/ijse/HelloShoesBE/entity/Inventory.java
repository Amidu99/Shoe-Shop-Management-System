package lk.ijse.HelloShoesBE.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "inventory")
@Entity
public class Inventory implements SuperEntity{
    @Id
    private String itemCode;
    private String itemDesc;
    @Column(columnDefinition = "LONGTEXT")
    private String itemPic;
    private String category;
    private int size;
    private String supplierCode;
    private String supplierName;
    private double buyPrice;
    private double sellPrice;
    private double profit;
    private double profitMargin;
    private String status;
}