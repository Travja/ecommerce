package me.travja.ecommerce.checkout;

import lombok.Data;
import me.travja.ecommerce.models.Cart;

@Data
public class CheckoutRequest {

    private String name, email, address;
//    private Cart cart;
    private CardInfo cardInfo;

}
