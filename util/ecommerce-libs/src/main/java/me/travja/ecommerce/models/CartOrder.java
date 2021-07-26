package me.travja.ecommerce.models;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class CartOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @OneToOne(cascade = CascadeType.MERGE)
    private Cart cart;

    private String name, email, address;

    private String shippedBy;

    private long trackingNumber;

}
