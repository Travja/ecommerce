package me.travja.ecommerce.checkout;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity//(name = "item")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    @Setter
    private int uid;

    @Getter
    @Setter
    private int id;

    @Getter
    @Setter
    private String title, description;
    @Getter
    @Setter
    private double unitPrice = -1;
    @Getter
    @Setter
    private int qty;
    @Getter
    @Setter
    @OneToOne
    @JsonIgnore
    private Cart cart;

    public CartItem() {

    }
}
