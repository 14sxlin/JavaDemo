package linsixin.demo.test;

import java.io.IOException;

import javax.mail.MessagingException;

import email.Mail;

public class TestEmail {

    public static void main(final String[] args) {

        String hostCore = null;
		String port587 = null;
		String username = null;
		String password = null;
		String from = null;
		String to = null;
		String[] files = null;
        String cc = null;
		final Mail mail = new Mail(hostCore,port587,username,password);

        try {

            mail.createNewEmail();
            
			mail.fillEmailTitle(from, to, "this is a text email");
            mail.setTextPart("my number is 2983y64239847");
            
			mail.setFileAttachments(files);
            
			mail.addCC(cc);
            mail.finishAndSendEmail();
            
        } catch (final MessagingException e) {
            throw new RuntimeException(e);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}
