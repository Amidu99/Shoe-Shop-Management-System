package lk.ijse.HelloShoesBE.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.HashSet;
import java.util.Set;

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
    private String supplierCode;
    private String supplierName;
    private double buyPrice;
    private double sellPrice;
    private double profit;
    private double profitMargin;

    @OneToMany(mappedBy = "inventory")
    private Set<SaleInventories> saleInventories = new HashSet<>();

    @OneToMany(mappedBy = "inventory")
    private Set<SupplierInventories> supplierInventories = new HashSet<>();
}