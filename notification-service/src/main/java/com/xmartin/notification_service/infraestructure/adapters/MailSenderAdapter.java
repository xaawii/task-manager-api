package com.xmartin.notification_service.infraestructure.adapters;

import com.xmartin.notification_service.domain.model.CreateTaskEvent;
import com.xmartin.notification_service.domain.model.DeleteTaskEvent;
import com.xmartin.notification_service.domain.model.UpdateTaskEvent;
import com.xmartin.notification_service.domain.port.out.MailSenderPort;
import com.xmartin.notification_service.infraestructure.exceptions.SendEmailException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailSenderAdapter implements MailSenderPort {

    private final JavaMailSender emailSender;
    @Value("${spring.mail.username}")
    private String supportEmail;

    @Override
    public void sendCreateNotification(CreateTaskEvent event) throws SendEmailException {

        String message =
                """
                        ===================================================
                        Task Created Notification
                        ----------------------------------------------------
                        Dear %s,
                        You have successfully created a task with the title: %s, with a due date for %s.
                                        
                                        
                        Task Manager Api
                        ===================================================
                        """
                        .formatted(event.user().name(), event.title(), formatDate(event.dueDate()));

        log.info("\n{}", message);
        sendEmail(event.user().email(), "Task Created Notification", message);

    }

    @Override
    public void sendUpdateNotification(UpdateTaskEvent event) throws SendEmailException {
        String message =
                """
                        ===================================================
                        Task Updated Notification
                        ----------------------------------------------------
                        Dear %s,
                        You have successfully updated a task with the title: %s, with a due date for %s.
                                        
                        Current task status: %s
                                        
                        Task Manager Api
                        ===================================================
                        """
                        .formatted(event.user().name(), event.title(), formatDate(event.dueDate()),
                                event.status().getText());

        log.info("\n{}", message);
        sendEmail(event.user().email(), "Task Updated Notification", message);
    }

    @Override
    public void sendDeleteNotification(DeleteTaskEvent event) throws SendEmailException {
        String message =
                """
                        ===================================================
                        Task Deleted Notification
                        ----------------------------------------------------
                        Dear %s,
                        You have successfully deleted a task with the title: %s.
                                        
                                        
                        Task Manager Api
                        ===================================================
                        """
                        .formatted(event.user().name(), event.title());

        log.info("\n{}", message);
        sendEmail(event.user().email(), "Task Deleted Notification", message);
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

    private String formatDate(String date) {
        DateTimeFormatter isoFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        LocalDateTime dueDate = LocalDateTime.parse(date, isoFormatter);


        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return dueDate.format(dateFormatter);
    }
}
