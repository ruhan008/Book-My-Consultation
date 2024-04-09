package com.bookmyconsultation.notificationservice.service;

import com.bookmyconsultation.notificationservice.model.Appointment;
import com.bookmyconsultation.notificationservice.model.Doctor;
import com.bookmyconsultation.notificationservice.model.Payment;
import com.bookmyconsultation.notificationservice.model.Prescription;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.annotation.PostConstruct;
import javax.mail.Message;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    private String smtpUserName;
    private String smtpPassword;
    private final FreeMarkerConfigurer configurer;

    /* Configure this email in application.yaml as default from email to be used
     while notifying user's, after verification of this fromEmail in Amazon SES */
    @Value("${from-email}")
    private String fromEmail;

    @PostConstruct
    public void init(){
        // When you hit the endpoint to send an email this value needs to be updated to the Smtp username that you generated
        smtpUserName="AKIA46QQ26U37FKWSSOV"; // Configure before run
        // When you hit the endpoint to send an email this value needs to be updated to the Smtp password that you generated
        smtpPassword="BNK02Brnc/rMsxqGLP5tU6jmIeV69C1ptA7+6t1iz0Pc"; // Configure before run
    }

    public void sendEmailForDoctorEvent(Doctor doctor,String eventType) throws IOException, TemplateException, MessagingException,
            javax.mail.MessagingException {
        String emailTemplate = "";
        String subject = "";
        if (eventType.equalsIgnoreCase("DoctorApprovalEvent")) {
            emailTemplate = "doctor-approved.ftl";
            subject = "BMC - Application Approved";
        } else if (eventType.equalsIgnoreCase("DoctorRejectionEvent")) {
            emailTemplate = "doctor-rejected.ftl";
            subject = "BMC - Application Rejected";
        }
        Map<String,Object> templateModel = new HashMap<>();
        templateModel.put("doctor",doctor);
        Template freeMarkerTemplate = configurer.getConfiguration().getTemplate(emailTemplate);
        String htmlBody = FreeMarkerTemplateUtils.processTemplateIntoString(freeMarkerTemplate,templateModel);
        sendSimpleMessage(doctor.getEmailId(),subject,htmlBody);
    }

    public void sendEmailForPaymentEvent(String eventType, Payment payment) throws TemplateException, IOException,
            javax.mail.MessagingException {
        String emailTemplate = "";
        if (eventType.equalsIgnoreCase("AppointmentPaymentEvent")) {
            emailTemplate = "payment-confirmation.ftl";
        }
        Map<String,Object> templateModel = new HashMap<>();
        templateModel.put("payment",payment);
        Template freeMarkerTemplate = configurer.getConfiguration().getTemplate(emailTemplate);
        String htmlBody = FreeMarkerTemplateUtils.processTemplateIntoString(freeMarkerTemplate,templateModel);
        sendSimpleMessage(payment.getUserEmailId(),"BMC - Payment Confirmation for Appointment",htmlBody);
    }

    public void sendEmailForUserEvent(String eventType, Appointment appointment) throws IOException,
            TemplateException, javax.mail.MessagingException {
        String emailTemplate = "";
        if (eventType.equalsIgnoreCase("UserAppointmentEvent")) {
            emailTemplate = "user-appointment.ftl";
        }
        Map<String,Object> templateModel = new HashMap<>();
        templateModel.put("appointment",appointment);
        Template freeMarkerTemplate = configurer.getConfiguration().getTemplate(emailTemplate);
        String htmlBody = FreeMarkerTemplateUtils.processTemplateIntoString(freeMarkerTemplate,templateModel);
        sendSimpleMessage(appointment.getUserEmailId(),"BMC - Appointment Confirmation",htmlBody);
    }

    public void sendEmailForPrescriptionEvent(String eventType, Prescription prescription) throws IOException,
            TemplateException, javax.mail.MessagingException {
        String emailTemplate = "";
        if (eventType.equalsIgnoreCase("PrescriptionUploadEvent")) {
            emailTemplate = "prescription-upload.ftl";
        }
        Map<String,Object> templateModel = new HashMap<>();
        templateModel.put("prescription",prescription);
        Template freeMarkerTemplate = configurer.getConfiguration().getTemplate(emailTemplate);
        String htmlBody = FreeMarkerTemplateUtils.processTemplateIntoString(freeMarkerTemplate,templateModel);
        sendSimpleMessage(prescription.getUserEmailId(),"BMC - Prescription Uploaded",htmlBody);
    }

    private void sendSimpleMessage(String toEmail, String subject, String body) throws MessagingException,
            javax.mail.MessagingException {
        Properties props = System.getProperties();
        props.put("mail.transport.protocol","smtp");
        props.put("mail.smtp.port",587);
        props.put("mail.smtp.starttls.enable","true");
        props.put("mail.smtp.auth","true");
        javax.mail.Session session = javax.mail.Session.getDefaultInstance(props);
        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(fromEmail);
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
        msg.setSubject(subject);
        msg.setContent(body,"text/html");
        Transport transport = session.getTransport();
        try {
            transport.connect("email-smtp.us-east-1.amazonaws.com", smtpUserName, smtpPassword);
            transport.sendMessage(msg, msg.getAllRecipients());
            log.info("Email sent to id: {} successfully", toEmail);
        }catch(Exception e){
            log.error(e.getMessage());
        }finally {
            transport.close();
        }
    }

}
