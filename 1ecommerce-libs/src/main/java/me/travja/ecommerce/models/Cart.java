package me.travja.ecommerce.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
public class Cart {

    @Getter
    @Id
    private String sessionId;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonProperty("items")
    @Getter
    private List<CartItem> items = new ArrayList<>();

    public Cart(String sessionId) {
        this.sessionId = sessionId;
    }

    public Cart() {

    }

    public boolean containsItem(CartItem item) {
        for (CartItem cartItem : items) {
            System.out.println("Items: " + cartItem.getItemId());
        }
        return items.stream().filter(it -> it.getItemId() == item.getItemId()).count() > 0;
    }

    public void addItem(CartItem item) {
        Optional<CartItem> i = items.stream().filter(it -> it.getItemId() == item.getItemId()).findFirst();

        i.ifPresentOrElse(
                it -> {
                    System.out.println("\n\nItem QTY: " + it.getQty());
                    System.out.println(item.getQty());
                    it.setQty(it.getQty() + item.getQty());
                },
                () -> {
                    items.add(item);
//                    item.setCart(this);
                }
        );
    }

    public void removeItem(CartItem item) {
        Optional<CartItem> i = items.stream().filter(it -> it.getItemId() == item.getItemId()).findFirst();

        i.ifPresent(it -> {
            if (item.getQty() >= it.getQty()) {
                items.remove(it);
//                it.setCart(null);
            } else {
                it.setQty(it.getQty() - item.getQty());
                if (it.getQty() <= 0)
                    items.remove(it);
            }
        });
//        item.setCart(null);
    }

    public void destroy() {
//        items.forEach(item -> item.setCart(null));
        items.clear();
    }
}