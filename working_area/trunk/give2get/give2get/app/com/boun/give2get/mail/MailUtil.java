package com.boun.give2get.mail;

import org.apache.log4j.Logger;
import com.boun.give2get.exceptions.MailException;
import com.amazonaws.services.simpleemail.AWSJavaMailTransport;

import javax.mail.Session;
import javax.mail.Message;
import javax.mail.Transport;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.AddressException;

import models.Registration;

/**
 * Created by IntelliJ IDEA.
 * User: canelmas
 * Date: Nov 8, 2011
 * Time: 4:40:58 PM
 * To change this template use File | Settings | File Templates.
 */
public final class MailUtil {

    private static final Logger log = Logger.getLogger(MailUtil.class);


    private static final String COMPLETE_REGISTRATION_URL   = "http://176.34.245.162:9000/complete?reg=";
    private static final String RATE_SERVICE_URL            = "http://176.34.245.162:9000/rate?sId=%1$s&c=%2$s&t=%3$s";

    //private static final String RATE_SERVICE_URL            = "http://192.168.1.67:9000/rate?sId=%1$s&c=%2$s&t=%3$s";



    private static final int PROVIDER    = 1;
    private static final int CONSUMER    = 2;


    public static final void sendRegistrationEmail(Registration registration) throws MailException {

        Session session = Session.getInstance(MailFactory.getMailProperties());

        try {

            // Create a new Message
            Message msg = new MimeMessage(session);

            msg.setFrom(new InternetAddress(MailFactory.getFrom()));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(registration.getEmail()));
            msg.setSubject(MailFactory.getRegistrationSubject());


            //  Registration Complete Url
            StringBuilder sb = new StringBuilder();

            sb.append(COMPLETE_REGISTRATION_URL);
            sb.append(registration.getCode());

            msg.setText(String.format(MailFactory.getRegistrationContent(), new String[] {registration.getFirstname().concat(" ").concat(registration.getLastname()), sb.toString()}));
            msg.saveChanges();


            // Reuse one Transport object for sending all your messages
            // for better performance
            Transport t = new AWSJavaMailTransport(session, null);
            t.connect();
            t.sendMessage(msg, null);


            // Close your transport when you're completely done sending
            // all your messages
            t.close();

        } catch(AddressException e) {

            log.warn(e);

            throw new MailException(e);


        } catch(MessagingException e) {

            log.warn(e);

            throw new MailException(e);
        }

    }

    public static final void sayThankyouForRegistration(String email) {

    }

    //acceptedrequest.subject=Give2Get - Your Request has been accepted!
    //acceptedrequest.content=Dear %1$s,\n\nYour Service Request for %2$s has been accepted by %3$s.Please log in and check the service details for more infrmation.\n\nhttp://176.34.245.162:9000 - Give2Get

    public static final void informRequester(String requesterEmail, String serviceTitle, String providerName, String requesterName) throws MailException {

        System.out.println(providerName);
        System.out.println(requesterEmail);
        System.out.println(serviceTitle);


        Session session = Session.getInstance(MailFactory.getMailProperties());

        try {

            // Create a new Message
            Message msg = new MimeMessage(session);

            msg.setFrom(new InternetAddress(MailFactory.getFrom()));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(requesterEmail));

            //  subject
            msg.setSubject(MailFactory.getAcceptedRequestSubject());


            //  content
            msg.setText(
                    String.format(
                            MailFactory.getAcceptedRequestContent(),
                            new String[] {requesterName, serviceTitle, providerName})
            );


            msg.saveChanges();


            // Reuse one Transport object for sending all your messages
            // for better performance
            Transport t = new AWSJavaMailTransport(session, null);
            t.connect();
            t.sendMessage(msg, null);


            // Close your transport when you're completely done sending
            // all your messages
            t.close();

        } catch(AddressException e) {

            log.warn(e);

            throw new MailException(e);


        } catch(MessagingException e) {

            log.warn(e);

            throw new MailException(e);
        }

    }

    public static final void informServiceProvider(String providerName, String providerEmail, String serviceTitle, String requesterName) throws MailException {

        System.out.println(providerName);
        System.out.println(providerEmail);
        System.out.println(serviceTitle);
        System.out.println(requesterName);

        Session session = Session.getInstance(MailFactory.getMailProperties());

        try {

            // Create a new Message
            Message msg = new MimeMessage(session);

            msg.setFrom(new InternetAddress(MailFactory.getFrom()));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(providerEmail));

            //  subject
            msg.setSubject(MailFactory.getNewRequestSubject());


            //  content
            msg.setText(
                    String.format(
                            MailFactory.getNewRequestContent(),
                            new String[] {providerName, requesterName, serviceTitle, })
            );


            msg.saveChanges();


            // Reuse one Transport object for sending all your messages
            // for better performance
            Transport t = new AWSJavaMailTransport(session, null);
            t.connect();
            t.sendMessage(msg, null);


            // Close your transport when you're completely done sending
            // all your messages
            t.close();

        } catch(AddressException e) {

            log.warn(e);

            throw new MailException(e);


        } catch(MessagingException e) {

            log.warn(e);

            throw new MailException(e);
        }

    }


    public static final void sendProviderRatingMail(String code, int serviceId, String serviceTitle, String requesterName, String providerEmail, String providerName) throws MailException {
        
        Session session = Session.getInstance(MailFactory.getMailProperties());

        try {

            // Create a new Message
            Message msg = new MimeMessage(session);

            msg.setFrom(new InternetAddress(MailFactory.getFrom()));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(providerEmail));

            //  subject
            msg.setSubject(MailFactory.getRateForConsumerSubject());


            String url = String.format(RATE_SERVICE_URL, new String[]{String.valueOf(serviceId), code, String.valueOf(PROVIDER)});
            System.out.println("url=" + url);


            //  content
            msg.setText(
                    String.format(
                            MailFactory.getRateForConsumerContent(),
                            new String[] {providerName, serviceTitle, requesterName, url})
            );


            msg.saveChanges();


            // Reuse one Transport object for sending all your messages
            // for better performance
            Transport t = new AWSJavaMailTransport(session, null);
            t.connect();
            t.sendMessage(msg, null);


            // Close your transport when you're completely done sending
            // all your messages
            t.close();

        } catch(AddressException e) {

            log.warn(e);

            throw new MailException(e);


        } catch(MessagingException e) {

            log.warn(e);

            throw new MailException(e);
        }

    }

    public static final void sendConsumerRatingMail(String code, int serviceId, String consumerEmail, String consumerName, String serviceTitle) throws MailException {

        Session session = Session.getInstance(MailFactory.getMailProperties());

        try {

            // Create a new Message
            Message msg = new MimeMessage(session);

            msg.setFrom(new InternetAddress(MailFactory.getFrom()));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(consumerEmail));

            //  subject
            msg.setSubject(MailFactory.getRateForProviderSubject());

            
            String url = String.format(RATE_SERVICE_URL, new String[]{String.valueOf(serviceId), code, String.valueOf(CONSUMER)});
            System.out.println("url=" + url);


            //  content
            msg.setText(
                    String.format(
                            MailFactory.getRateForProviderContent(),
                            new String[] {consumerName, serviceTitle, url })
            );


            msg.saveChanges();


            // Reuse one Transport object for sending all your messages
            // for better performance
            Transport t = new AWSJavaMailTransport(session, null);
            t.connect();
            t.sendMessage(msg, null);


            // Close your transport when you're completely done sending
            // all your messages
            t.close();

        } catch(AddressException e) {

            log.warn(e);

            throw new MailException(e);


        } catch(MessagingException e) {

            log.warn(e);

            throw new MailException(e);
        }

    }
}
