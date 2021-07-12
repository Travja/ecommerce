package me.travja.ecommerce.cart;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cart")
@AllArgsConstructor
@CrossOrigin
public class CartRestController {

    private final CartRepository repo;
    private final ItemRepository itemRepo;

    @GetMapping("/all")
    public List<Cart> getAllCarts() {
        return repo.findAll();
    }

    @GetMapping("/{sid}")
    public Cart getCart(@PathVariable String sid) {
        String id = sid;
        System.out.println("ID is " + id);
        return repo.findBySessionId(id).orElse(null);
    }

//    private String getClientIp(HttpServletRequest request) {
//
//        String remoteAddr = "";
//
//        if (request != null) {
//            remoteAddr = request.getHeader("X-FORWARDED-FOR");
//            if (remoteAddr == null || "".equals(remoteAddr)) {
//                remoteAddr = request.getRemoteAddr();
//            }
//        }
//
//        return remoteAddr;
//    }

    @PostMapping("/{sid}/additem")
    public void addItem(@PathVariable String sid, @RequestBody CartItem item) {
        CartItem temp = itemRepo.findById(item.getId()).orElse(null);
        if (temp == null)
            itemRepo.save(item);

        String id = sid;
        Cart cart = repo.findBySessionId(id).orElse(new Cart(id));

        cart.addItem(item);
        repo.save(cart);
    }

    @PostMapping("/{sid}/removeitem/{itemId}")
    public void removeItem(@PathVariable String sid, @PathVariable int itemId) {
        String id = sid;
        Cart cart = repo.findBySessionId(id).orElse(new Cart(id));

        Optional<CartItem> item = cart.getItems().stream().filter(it -> it.getId() == itemId).findFirst();

        item.ifPresent(it -> cart.removeItem(it));

        repo.save(cart);
        purgeItems();
    }

    public void purgeItems() {
        itemRepo.findAll().stream().filter(it -> it.getCart() == null).forEach(it -> itemRepo.delete(it));
    }

    @DeleteMapping("/{sid}")
    public void removeLocalCart(@PathVariable String sid) {
        repo.findBySessionId(sid).ifPresent(cart -> cart.destroy());
        repo.deleteBySessionId(sid);

        purgeItems();
    }

}
