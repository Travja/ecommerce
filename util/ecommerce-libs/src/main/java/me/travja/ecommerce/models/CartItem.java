package me.travja.ecommerce.models;

import lombok.Data;

import javax.persistence.*;

@Entity//(name = "item")
@Data
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @OneToOne
    private Item item;
    private int qty;

}
