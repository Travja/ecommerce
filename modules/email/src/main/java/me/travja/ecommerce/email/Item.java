package me.travja.ecommerce.email;

import lombok.Data;

@Data
public class Item {

    private String title, description;
    private double unitPrice = -1;
    private int qty;

    public Item() {

    }
}
