package me.travja.ecommerce.checkout;

import lombok.AllArgsConstructor;
import me.travja.ecommerce.models.CartOrder;
import me.travja.ecommerce.repo.OrderRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
@AllArgsConstructor
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class OrderRestController {

    private OrderRepository repo;

    @GetMapping
    public List<CartOrder> getOrders() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public CartOrder getOrder(@PathVariable int id) {
        return repo.findById(id).orElse(null);
    }

    @PostMapping("/{id}/shipped/{shippedThru}/{tracking}")
    public ResponseEntity setShipped(@PathVariable int id, @PathVariable String shippedThru, @PathVariable long tracking) {
        CartOrder order = getOrder(id);
        if (order != null) {
            order.setShippedBy(shippedThru);
            order.setTrackingNumber(tracking);
            repo.save(order);
            return ResponseEntity.ok().build();
        } else
            return ResponseEntity.notFound().build();
    }

    @PostMapping("/update")
    public void saveOrder(@RequestBody CartOrder order) {
        repo.save(order);
    }

}