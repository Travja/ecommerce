package me.travja.ecommerce.card;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/card")
@AllArgsConstructor
public class CardRestController {

    @PostMapping("/check")
    public boolean checkCard(@RequestBody CardInfo info) {
        return !info.isExpired() &&
                (String.valueOf(info.getCardNumber()).endsWith("4") ||
                        String.valueOf(info.getCardNumber()).endsWith("5"));
    }

}
