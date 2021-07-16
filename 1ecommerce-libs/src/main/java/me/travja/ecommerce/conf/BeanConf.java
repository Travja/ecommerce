package me.travja.ecommerce.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class BeanConf {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
