package me.travja.ecommerce.cart;

import lombok.AllArgsConstructor;
import me.travja.ecommerce.models.Cart;
import me.travja.ecommerce.models.CartItem;
import me.travja.ecommerce.models.Item;
import me.travja.ecommerce.repo.CartRepository;
import me.travja.ecommerce.repo.ItemRepository;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.util.List;

@RestController
@RequestMapping("/cart")
@AllArgsConstructor
@CrossOrigin
public class CartRestController {

    private final CartRepository repo;
    private final ItemRepository itemRepo;
    private final LoadBalancerClient loadBalancerClient;
    private final RestTemplate restTemplate;

    @GetMapping("/all")
    public List<Cart> getAllCarts() {
        return repo.findAll();
    }

    @GetMapping("/{sid}")
    public Cart getCart(@PathVariable String sid) {
        String id = sid;
        System.out.println("ID is " + id);
        return repo.findBySessionId(id).orElse(new Cart());
    }

    @PostMapping("/{sid}/additem/{itemId}")
    public ResponseEntity<Object> addItem(@PathVariable String sid, @PathVariable int itemId, @RequestParam(required = false, defaultValue = "1") int qty) {
        CartItem item = new CartItem();

        ServiceInstance serviceInstance = loadBalancerClient.choose("catalog-service");
        if (serviceInstance == null) {
            System.err.println("Service instance is null... Couldn't find the service?");
            return ResponseEntity.internalServerError().build();
        } else {
            System.out.println("Catalog service found at: " + serviceInstance.getUri());

            String url = serviceInstance.getUri() + "/api/items/" + itemId;
            Item it = restTemplate.getForObject(url, Item.class);
            if (it == null) {
                System.out.println("Item is null!");
                return ResponseEntity.internalServerError().build();
            } else {
                item.setItem(it);
                item.setQty(qty);
                addItem(sid, item);
                return ResponseEntity.ok().build();
            }
        }

    }

    @PostMapping("/{sid}/additem")
    public void addItem(@PathVariable String sid, @RequestBody CartItem citem) { //CartItem item) {
        Item item = citem.getItem();
        String id = sid;
        Cart cart = repo.findBySessionId(id).orElse(new Cart(id));
        if (!cart.containsItem(item)) {
            System.out.println("CART ITEM NOT FOUND");
            itemRepo.save(citem);
        }

        System.out.println(item.getId());
        cart.addItem(citem);
        repo.save(cart);
    }

    @PostMapping("/{sid}/removeitem")
    @Transactional
    public void removeItem(@PathVariable String sid, @RequestBody CartItem citem) {
        Item item = citem.getItem();
        String id = sid;
        Cart cart = repo.findBySessionId(id).orElse(new Cart(id));

        cart.removeItem(citem);

        repo.save(cart);
        if (cart.getItems().stream().filter(it -> it.getItem().getId() == item.getId()).count() == 0)
            itemRepo.delete(citem);
    }

    @PostMapping("/{sid}/removeitem/{itemId}")
    @Transactional
    public ResponseEntity<Object> removeItem(@PathVariable String sid, @PathVariable int itemId, @RequestParam(required = false, defaultValue = "1") int qty) {
        CartItem citem = new CartItem();

        ServiceInstance serviceInstance = loadBalancerClient.choose("catalog-service");
        if (serviceInstance == null) {
            System.err.println("Service instance is null... Couldn't find the service?");
            return ResponseEntity.internalServerError().build();
        } else {
            System.out.println("Catalog service found at: " + serviceInstance.getUri());

            String url = serviceInstance.getUri() + "/api/items/" + itemId;
            Item it = restTemplate.getForObject(url, Item.class);
            if (it == null) {
                System.out.println("Item is null!");
                return ResponseEntity.internalServerError().build();
            } else {
                citem.setItem(it);
                citem.setItem(it);
                citem.setQty(qty);
                removeItem(sid, citem);
                return ResponseEntity.ok().build();
            }
        }
    }

    @DeleteMapping("/{sid}")
    @Transactional
    public void removeLocalCart(@PathVariable String sid) {
        repo.findBySessionId(sid).ifPresent(cart -> cart.destroy());
        repo.deleteBySessionId(sid);
    }

}
