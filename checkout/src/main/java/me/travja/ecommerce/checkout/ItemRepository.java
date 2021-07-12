package me.travja.ecommerce.checkout;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends CrudRepository<CartItem, Integer> {

    List<CartItem> findAll();

    List<CartItem> findByItemId(Integer id);

    @Query(nativeQuery = true, value = "SELECT * FROM item i WHERE i.title = :title " +
            "and i.description = :description LIMIT 1")
    CartItem match(@Param("title") String title, @Param("description") String description);

    CartItem save(CartItem item);

    void delete(CartItem item);

}
