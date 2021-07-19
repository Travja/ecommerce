package me.travja.ecommerce.checkout;

import lombok.Data;
import me.travja.ecommerce.models.Cart;

@Data
public class CheckoutRequest {

    private String email;
    private String address;
//    private Cart cart;
    private CardInfo cardInfo;

}
