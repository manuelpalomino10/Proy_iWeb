package com.example.unmujeres.utils;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public class EmailUtil {

    private static final String USERNAME = "onu.mujeres.proyecto@gmail.com";    
    private static final String PASSWORD = "tfpd yszc fnjo azap";         
    private static final String HOST = "smtp.gmail.com";
    private static final String PORT = "587";

    public static void sendEmail(String to, String subject, String htmlContent) {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", HOST);
            props.put("mail.smtp.port", PORT);
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");

            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(USERNAME, PASSWORD);
                }
            });

            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(USERNAME));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            msg.setSubject(subject);
            msg.setContent(htmlContent, "text/html; charset=utf-8");

            Transport.send(msg);
        } catch (MessagingException e) {
            // En un entorno real se registraría la excepción
            e.printStackTrace();
        }
    }

    //prueba de que si envía correos
    /*
    public static void main(String[] args) {
        sendEmail(
                "tteffahh1002@gmail.com",          // Usa tu propio correo para ver si llega
                "Prueba de correo ONU Mujeres",
                "<h2>¡Hola, lapepelin!</h2><p>Esto es un correo de prueba enviado desde Java usando App Password y Gmail. </p>"
        );
    }
    */
}

