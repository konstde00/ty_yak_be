package com.ty_yak.social.service;

import com.ty_yak.auth.model.entity.User;
import com.ty_yak.auth.model.enums.StatusEnum;
import com.ty_yak.auth.service.StatusService;
import com.ty_yak.auth.service.UserService;
import com.ty_yak.social.model.enums.NotificationType;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;

import java.util.List;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationService implements SmartInitializingSingleton {

    UserService userService;
    GroupService groupService;
    String FROM_EMAIL_ADDRESS;
    StatusService statusService;
    AmazonSimpleEmailService sesClient;

    public NotificationService(
            AmazonSimpleEmailService sesClient,
            UserService userService,
            @Value("${email-sender-address}") String fromEmailAddress,
            StatusService statusService,
            GroupService groupService) {
        this.sesClient = sesClient;
        this.userService = userService;
        this.groupService = groupService;
        this.statusService = statusService;
        this.FROM_EMAIL_ADDRESS = fromEmailAddress;
    }

    @Override
    public void afterSingletonsInstantiated() {

        sendNotificationToUserGroup(1L, NotificationType.STATUS_UPDATE);
    }

    public void sendNotificationToUserGroup(Long userId, NotificationType notificationType) {

        User currentUser = userService.getById(userId);
        StatusEnum currentStatus = statusService.getStatusByUserId(userId);

        List<String> toAddresses =
                groupService.getEmailsByUserId(userId);

        if (toAddresses.isEmpty()) return;

        String message = getNotificationMessage(currentUser.getName(), currentStatus);

        final SendEmailRequest request =
                createSendEmailRequest(toAddresses, notificationType.getSubject(), message);

        try {
            sesClient.sendEmail(request);
        } catch (Exception e) {

            log.error(
                    "Failed to send email, subject {}, message {}, toAddresses: {}",
                    notificationType.getSubject(),
                    message,
                    toAddresses,
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
