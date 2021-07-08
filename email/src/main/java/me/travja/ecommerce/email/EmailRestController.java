package me.travja.ecommerce.email;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email")
@AllArgsConstructor
public class EmailRestController {

    @PostMapping("")
    public void sendEmail() {
        //TODO Implement this
    }

}
