package sendmail.mailer;

import java.util.Arrays;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@SpringBootApplication
@RestController
public class MailerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MailerApplication.class, args);
	}

	@PostMapping("/sendmail")
	public String sendUsermail(@RequestParam(value = "subject") String subject , @RequestParam(value = "name") String name) {
		Properties properties = System.getProperties();
		properties.setProperty("mail.smtp.host", "mail.smtpbucket.com");
		properties.setProperty("mail.smtp.port", "8025");

		String status = "";

		if(subject.equals("Profile Notification #Edited")){
			status = "Edited";
		} else if (subject.equals("Profile Notification #Created")) {
			status = "Created";
		} else if ( subject.equals("Profile Notification #Deleted")) {
			status = "Deleted";
		}

		Session session = Session.getDefaultInstance(properties);

		MimeMessage message = new MimeMessage(session);
		try {
			message.setFrom(new InternetAddress("zamara@test.com"));
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
		try {
			message.addRecipient(Message.RecipientType.TO, new InternetAddress("staff@test.com"));
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
		try {
			message.setSubject(subject);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
		try {
			message.setText("Greeting " + name +", we are glad to inform you that your staff profile has been " + status);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
		try {
			Transport.send(message);
			System.out.println("message sent successfully");

			return "success";

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}

	}

}
