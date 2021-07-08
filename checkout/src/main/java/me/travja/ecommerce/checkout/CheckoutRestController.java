package me.travja.ecommerce.checkout;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@RestController
@RequestMapping("/checkout")
@AllArgsConstructor
public class CheckoutRestController {

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
            // TODO Actually do the transaction.
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
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