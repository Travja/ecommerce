package me.travja.ecommerce.card;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cart")
@AllArgsConstructor
public class CartRestController {

    private final CartRepository repo;
    private final ItemRepository itemRepo;

    @GetMapping("/all")
    public List<Cart> getAllCarts() {
        return repo.findAll();
    }

    @GetMapping("")
    public Cart getCart(HttpServletRequest request) {
        String id = request.getSession().getId();
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

    @PostMapping("/additem")
    public void addItem(HttpServletRequest request, @RequestBody CartItem item) {
        CartItem temp = itemRepo.findById(item.getId()).orElse(null);
        if (temp == null)
            itemRepo.save(item);

        String id = request.getSession().getId();
        Cart cart = repo.findBySessionId(id).orElse(new Cart(id));

        cart.addItem(item);
        repo.save(cart);
    }

    @PostMapping("removeitem/{itemId}")
    public void removeItem(HttpServletRequest request, @PathVariable int itemId) {
        String id = request.getSession().getId();
        Cart cart = repo.findBySessionId(id).orElse(new Cart(id));

        Optional<CartItem> item = cart.getItems().stream().filter(it -> it.getId() == itemId).findFirst();

        item.ifPresent(it -> cart.removeItem(it));

        repo.save(cart);
        purgeItems();
    }

    public void purgeItems() {
        itemRepo.findAll().stream().filter(it -> it.getCart() == null).forEach(it -> itemRepo.delete(it));
    }

    @DeleteMapping("")
    public void removeLocalCart(HttpServletRequest request) {
        repo.findBySessionId(request.getSession().getId()).ifPresent(cart -> cart.destroy());
        repo.deleteBySessionId(request.getSession().getId());

        purgeItems();
    }

    @DeleteMapping("/{id}")
    public void removeCart(@PathVariable String id) {
        repo.deleteById(id);
    }

}
