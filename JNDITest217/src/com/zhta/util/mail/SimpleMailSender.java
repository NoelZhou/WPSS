package com.zhta.util.mail;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
/**   
 * 简单邮件（不带附件的邮件）发送器   
 */ 
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import com.zhta.bean.JDBConnection;


    
public class SimpleMailSender{  
	
	/**
	 * 发送带附件的邮件 
	 * @throws MessagingException 
	 * @throws UnsupportedEncodingException
	 */
	public void sendEmailWithAttachment(MailSenderInfo mailInfo) throws MessagingException{
		try {
			// 判断是否需要身份认证    
		    MyAuthenticator authenticator = null;    
		    Properties pro = mailInfo.getProperties();   
		    if (mailInfo.isValidate()) {    
		      // 如果需要身份认证，则创建一个密码验证器    
		      authenticator = new MyAuthenticator(mailInfo.getUserName(), mailInfo.getPassword());    
		    }   
		    // 根据邮件会话属性和密码验证器构造一个发送邮件的session    
		    Session sendMailSession = Session.getDefaultInstance(pro,authenticator); 
		  //  System.out.println("构造一个发送邮件的session");
		      
		    // 根据session创建一个邮件消息    
		    Message mailMessage = new MimeMessage(sendMailSession);    
		    // 创建邮件发送者地址    
		    
		    Address from = new InternetAddress(mailInfo.getFromAddress());
		    // 设置邮件消息的发送者    
		    mailMessage.setFrom(from);    
		    // 创建邮件的接收者地址，并设置到邮件消息中    
		    Address to = new InternetAddress(mailInfo.getToAddress());    
		    mailMessage.setRecipient(Message.RecipientType.TO,to);    
		    // 设置邮件消息的主题    
		    mailMessage.setSubject(mailInfo.getSubject());    
		    // 设置邮件消息发送的时间    
		    mailMessage.setSentDate(new Date());    
		    //附件
		    String attachFileNames = mailInfo.getAttachFileNames();
		  	BodyPart messageBodyPart = new MimeBodyPart();
		  	messageBodyPart.setText(mailInfo.getContent());
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);
			messageBodyPart = new MimeBodyPart();
			DataSource source = new FileDataSource(attachFileNames);
			messageBodyPart.setDataHandler(new DataHandler(source));
			//附件乱码处理
			try {
				messageBodyPart.setFileName(MimeUtility.encodeText(getFileName(attachFileNames)));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			multipart.addBodyPart(messageBodyPart);
			mailMessage.setContent(multipart);
			mailMessage.saveChanges();
			System.out.println("开始发送邮件……");
		    // 发送邮件    
		    Transport.send(mailMessage); 
		    System.out.println(mailInfo.getToAddress()+"发送成功！");
		} catch (AddressException e) {
			e.printStackTrace();
		}    
	      
	} 
	/**
	 * 发送邮件
	 * @param attachFileNames
	 */
	public void mailSend(String title,String attachFileNames){
		try {
			 Connection conn = null;
			 Statement st = null;
			 ResultSet rs = null;
			conn = JDBConnection.getConnect();
			st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			String sql = "select * from windpower_emailserver";
			String mailServerHost = "";
			String fromAddress = "";
			String toAddress = "";
		    String password = ""; 
		    String content = "";
			rs = st.executeQuery(sql);
			while(rs.next()){
				mailServerHost = rs.getString("serveraddr");
				fromAddress = rs.getString("emailid");
				password = rs.getString("passwd");
				content = rs.getString("assgin");
			}
			sql = "select emailaddr from windpower_useremail";
			rs = st.executeQuery(sql);
			while(rs.next()){
				toAddress = rs.getString("emailaddr");
				MailSenderInfo mailInfo = new MailSenderInfo();
				mailInfo.setMailServerHost(mailServerHost);    
			    mailInfo.setUserName(fromAddress);    
			    mailInfo.setPassword(password);  
			    mailInfo.setFromAddress(fromAddress);    
			    mailInfo.setToAddress(toAddress);    
			    mailInfo.setSubject(title);    
			    mailInfo.setContent(content);
			    mailInfo.setAttachFileNames(attachFileNames);
			    sendEmailWithAttachment(mailInfo);
			}
			rs.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取文件名称
	 * @param attachFileNames
	 * @return
	 */
	public static String getFileName(String attachFileNames){
		File tempFile =new File(attachFileNames.trim());  
        String fileName = tempFile.getName();  
        return fileName;
	}
       
    public static void main(String[] args){   
    	SimpleMailSender aaa = new SimpleMailSender();
    	aaa.mailSend("标题测试", "E:\\测试.sgu");
    }
    
}   
