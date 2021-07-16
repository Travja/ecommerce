package me.travja.ecommerce.checkout;

import lombok.Data;
import me.travja.ecommerce.models.Cart;

@Data
public class EmailRequest {

    private String email;
    private Cart cart;

}
