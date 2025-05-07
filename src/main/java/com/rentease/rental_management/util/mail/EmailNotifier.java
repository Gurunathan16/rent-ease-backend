package com.rentease.rental_management.util.mail;

import com.rentease.rental_management.auth.dto.UserAsParty;
import com.rentease.rental_management.rent.dto.PropertyProjection;
import com.rentease.rental_management.rent.entity.Address;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailNotifier
{
    private final JavaMailSender javaMailSender;

    public EmailNotifier(JavaMailSender javaMailSender)
    {
        this.javaMailSender = javaMailSender;
    }

    public void notificationForBuyer(String toEmail, String name, UserAsParty seller,
                                     PropertyProjection propertyProjection)
    {
        Address address = propertyProjection.getAddress();
        String addressBuilder = address.getFullAddress() + " " + address.getSubLocality() + " " +
                address.getLocality() + " " + address.getCity() + " - " + address.getPinCode() + "\nState : " + address.getState();

        try
        {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject("Property Interest Confirmation - Seller Details");

            String content = """
                <p>Dear %s,</p>
                <p>Thanks for showing interest in the property: <strong>%s</strong></p>
                <p><strong>Property Details:</strong><br>
                Address: %s<br>
                Price: ₹%s<br>
                Security Deposit: ₹%s</p>

                <p><strong>Seller's Contact:</strong><br>
                Name: %s<br>
                Email: %s<br>
                Phone: %s</p>

                <p>Feel free to contact the seller to proceed further.</p>
                <p>Regards,<br>Team RentEase</p>
                """.formatted(name,
                    propertyProjection.getTitle(),
                    addressBuilder,
                    propertyProjection.getPrice().getExpectedPrice(),
                    propertyProjection.getPrice().getSecurityDeposit(),
                    seller.getFirstName() + " " + seller.getLastName(),
                    seller.getEmail(),
                    seller.getPhoneNumber()
            );

            helper.setText(content, true);
            javaMailSender.send(message);
        } catch (Exception e) {

            throw new RuntimeException("Failed to send buyer notification", e);
        }
    }

    public void notificationForSeller(String toEmail, String name, UserAsParty buyer,
                                      PropertyProjection propertyProjection)
    {
        Address address = propertyProjection.getAddress();
        String addressBuilder = address.getFullAddress() + " " + address.getSubLocality() + " " +
                address.getLocality() + " " + address.getCity() + " - " + address.getPinCode() + "\nState : " + address.getState();
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject("Someone is Interested in Your Property!");

            String content = """
                <p>Dear %s,</p>
                <p>A user is interested in your property: <strong>%s</strong></p>
                <p><strong>Property Details:</strong><br>
                Address: %s<br>
                Price: ₹%s<br>
                Security Deposit: ₹%s</p>

                <p><strong>Buyer's Contact:</strong><br>
                Name: %s<br>
                Email: %s<br>
                Phone: %s</p>

                <p>You may contact them to proceed with further communication.</p>
                <p>Regards,<br>Team RentEase</p>
                """.formatted(name,
                    propertyProjection.getTitle(),
                    addressBuilder,
                    propertyProjection.getPrice().getExpectedPrice(),
                    propertyProjection.getPrice().getSecurityDeposit(),
                    buyer.getFirstName() + " " + buyer.getLastName(),
                    buyer.getEmail(),
                    buyer.getPhoneNumber()
            );

            helper.setText(content, true);
            javaMailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send seller notification", e);
        }
    }
}
