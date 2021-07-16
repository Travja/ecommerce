package me.travja.ecommerce.checkout;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import me.travja.ecommerce.models.Cart;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@RestController
@RequestMapping("/checkout")
@AllArgsConstructor
@CrossOrigin
public class CheckoutRestController {

    private OrderRepository repo;
    private CartRepository cartRepo;

    @PostMapping("")
    public ResponseEntity<Object> getCart(@RequestBody CheckoutRequest request) {

        Cart cart = request.getCart();
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
            order.setItems(cart.getItems());

            order.getItems().forEach(it -> System.out.println(it));

            System.out.println("Made it to 1");
            repo.save(order);
            System.out.println("Made it to 2");

            EmailRequest emailRequest = new EmailRequest();
            emailRequest.setCart(cart);
            emailRequest.setEmail(email);
            boolean sentRequest = sendEmail(emailRequest);
            if (sentRequest) {
                cart.destroy();
                cartRepo.deleteById(cart.getSessionId());
                return new ResponseEntity<>(HttpStatus.OK);
            } else
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    private boolean sendEmail(EmailRequest request) {
        try {
            URL url = new URL("http://email-service:8080/email");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");

            conn.setDoOutput(true);

            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(request);

            OutputStream os = conn.getOutputStream();
            byte[] input = json.getBytes("utf-8");
            os.write(input, 0, input.length);
            os.close();

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            boolean authorized = response.toString().equalsIgnoreCase("true");
            br.close();
            return authorized;
        } catch (IOException e) {
            System.out.println("Could not establish connection to email auth service.");
            e.printStackTrace();
        }

        return false;
    }

    private boolean checkAuth(CardInfo cardInfo) {
        try {
            URL url = new URL("http://card-service:8080/card/check");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");

            conn.setDoOutput(true);

            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(cardInfo);

            OutputStream os = conn.getOutputStream();
            byte[] input = json.getBytes("utf-8");
            os.write(input, 0, input.length);
            os.close();

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            boolean authorized = response.toString().equalsIgnoreCase("true");
            br.close();
            return authorized;
        } catch (IOException e) {
            System.out.println("Could not establish connection to card auth service.");
            e.printStackTrace();
        }

        return false;
    }

}