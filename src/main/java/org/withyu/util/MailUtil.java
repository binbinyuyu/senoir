package org.withyu.util;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailUtil implements Runnable {
    private String email;// 收件人邮箱
    private String code;// 激活码
    private String type;// 方式：register或resetpsd

    public MailUtil(String email, String code, String type) {
        this.email = email;
        this.code = code;
        this.type = type;
    }

    public void run() {
        //System.out.println("邮件开始发送!");
        // 1.创建连接对象javax.mail.Session
        // 2.创建邮件对象 javax.mail.Message
        // 3.发送一封激活邮件
        String from = "chyandgb@163.com";// 发件人电子邮箱
        String host = "smtp.163.com"; // 指定发送邮件的主机smtp.qq.com(QQ)|smtp.163.com(网易)

        Properties properties = System.getProperties();// 获取系统属性

        properties.setProperty("mail.smtp.host", host);// 设置邮件服务器
        properties.setProperty("mail.smtp.auth", "true");// 打开认证

        try {
//			//QQ邮箱需要下面这段代码，163邮箱不需要
//			MailSSLSocketFactory sf = new MailSSLSocketFactory();
//			sf.setTrustAllHosts(true);
//			properties.put("mail.smtp.ssl.enable", "true");
//			properties.put("mail.smtp.ssl.socketFactory", sf);

            // 1.获取默认session对象
            Session session = Session.getDefaultInstance(properties, new Authenticator() {
                public PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("chyandgb@163.com", "zxcZXC123"); // 发件人邮箱账号、授权码
                }
            });

            // 2.创建邮件对象
            Message message = new MimeMessage(session);
            // 2.1设置发件人
            message.setFrom(new InternetAddress(from));
            // 2.2设置接收人
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            // 2.3设置邮件主题
            if (type.equals("register"))
                message.setSubject("WithYou账号激活");
            else
                message.setSubject("WithYou密码重置");
            // 2.4设置邮件内容
            String content = null;
            if (type.equals("register")) {//激活
//                content = "<html><head></head><body><h1>这是一封激活邮件,激活请点击以下链接</h1><h3><a href='http://localhost:8888/withyu_war_exploded/user/active?code="
//                        + code + "'>http://localhost:8888/withyou/user/active?code=" + code
//                        + "</href></h3></body></html>";
                content = "<html><head></head><body><h1>这是一封激活邮件,激活请点击以下链接</h1><h3><a href='http://122.51.82.131/withyou/ActivateServlet?code="
                        + code + "'>http://122.51.82.131/withyou/user/active?code=" + code
                        + "</href></h3></body></html>";
            } else {//忘记密码
//                content = "<html><head></head><body><h1>这是一封重置密码的邮件,请点击以下链接</h1><h3><a href='http://localhost:8888/withyu_war_exploded/forget.html?code="
//                        + code + "&step=2'>http://localhost:8888/withyou/forget.html?code=" + code
//                        + "&step=2</href></h3></body></html>";
                content = "<html><head></head><body><h1>这是一封重置密码的邮件,请点击以下链接</h1><h3><a href='http://122.51.82.131/withyou/forget.html?code="
                        + code + "&step=2'>http://122.51.82.131/withyou/forget.html?code=" + code
                        + "&step=2</href></h3></body></html>";
            }

            message.setContent(content, "text/html;charset=UTF-8");
            // 3.发送邮件
            Transport.send(message);
            System.out.println("邮件成功发送!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
