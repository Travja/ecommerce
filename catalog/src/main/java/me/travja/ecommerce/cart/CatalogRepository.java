package me.travja.ecommerce.cart;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CatalogRepository extends CrudRepository<Item, Integer> {

    List<Item> findAll();

    Optional<Item> findItemById(int id);

    @Query(nativeQuery = true, value = "SELECT * FROM item i WHERE i.title = :title " +
            "and i.description = :description LIMIT 1")
    Item match(@Param("title") String title, @Param("description") String description);

    Item save(Item item);

    void delete(Item item);

    void deleteById(int id);

}
