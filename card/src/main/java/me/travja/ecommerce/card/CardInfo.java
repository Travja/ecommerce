package me.travja.ecommerce.card;

import lombok.Data;

import java.util.Date;

@Data
public class CardInfo {

    private int cardNumber;
    private Date expirationDate;
    private int cvv;

    public boolean isExpired() {
        return new Date().after(getExpirationDate());
    }

}
