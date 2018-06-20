/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans.respaldo;

import java.io.Serializable;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class EnviarMail implements Serializable {

    static Properties mailServerProperties;
    static Session getMailSession;
    static MimeMessage generateMailMessage;

    private String usuario;
    private String body;

    public EnviarMail() {
    }

    public static Properties getMailServerProperties() {
        return mailServerProperties;
    }

    public static Session getGetMailSession() {
        return getMailSession;
    }

    public static MimeMessage getGenerateMailMessage() {
        return generateMailMessage;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void send() throws MessagingException {
        Transport transport = null;
        Session session = null;

        if (usuario == "") {
            usuario = "Anonimo";
        }
            Properties properties = new Properties();

            properties.put("mail.smtp.ssl.enable", "false");

            session = Session.getDefaultInstance(properties, null);
            transport = session.getTransport("smtp");
            transport.connect(
                    "94.249.236.114",
                    "admin@bitacora.com",
                    "1234");
         
       
        Message msg = new MimeMessage(session);

        msg.setFrom(new InternetAddress("admin@bitacora.com"));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse("rafalitro7@hotmail.com", false));
        msg.setSubject("Alta en bitacora - "+usuario); //ASUNTO
        String text = body; //CONTENIDO
        msg.setContent(text, "text/html; charset=utf-8");
        msg.setHeader("X-Mailer", "smtpsend");
        msg.setSentDate(new Date());
        transport.sendMessage(msg, msg.getAllRecipients());
        transport.close();

    }

}
