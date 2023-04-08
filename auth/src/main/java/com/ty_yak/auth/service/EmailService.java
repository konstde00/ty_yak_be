package com.ty_yak.auth.service;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.*;
import com.ty_yak.auth.model.enums.StatusEnum;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailService {
    String FROM_EMAIL_ADDRESS;
    AmazonSimpleEmailService sesClient;

    public EmailService(
            AmazonSimpleEmailService sesClient,
            @Value("${email-sender-address}") String fromEmailAddress) {
        this.sesClient = sesClient;
        this.FROM_EMAIL_ADDRESS = fromEmailAddress;
    }

    public void sendRecoveryCode(String email, int code) {

        String subject = "Recovery code";
        String message = "Your recovery code is " + code;

        final SendEmailRequest request =
                createSendEmailRequest(List.of(email), subject, message);

        try {
            sesClient.sendEmail(request);
        } catch (Exception e) {

            log.error(
                    "Failed to send email, subject {}, message {}, toAddresses: {}",
                    subject,
                    message,
                    email,
                    e);

            throw e;
        }
    }

    protected SendEmailRequest createSendEmailRequest(
            List<String> toAddresses, String subject, String content) {
        val request = new SendEmailRequest()
                .withDestination(
                        new Destination().withToAddresses(toAddresses))
                .withMessage(new Message()
                        .withBody(new Body()
                                .withText(new Content()
                                        .withCharset("UTF-8").withData(content)))
                        .withSubject(new Content()
                                .withCharset("UTF-8").withData(subject)))
                .withSource(FROM_EMAIL_ADDRESS);
        return request;
    }

    private String getNotificationMessage(String firstName,  StatusEnum currentStatus) {

        return "Hello " + ",\n" +
                "You have a new notification from Ty Yak.\n" +
                String.format("Your friend %s has updated his status, current status is %s\n",
                        firstName, currentStatus.name()) +
                "Thank you for using Ty Yak.\n" +
                "Best regards,\n" +
                "Ty Yak Team";
    }
}
