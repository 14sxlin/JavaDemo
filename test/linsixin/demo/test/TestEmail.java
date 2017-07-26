package linsixin.demo.test;

import java.io.IOException;

import javax.mail.MessagingException;

import linsixin.demo.Mail;

public class TestEmail {

    public static void main(final String[] args) {

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
