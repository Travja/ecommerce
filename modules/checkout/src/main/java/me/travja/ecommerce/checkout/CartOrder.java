package me.travja.ecommerce.checkout;

import lombok.Data;
import me.travja.ecommerce.models.Cart;

import javax.persistence.*;

@Entity
@Data
public class CartOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @OneToOne(cascade = CascadeType.MERGE)
    private Cart cart;

    private String email, address;

}
