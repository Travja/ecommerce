package me.travja.ecommerce.card;

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

    @OneToMany(cascade = CascadeType.ALL)
    @JsonProperty("items")
    @Getter
    private List<CartItem> items = new ArrayList<>();

    public Cart(String sessionId) {
        this.sessionId = sessionId;
    }

    public Cart() {

    }

    public void addItem(CartItem item) {
        Optional<CartItem> i = items.stream().filter(it -> it.getId() == item.getId()).findFirst();

        i.ifPresentOrElse(
                it -> it.setQty(it.getQty() + item.getQty()),
                () -> {
                    items.add(item);
                    item.setCart(this);
                }
        );
    }

    public void removeItem(CartItem item) {
        Optional<CartItem> i = items.stream().filter(it -> it.getId() == item.getId()).findFirst();

        i.ifPresent(it -> {
            if (item.getQty() >= it.getQty()) {
                items.remove(it);
                it.setCart(null);
            } else
                it.setQty(it.getQty() - item.getQty());
        });
        item.setCart(null);
    }

    public void destroy() {
        items.forEach(item -> item.setCart(null));
        items.clear();
    }
}