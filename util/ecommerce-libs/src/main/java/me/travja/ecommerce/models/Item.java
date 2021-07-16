package me.travja.ecommerce.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "item")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    private int id;
    @Getter
    @Setter
    private String title, description;
    @Getter
    @Setter
    private double unitPrice = -1;

    public Item(String title, String description, double unitPrice) {
        this.title = title;
        this.description = description;
        this.unitPrice = unitPrice;
    }

    public Item() {

    }
}
