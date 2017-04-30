/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package business;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author indap.n
 */
public class EmailUtility {

    public static void sendEmail(int xPos, int yPos)
            throws AddressException, MessagingException, IOException {

        File attachFile = new File("Fire.png");

        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.host", Constants.SMTP_HOST);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", Constants.PORT);
        props.put("mail.smtp.socketFactory.port", Constants.PORT);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");

        final String userName = Constants.USER;
        final String password = Constants.PASSWORD;

        // creates a new session with an authenticator
        Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password);
            }
        };
        Session session = Session.getInstance(props, auth);

        // creates a new e-mail message
        Message msg = new MimeMessage(session);

        msg.setFrom(new InternetAddress(userName));
        InternetAddress[] toAddresses = {new InternetAddress(Constants.TO_USER)};
        msg.setRecipients(Message.RecipientType.TO, toAddresses);
        msg.setSubject(Constants.EMAIL_SUB);
        msg.setSentDate(new Date());

        // creates message part
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent("Fire-detected at co-ordinates (" + xPos + "," + yPos + ").\n\nPlease send assistance immediately.", "text/html");

        // creates multi-part
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);

        // adds attachment
        if (attachFile != null) {
            MimeBodyPart attachPart = new MimeBodyPart();

            try {
                attachPart.attachFile(attachFile);
            } catch (IOException ex) {
                throw ex;
            }

            multipart.addBodyPart(attachPart);
        }

        // sets the multi-part as e-mail's content
        msg.setContent(multipart);

        // sends the e-mail
        Transport.send(msg);
    }
}
