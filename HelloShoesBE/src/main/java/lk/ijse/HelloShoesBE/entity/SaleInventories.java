package lk.ijse.HelloShoesBE.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Date;

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
    private String orderCode;
    private Date date;
    private int size;
    private int qty;
}