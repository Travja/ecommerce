package me.travja.ecommerce.card;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/card")
@AllArgsConstructor
public class CardRestController {

    @PostMapping("/check")
    public boolean checkCard(@RequestBody CardInfo info) {
        return true;
    }

}
