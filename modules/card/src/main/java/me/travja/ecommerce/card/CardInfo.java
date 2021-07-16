package me.travja.ecommerce.card;

import lombok.Data;

import java.util.Date;

@Data
public class CardInfo {

    private long cardNumber;
    private Date expirationDate;
    private short cvv;

    public boolean isExpired() {
        return new Date().before(getExpirationDate());
    }

}
