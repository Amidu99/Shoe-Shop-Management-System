package lk.ijse.HelloShoesBE.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "sale")
@Entity
public class Sale implements SuperEntity{
    @Id
    private String orderCode;
    private String customerName;
    private double totalPrice;
    private Timestamp date;
    private String payMethod;
    private double addedPoints;
    private String cashierName;
    @ManyToOne
    @JoinColumn(name = "userCode")
    private User user;
    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SaleInventories> saleInventories = new HashSet<>();
    @ManyToOne
    @JoinColumn(name = "customerCode")
    private Customer customer;
}