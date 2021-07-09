package me.travja.ecommerce.email;

import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email")
@AllArgsConstructor
public class EmailRestController {

    private final JavaMailSender emailSender;

    public EmailRestController(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(MailConfiguration.email);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }

    @PostMapping("")
    public void sendEmail(@RequestBody EmailRequest request) {
        //TODO Implement this
        sendSimpleMessage(request.getEmail(), "Order received!",
                "We have received your order. Your items total: $" + request.getCart().getTotal());
    }

}
