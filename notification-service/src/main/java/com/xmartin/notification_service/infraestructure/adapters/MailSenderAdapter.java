package com.xmartin.notification_service.infraestructure.adapters;

import com.xmartin.notification_service.domain.model.*;
import com.xmartin.notification_service.domain.port.out.MailSenderPort;
import com.xmartin.notification_service.infraestructure.exceptions.SendEmailException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailSenderAdapter implements MailSenderPort {

    private final JavaMailSender emailSender;
    @Value("${spring.mail.username}")
    private String supportEmail;

    @Override
    public void sendCreateNotification(CreateTaskEvent event) throws SendEmailException {

        String message = """
                                
                Dear %s,
                                
                You have successfully created a task with the title: %s, with a due date for %s.
                                
                Att,
                Task Manager Api
                                
                """.formatted(event.user().name(), event.title(), event.dueDate());

        log.info("\n{}", message);
        sendEmail(event.user().email(), "Task Created Notification", message);

    }

    @Override
    public void sendUpdateNotification(UpdateTaskEvent event) throws SendEmailException {
        String message = """
                                
                Dear %s,
                                
                You have successfully updated a task with the title: %s, with a due date for %s.
                                
                Current task status: %s
                                
                Att,
                Task Manager Api
                               
                """.formatted(event.user().name(), event.title(), event.dueDate(), event.status().getText());

        log.info("\n{}", message);
        sendEmail(event.user().email(), "Task Updated Notification", message);
    }

    @Override
    public void sendDeleteNotification(DeleteTaskEvent event) throws SendEmailException {
        String message = """
                                
                Dear %s,
                                
                You have successfully deleted a task with the title: %s.
                                
                Att,
                Task Manager Api
                                
                """.formatted(event.user().name(), event.title());

        log.info("\n{}", message);
        sendEmail(event.user().email(), "Task Deleted Notification", message);
    }

    @Override
    public void sendPasswordTokenNotification(PasswordTokenEvent event) throws SendEmailException {
        String message = """
                                
                Use the following code to reset your password:
                     
                %s
                   
                Att,
                Task Manager Api
                                
                """.formatted(event.token());

        log.info("\n{}", message);
        sendEmail(event.email(), "Recover Password Code", message);
    }

    @Override
    public void sendResetPasswordNotification(ResetPasswordEvent event) throws SendEmailException {
        String message = """
                                
                Dear %s,
                                
                You have successfully changed your password.
                                
                Att,
                Task Manager Api
                                
                """.formatted(event.user().name());

        log.info("\n{}", message);
        sendEmail(event.user().email(), "Password Changed", message);
    }

    @Override
    public void sendRegisterUserNotification(RegisterUserEvent event) throws SendEmailException {
        String message = """
                                
                Dear %s,
                                
                Your registration has been completed.
                                
                Att,
                Task Manager Api
                                
                """.formatted(event.user().name());

        log.info("\n{}", message);
        sendEmail(event.user().email(), "Registration Completed", message);
    }

    private void sendEmail(String recipient, String subject, String content) throws SendEmailException {
        try {
            MimeMessage mimeMessage = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setFrom(supportEmail);
            helper.setTo(recipient);
            helper.setSubject(subject);
            helper.setText(content);
            emailSender.send(mimeMessage);
            log.info("Email sent to: {}", recipient);
        } catch (Exception e) {
            throw new SendEmailException(e);
        }
    }

}
