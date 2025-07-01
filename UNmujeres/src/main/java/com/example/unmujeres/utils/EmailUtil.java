package com.example.unmujeres.utils;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public class EmailUtil {

    /**
     * Configuración para el envío de correos mediante Gmail.
     * <p>
     * Reemplazar los valores de USERNAME y PASSWORD con las credenciales de la
     * cuenta de Gmail que se utilizará para enviar los mensajes o cargar estos
     * datos desde variables de entorno/archivos de configuración según se
     * prefiera.
     */
    private static final String USERNAME = "tu_correo@gmail.com";    // TODO cambiar
    private static final String PASSWORD = "tu_contraseña";          // TODO cambiar
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
}