// Copyright (c) 1998-2017 Core Solutions Limited. All rights reserved.
// ============================================================================
// CURRENT VERSION CNT.5.0.1
// ============================================================================
// CHANGE LOG
// CNT.5.0.1 : 2017-07-26, simba.lin, creation
// ============================================================================
package linsixin.demo;

import java.io.IOException;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


public class Mail {

    private static final int CHAR_NOT_FOUND = -1;

    private Message message;
    
    private MimeMultipart body;
    
    private String username;
    private String password;
    
    private String host;
    private String port;
    
    
    
    public Mail(String host,String port,String username,String password) {
		this.host = host;
		this.port = port;
		this.username = username;
		this.password = password;
	}
    
    public void createNewEmail() {
    	message = new MimeMessage(
    			buildSession(setupProperties(host, port), username, password));
    	body = new MimeMultipart();
    }
    
    protected Session buildSession(final Properties properties, final String username, final String password) {
        return Session.getInstance(properties,
         new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
               return new PasswordAuthentication(username, password);
       }
         });
    }
    
    
    protected Properties setupProperties(final String host, final String port) {
        final Properties props = new Properties();
        props.put("mail.debug", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        return props;
    }


    public void fillEmailTitle(String from,String to,String subject) throws AddressException, MessagingException {
    	message.setFrom(new InternetAddress(from));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);
    }
    
    public void addCC(String ccAddrs) throws AddressException, MessagingException {
    	message.addRecipients(Message.RecipientType.CC, InternetAddress.parse(ccAddrs));
    }


    public void setTextPart(final String text) throws MessagingException {
        final BodyPart textPart = new MimeBodyPart();
        textPart.setText(text);
        body.addBodyPart(textPart);
    }


    public void setFileAttachments(
            final String[] files) throws MessagingException, IOException {
        if (files == null || files.length == 0) {
            return;
        }
        for (final String file:files) {
        	body.addBodyPart(buildFilePart(file));
        }

    }

    private BodyPart buildFilePart(final String file) throws MessagingException {
        assert file != null;
        final BodyPart bodyPart = new MimeBodyPart();
        final DataSource dataSource = new FileDataSource(file);
        bodyPart.setDataHandler(new DataHandler(dataSource));
        bodyPart.setFileName(parseFileName(file));
        return bodyPart;
    }

    private String parseFileName(final String path) {
        if (path == null) {
            return "unknow.txt";
        }
        final int index = path.lastIndexOf("\\");
        if (index == CHAR_NOT_FOUND) {
            return path;
        }
        return path.substring(index + 1);
    }
    
    public void finishAndSendEmail() throws MessagingException {
		message.setContent(body);
		Transport.send(message);
	}

}
