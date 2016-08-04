package be.kuleuven.rega.phylogeotool.pplacer;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Mail {
	private String username = "ue711728";
	private String password = "BioInformatics007";
	private Properties props;

	public Mail() {
		props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtps.kuleuven.be");
		props.put("mail.smtp.port", "587");
	}
	
	public void sendEmail(String recipient, String pplacerId) {
		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("phylogeotool@kuleuven.be"));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
			message.setSubject("Your PPlacing job has finished");
			message.setText("Dear User,"
					+ "\n\n Your PPlacing job has recently finished and can be reviewed here: http://localhost:8080/phylogeotool/PhyloGeoTool/root_1?pplacer="
					+ pplacerId);

			Transport.send(message);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void main(String[] args) {
		Mail mail = new Mail();
		mail.sendEmail("ewout.ve@gmail.com", "CRQdxm0J");
	}
}