package me.travja.ecommerce.email;

import lombok.Data;

@Data
public class EmailRequest {

    private String email;
    private Cart cart;

}
