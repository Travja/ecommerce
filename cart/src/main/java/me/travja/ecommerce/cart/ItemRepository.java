package me.travja.ecommerce.cart;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends CrudRepository<CartItem, Integer> {

    List<CartItem> findAll();

    Optional<CartItem> findById(int id);

    List<CartItem> findAllByCartSessionId(String cartId);

    @Query(nativeQuery = true, value = "SELECT * FROM item i WHERE i.title = :title " +
            "and i.description = :description LIMIT 1")
    CartItem match(@Param("title") String title, @Param("description") String description);

    CartItem save(CartItem item);

    void delete(CartItem item);

    void deleteById(int id);

}
