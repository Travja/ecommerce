package me.travja.ecommerce.models;

import lombok.Data;
import me.travja.ecommerce.models.Cart;

@Data
public class EmailRequest {

    private String email;
    private Cart cart;

}
