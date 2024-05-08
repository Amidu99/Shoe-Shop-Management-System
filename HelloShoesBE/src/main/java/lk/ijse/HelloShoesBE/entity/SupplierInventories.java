package lk.ijse.HelloShoesBE.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "supplier_inventories")
@Entity
public class SupplierInventories implements SuperEntity{
    @Id
    private String stockCode;
    private int size;
    private int originalQty;
    private int availableQty;
    private String status;

    @ManyToOne
    @JoinColumn(name = "itemCode")
    private Inventory inventory;

    @ManyToOne
    @JoinColumn(name = "supplierCode")
    private Supplier supplier;
}