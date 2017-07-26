package linsixin.demo.test;

import java.io.IOException;

import javax.mail.MessagingException;

import linsixin.demo.Mail;

public class TestEmail {

    public static void main(final String[] args) {
        final String to = "simba.lin@cbxsoftware.com";
        final String from = "simba.lin@cbxsoftware.com";
        final String username = "simba.lin";
        final String password = "ZUMEbrA7re@u4e";
        final String cc = "canon.li@cbxsoftware.com";
        final String hostCore = "prism.coresolutions.com";
        final String port587 = "587";
        final String[] files = {
                "C:\\Users\\simba.lin\\Pictures\\1.PNG",
                "C:\\Users\\simba.lin\\Pictures\\2.PNG"
        };

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
