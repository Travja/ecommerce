package me.travja.ecommerce.cart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableEurekaClient
//@RibbonClient(name = "card-service")
@EntityScan(basePackages = "me.travja.ecommerce")
@ComponentScan(basePackages = "me.travja.ecommerce")
@EnableJpaRepositories(basePackages = "me.travja.ecommerce")
public class CartApplication {

    public static void main(String[] args) {
        SpringApplication.run(CartApplication.class, args);
    }

}
