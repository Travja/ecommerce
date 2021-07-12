package me.travja.ecommerce.checkout;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class CartOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @OneToMany(cascade = CascadeType.MERGE)
    private List<CartItem> items = new ArrayList<>();

    private String email, address;

}
