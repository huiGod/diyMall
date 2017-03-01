package cn._51app.util;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


/**
 * @author Administrator
 * 邮件发送 qq smtp
 */
public class SMTPMailUtil {
	static int port = 25;
	static String server = "smtp.qq.com";
	static String from = "2912907596@qq.com";
	static String user = "2912907596";
	static String password = "szwcl2015";
	public static void sendEmail(String email, String subject, String body) {
		try {
			Properties props = new Properties();
			props.put("mail.smtp.host", server);
			props.put("mail.smtp.port", String.valueOf(port));
			props.put("mail.smtp.auth", "true");
			Transport transport = null;
			Session session = Session.getDefaultInstance(props, null);
			transport = session.getTransport("smtp");
			try {
				transport.connect(server, user, password);
			} catch (Exception e) {
				e.printStackTrace();
			}
			MimeMessage msg = new MimeMessage(session);
			msg.setSentDate(new Date());
			InternetAddress fromAddress = new InternetAddress(from);
			msg.setFrom(fromAddress);
			InternetAddress[] toAddress = new InternetAddress[1];
			toAddress[0] = new InternetAddress(email.trim());
			msg.setRecipients(Message.RecipientType.TO, toAddress);
			msg.setSubject(subject, "UTF-8");	
			//msg.setText(body, "UTF-8");
			msg.setContent(body, "text/html;charset=utf-8");		   
			msg.saveChanges();
			transport.sendMessage(msg, msg.getAllRecipients());
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
	/*
	public static void main(String[] args) {
		for (int i = 0; i < 2; i++) {
			SMTPMailUtil.sendEmail("783878156@qq.com", "测试邮件！", "136");
		}
	}
	*/
}
