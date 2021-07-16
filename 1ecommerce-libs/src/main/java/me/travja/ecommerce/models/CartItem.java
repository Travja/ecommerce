package me.travja.ecommerce.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity//(name = "item")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    @Setter
    private int uid;

    @Getter
    @Setter
    @JsonProperty(value = "id")
    private int itemId;

    @Getter
    @Setter
    private String title, description;
    @Getter
    @Setter
    private double unitPrice = -1;
    @Getter
    @Setter
    private int qty;

    public CartItem() {

    }
}