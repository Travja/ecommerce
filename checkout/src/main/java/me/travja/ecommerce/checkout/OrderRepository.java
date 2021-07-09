package me.travja.ecommerce.checkout;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends CrudRepository<Order, Integer> {

    List<Order> findAll();

    Optional<Order> findItemById(int id);

//    @Query(nativeQuery = true, value = "SELECT * FROM item i WHERE i.title = :title " +
//            "and i.description = :description LIMIT 1")
//    Item match(@Param("title") String title, @Param("description") String description);

    Order save(Order order);

    void delete(Order order);

    void deleteById(int id);

}
