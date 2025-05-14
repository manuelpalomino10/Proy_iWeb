package com.example.onu.util;

package com.example.onu.util;

import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.util.Properties;

public class EmailUtil {

    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final int    SMTP_PORT = 587;
    private static final String USERNAME  = "tu.cuenta@gmail.com";     // tu correo
    private static final String PASSWORD  = "tuAppPassword";           // app-password de Gmail

    public static void sendValidationEmail(String toEmail, String code) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.auth",         "true");
        props.put("mail.smtp.starttls.enable","true");
        props.put("mail.smtp.host",         SMTP_HOST);
        props.put("mail.smtp.port",         String.valueOf(SMTP_PORT));

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, PASSWORD);
            }
        });

        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(USERNAME, "Tu App iWeb"));
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
        msg.setSubject("Valida tu correo en iWeb");

        // Construye el enlace de validación
        String link = "https://tuDominio.com/encuestador/confirm?code=" + code;
        String html = "<p>Hola,</p>"
                + "<p>Gracias por registrarte. Haz clic en el botón para validar tu correo:</p>"
                + "<p><a href=\"" + link + "\" "
                + "style=\"display:inline-block;padding:10px 20px;"
                + "background-color:#28a745;color:white;text-decoration:none;"
                + "border-radius:5px;\">Validar Correo</a></p>"
                + "<p>Si no funcionara el botón, copia y pega esta URL en tu navegador:</p>"
                + "<p>" + link + "</p>";

        msg.setContent(html, "text/html; charset=UTF-8");
        Transport.send(msg);
    }
}

