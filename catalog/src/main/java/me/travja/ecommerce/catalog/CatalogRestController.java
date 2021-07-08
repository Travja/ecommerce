package me.travja.ecommerce.catalog;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class CatalogRestController {

    private final CatalogRepository repo;

    @GetMapping("/items")
    public List<Item> getAllItems() {
        return repo.findAll();
    }

    @PostMapping("/items")
    public void createItem(@RequestBody Item item) {
        repo.save(item);
    }

    @PatchMapping("/items/{id}")
    public void patchItem(@PathVariable int id, @RequestBody Item item) {
        Item itm = repo.findById(id).get();
        if (itm == null) return;

        if (item.getTitle() != null)
            itm.setTitle(item.getTitle());
        if (item.getDescription() != null)
            itm.setDescription(item.getDescription());
        if (item.getUnitPrice() != -1)
            itm.setUnitPrice(item.getUnitPrice());

        repo.save(itm);
    }

    @DeleteMapping("/items/{id}")
    public void deleteItem(@PathVariable int id) {
        repo.deleteById(id);
    }

}