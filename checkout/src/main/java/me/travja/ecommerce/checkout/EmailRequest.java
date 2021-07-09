package me.travja.ecommerce.checkout;

import lombok.Data;

@Data
public class EmailRequest {

    private String email;
    private Cart cart;

}
