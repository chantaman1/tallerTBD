package backend.extra;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class EmailSender implements Runnable{

	public EmailSender(String emailTo) {
		this.emailTo = emailTo;
	}
	private JSONFileReader file = new JSONFileReader();
	private String emailTo;
	private String messageUserWelcome = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\r\n" +
			"<html xmlns=\"http://www.w3.org/1999/xhtml\">\r\n" +
			"<head>\r\n" +
			"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\r\n" +
			"<title>Demystifying Email Design</title>\r\n" +
			"<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"/>\r\n" +
			"</head>\r\n" +
			"<body style=\"margin: 0; padding: 0;\">\r\n" +
			"	<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">	\r\n" +
			"		<tr>\r\n" +
			"			<td style=\"padding: 10px 0 30px 0;\">\r\n" +
			"				<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\" style=\"border: 1px solid #cccccc; border-collapse: collapse;\">\r\n" +
			"					<tr>\r\n" +
			"						<td align=\"center\" bgcolor=\"#70bbd9\" style=\"padding: 40px 0 30px 0; color: #153643; font-size: 28px; font-weight: bold; font-family: Arial, sans-serif;\">\r\n" +
			"							<img src=\"https://i.ibb.co/zRw8YmP/h1.gif\" alt=\"¡Bienvenido a IDEA de Alaya Digital Solutions!\" width=\"300\" height=\"230\" style=\"display: block;\" />" +
			"						</td>\r\n" +
			"					</tr>\r\n" +
			"					<tr>\r\n" +
			"						<td bgcolor=\"#ffffff\" style=\"padding: 40px 30px 40px 30px;\">\r\n" +
			"							<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\r\n" +
			"								<tr>\r\n" +
			"									<td style=\"color: #153643; font-family: Arial, sans-serif; font-size: 24px;\">\r\n" +
			"										<b>¡Descubre lo que podras hacer con IDEA!</b>\r\n" +
			"									</td>\r\n" +
			"								</tr>\r\n" +
			"								<tr>\r\n" +
			"									<td style=\"padding: 20px 0 30px 0; color: #153643; font-family: Arial, sans-serif; font-size: 16px; line-height: 20px;\">\r\n" +
			"										Como usuario del sistema, podr\u00e1s crear tus propias ideas y compartirlas con el resto de usuarios, adem\u00e1s comentar ideas de otros usuarios como critica constructiva.\r\n" +
			"									</td>\r\n" +
			"								</tr>\r\n" +
			"								<tr>\r\n" +
			"									<td>\r\n" +
			"										<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\r\n" +
			"											<tr>\r\n" +
			"												<td width=\"260\" valign=\"top\">\r\n" +
			"													<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\r\n" +
			"														<tr>\r\n" +
			"															<td>\r\n" +
			"																<img src=\"https://i.ibb.co/fNtM5rP/left.gif\" alt=\"\" width=\"100%\" height=\"140\" style=\"display: block;\" />\r\n" +
			"															</td>\r\n" +
			"														</tr>\r\n" +
			"														<tr>\r\n" +
			"															<td style=\"padding: 25px 0 0 0; color: #153643; font-family: Arial, sans-serif; font-size: 16px; line-height: 20px;\">\r\n" +
			"																Cada idea que publique los distintos usuarios podr\u00e1n comentar su idea generando feedback, esto con el objetivo de que usted vaya mejorando y evolucionando la idea.\r\n" +
			"															</td>\r\n" +
			"														</tr>\r\n" +
			"													</table>\r\n" +
			"												</td>\r\n" +
			"												<td style=\"font-size: 0; line-height: 0;\" width=\"20\">\r\n" +
			"													&nbsp;\r\n" +
			"												</td>\r\n" +
			"												<td width=\"260\" valign=\"top\">\r\n" +
			"													<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\r\n" +
			"														<tr>\r\n" +
			"															<td>\r\n" +
			"																<img src=\"https://i.ibb.co/dGKgtTX/right.gif\" alt=\"\" width=\"100%\" height=\"140\" style=\"display: block;\" />\r\n" +
			"															</td>\r\n" +
			"														</tr>\r\n" +
			"														<tr>\r\n" +
			"															<td style=\"padding: 25px 0 0 0; color: #153643; font-family: Arial, sans-serif; font-size: 16px; line-height: 20px;\">\r\n" +
			"																Llegada una fecha, todas las ideas ser\u00e1n evaluadas por un evaluador, quien eligir\u00e1 la idea ganadora. Procura formular una buena idea, ¡Podr\u00eda tu idea ser la ganadora!.\r\n" +
			"															</td>\r\n" +
			"														</tr>\r\n" +
			"													</table>\r\n" +
			"												</td>\r\n" +
			"											</tr>\r\n" +
			"										</table>\r\n" +
			"									</td>\r\n" +
			"								</tr>\r\n" +
			"							</table>\r\n" +
			"						</td>\r\n" +
			"					</tr>\r\n" +
			"					<tr>\r\n" +
			"						<td bgcolor=\"#ee4c50\" style=\"padding: 30px 30px 30px 30px;\">\r\n" +
			"							<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\r\n" +
			"								<tr>\r\n" +
			"									<td style=\"color: #ffffff; font-family: Arial, sans-serif; font-size: 14px;\" width=\"75%\">\r\n" +
			"										&reg; Alaya Digital Solutions, Santiago 2018<br/>\r\n" +
			"									</td>\r\n" +
			"									<td align=\"right\" width=\"25%\">\r\n" +
			"										<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\r\n" +
			"											<tr>\r\n" +
			"												<td style=\"font-family: Arial, sans-serif; font-size: 12px; font-weight: bold;\">\r\n" +
			"													</a>\r\n" +
			"												</td>\r\n" +
			"												<td style=\"font-size: 0; line-height: 0;\" width=\"20\">&nbsp;</td>\r\n" +
			"												<td style=\"font-family: Arial, sans-serif; font-size: 12px; font-weight: bold;\">\r\n" +
			"													</a>\r\n" +
			"												</td>\r\n" +
			"											</tr>\r\n" +
			"										</table>\r\n" +
			"									</td>\r\n" +
			"								</tr>\r\n" +
			"							</table>\r\n" +
			"						</td>\r\n" +
			"					</tr>\r\n" +
			"				</table>\r\n" +
			"			</td>\r\n" +
			"		</tr>\r\n" +
			"	</table>\r\n" +
			"</body>\r\n" +
			"</html>";

	public void sendMail() throws AddressException, MessagingException, IOException {
	   List<String> mailData = file.readEmailConfigJSON();
	   Properties props = new Properties();
	   props.put("mail.smtp.auth", "true");
	   props.put("mail.smtp.starttls.enable", "true");
	   props.put("mail.smtp.host", "smtp.gmail.com");
	   props.put("mail.smtp.port", "587");

	   Session session = Session.getInstance(props, new javax.mail.Authenticator() {
	      protected PasswordAuthentication getPasswordAuthentication() {
	         return new PasswordAuthentication(mailData.get(0), mailData.get(1));
	      }
	   });
	   Message msg = new MimeMessage(session);
	   msg.setFrom(new InternetAddress(mailData.get(0), false));

	   msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailTo));
	   msg.setSubject("Bienvenido a Idea de Alaya Digital Solutions");
	   msg.setSentDate(new Date());

	   MimeBodyPart messageBodyPart = new MimeBodyPart();
	   messageBodyPart.setContent(messageUserWelcome, "text/html");

	   Multipart multipart = new MimeMultipart();
	   multipart.addBodyPart(messageBodyPart);
	   msg.setContent(multipart);
	   Transport.send(msg);
	}

	public void run() {
		try {
			sendMail();
		}
		catch(Exception e) {
			System.out.println("Exception caught while sending message");
		}
	}
}
