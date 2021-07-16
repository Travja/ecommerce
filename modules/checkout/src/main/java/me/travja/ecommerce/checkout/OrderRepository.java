package me.travja.ecommerce.checkout;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends CrudRepository<CartOrder, Integer> {

    List<CartOrder> findAll();

    Optional<CartOrder> findItemById(int id);

//    @Query(nativeQuery = true, value = "SELECT * FROM item i WHERE i.title = :title " +
//            "and i.description = :description LIMIT 1")
//    Item match(@Param("title") String title, @Param("description") String description);

    CartOrder save(CartOrder order);

    void delete(CartOrder order);

    void deleteById(int id);

}
