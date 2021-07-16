package me.travja.ecommerce.checkout;

import me.travja.ecommerce.models.Cart;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends CrudRepository<Cart, String> {

    List<Cart> findAll();

    Optional<Cart> findById(String id);

    Optional<Cart> findBySessionId(String sessionId);

    Cart save(Cart cart);

    void delete(Cart cart);

    void deleteById(String id);

    void deleteBySessionId(String id);

}
