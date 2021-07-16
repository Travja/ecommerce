package me.travja.ecommerce.catalog;

import lombok.AllArgsConstructor;
import me.travja.ecommerce.models.Item;
import me.travja.ecommerce.repo.CatalogRepository;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
@CrossOrigin
public class CatalogRestController {

    private final CatalogRepository repo;

    @GetMapping("/items")
    public List<Item> getAllItems() {
        return repo.findAll();
    }

    @GetMapping("/items/search")
    public List<Item> searchItems(@RequestParam String title) {
        return repo.searchByTitle(title);
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

    @PostConstruct
    public void init() {
        if(repo.findAll().size() == 0) {
            repo.save(new Item("Chips", "What a great snack!", 1.99));
            repo.save(new Item("Milk", "Basic 2% Milk", 2.89));
            repo.save(new Item("Eggs", "Just a dozen eggs", 1.79));
            repo.save(new Item("Red T-Shirt", "Basic Red T-Shirt", 4.99));
            repo.save(new Item("Blue T-Shirt", "Basic Blue T-Shirt", 4.99));
            repo.save(new Item("Black T-Shirt", "Basic Black T-Shirt", 4.99));
            repo.save(new Item("Gray T-Shirt", "Basic Gray T-Shirt", 4.99));
            repo.save(new Item("White T-Shirt", "Basic White T-Shirt", 4.99));
            repo.save(new Item("Jeans", "Normal Jeans", 21.99));
            repo.save(new Item("Shorts", "Normal Shorts", 14.99));
            repo.save(new Item("Shoes", "These will probably fall apart next week.", 10.99));
            repo.save(new Item("Socks", "They cover your feet.", 7.99));
            repo.save(new Item("Cheez-Its", "Mm. Cheese", 2.57));
            repo.save(new Item("Mop", "Cleans things", 9.99));
            repo.save(new Item("Chicken", "It's a live chicken", 4.99));
            repo.save(new Item("Drone", "Don't fly too high", 25.67));
            repo.save(new Item("Batteries", "I've got the power!", 5.43));
            repo.save(new Item("Donuts", "Homer approves.", 3.12));
            repo.save(new Item("Bicycle", "Does anyone use these?", 45.99));
            repo.save(new Item("Helmet", "Good companion to the Bicycle", 12.99));
            repo.save(new Item("Spray Paint", "Don't do graffiti", 4.49));
            repo.save(new Item("Pop Figure", "Make good pals on your desk", 8.67));
            repo.save(new Item("$5 Movie", "It's $5 and will make you laugh", 5));
            repo.save(new Item("Shampoo", "Gotta keep clean", 3.81));
            repo.save(new Item("Conditioner", "Keep that friz down.", 3.98));
        }
    }

}
