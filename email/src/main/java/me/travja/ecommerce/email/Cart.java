package me.travja.ecommerce.email;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class Cart {

    @Getter
    private String sessionId;

    @JsonProperty("items")
    @Getter
    private List<Item> items = new ArrayList<>();

    public Cart(String sessionId) {
        this.sessionId = sessionId;
    }

    public Cart() {

    }

}