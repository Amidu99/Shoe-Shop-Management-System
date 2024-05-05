package lk.ijse.HelloShoesBE.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "sale_inventories")
@Entity
public class SaleInventories implements SuperEntity{
    @Id
    private String orderDetailCode;
    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "itemCode")
    private Inventory inventory;
    @ManyToOne
    @JoinColumn(name = "orderCode")
    private Sale sale;
    private int size;
    private int qty;
}