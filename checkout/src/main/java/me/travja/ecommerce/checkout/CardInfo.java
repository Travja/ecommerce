package me.travja.ecommerce.checkout;

import lombok.Data;

import java.util.Date;

@Data
public class CardInfo {

    private int cardNumber;
    private Date expirationDate;
    private int cvv;

    public boolean isExpired() {
        return new Date().before(getExpirationDate());
    }

}
