package me.travja.ecommerce.checkout;

import lombok.Data;

@Data
public class CheckoutRequest {

    private String email;
    private String address;
    private Cart cart;
    private CardInfo cardInfo;

}
