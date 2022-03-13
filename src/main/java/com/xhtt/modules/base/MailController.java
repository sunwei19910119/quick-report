package com.xhtt.modules.base;


import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.internet.MimeMessage;

/**
 * APP测试接口
 */
@RestController
@RequestMapping("/app")
@Api("APP测试接口")
public class MailController {
    @Value("${spring.mail.username}")
    private String from;

    @Autowired
    private JavaMailSender mailSender;

    @GetMapping("/mail")
    public void mail() throws Exception {
//        SimpleMailMessage message = new SimpleMailMessage();
//        //邮件设置
//        message.setSubject("麻卫？");
//        message.setText("麻卫大沙皮？麻卫大沙皮！");
//        message.setTo("xxxxxxx@139.com", "359232046@qq.com");
//        message.setFrom("麻瓜");
//        mailSender.send(message);

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        //邮件主题
        helper.setSubject("这是一个邮件啊");

        //邮件内容
        helper.setText("<html>" +
                "<body>" +
                "<h1>葬礼邀请函</h1>" +
                "<h2>欢迎光临</h2>" +
                "<h3>欢迎光临</h3>" +
                "<img src='cid:" + "image1" + "'></img>" +
                "<body>" +
                "</html>", true);
        helper.setTo(new String[]{"359232046@qq.com"});
        //文本中添加图片
        helper.addInline("image1", new FileSystemResource("C:\\Users\\xhtt\\Desktop\\ma.jpg"));
        helper.setFrom("feipcfeipc@aliyun.com");
        //附件添加图片
//        helper.addAttachment("1.jpg",new File("D:\\images\\spring\\1.jpg"));
        //附件添加word文档
//        helper.addAttachment("哈哈哈.docx",new File("D:\\images\\spring\\哈哈哈.docx"));

        mailSender.send(mimeMessage);
        System.out.println("发送完成");
    }

}
