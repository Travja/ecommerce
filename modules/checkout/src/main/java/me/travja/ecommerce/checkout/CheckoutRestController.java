package me.travja.ecommerce.checkout;

import lombok.AllArgsConstructor;
import me.travja.ecommerce.models.Cart;
import me.travja.ecommerce.models.EmailRequest;
import me.travja.ecommerce.models.ErrorBody;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;

@RestController
@RequestMapping("/checkout")
@AllArgsConstructor
public class CheckoutRestController {

    private final LoadBalancerClient loadBalancerClient;
    private final RestTemplate restTemplate;
    private OrderRepository repo;

    @PostMapping("/{cartId}")
    @Transactional
    public ResponseEntity<Object> checkout(@PathVariable String cartId, @RequestBody CheckoutRequest request) {

        Cart cart = getCart(cartId);
        if (cart.getItems().isEmpty())
            return ResponseEntity.badRequest().body(new ErrorBody("Cart is empty"));

        CardInfo cardInfo = request.getCardInfo();
        String address = request.getAddress();
        String email = request.getEmail();

        //Hit card authentication service
        //  IF successful, send email and checkout
        boolean authorized = checkAuth(cardInfo);
        System.out.println("Card is " + (authorized ? "" : "not ") + "authorized.");

        if (authorized) {

            CartOrder order = new CartOrder();
            order.setAddress(address);
            order.setEmail(email);
            order.setCart(cart);

            repo.save(order);

            EmailRequest emailRequest = new EmailRequest();
            emailRequest.setCart(cart);
            emailRequest.setEmail(email);
            boolean sentRequest = sendEmail(emailRequest);
            if (sentRequest) {
//                cart.destroy();
//                cartRepo.deleteById(cart.getSessionId());
                return new ResponseEntity<>(HttpStatus.OK);
            } else
                throw new RuntimeException("Could not find email service.");
        } else {
            return new ResponseEntity<>(new ErrorBody("Card not valid"), HttpStatus.UNAUTHORIZED);
        }
    }

    private Cart getCart(String cartId) {
        ServiceInstance serviceInstance = loadBalancerClient.choose("cart-service");
        if (serviceInstance == null) {
            System.err.println("Service instance is null... Couldn't find the service?");
            return null;
        } else {
            System.out.println("Cart service found at: " + serviceInstance.getUri());

            String url = serviceInstance.getUri() + "/cart/" + cartId;
            return restTemplate.getForObject(url, Cart.class);
        }
    }

    private boolean sendEmail(EmailRequest request) {
        ServiceInstance serviceInstance = loadBalancerClient.choose("email-service");
        if (serviceInstance == null) {
            System.err.println("Service instance is null... Couldn't find the service?");
            return false;
        } else {
            System.out.println("Email service found at: " + serviceInstance.getUri());

            String url = serviceInstance.getUri() + "/email";
            ResponseEntity<Boolean> res = restTemplate.postForEntity(url, request, Boolean.class);
            if (res.getStatusCode() == HttpStatus.OK)
                return res.getBody();
            else
                return false;
        }
    }

    private boolean checkAuth(CardInfo cardInfo) {
        ServiceInstance serviceInstance = loadBalancerClient.choose("card-service");
        if (serviceInstance == null) {
            System.err.println("Service instance is null... Couldn't find the service?");
            return false;
        } else {
            System.out.println("Card service found at: " + serviceInstance.getUri());

            String url = serviceInstance.getUri() + "/card/check";
            ResponseEntity<Boolean> res = restTemplate.postForEntity(url, cardInfo, Boolean.class);
            if (res.getStatusCode() == HttpStatus.OK)
                return res.getBody();
            else
                return false;
        }
    }

}