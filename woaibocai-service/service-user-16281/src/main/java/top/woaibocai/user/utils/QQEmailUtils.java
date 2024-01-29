package top.woaibocai.user.utils;

import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @program: woaibocai-parent
 * @description: qq邮箱工具类
 * @author: woaibocai
 * @create: 2024-01-29 12:13
 **/
@Component
@Slf4j
public class QQEmailUtils {
    @Resource
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    String username;
    public Boolean sendHtml(String title,String html,String to) {
        MimeMessage mailMessage = javaMailSender.createMimeMessage();
        // 需要借助 Helper 类
        MimeMessageHelper helper = new MimeMessageHelper(mailMessage);
        try {
            helper.setFrom(username);
            helper.setTo(to);
            helper.setSubject(title);
            helper.setSentDate(new Date());
            helper.setText(html,true);
            javaMailSender.send(mailMessage);
        } catch (MessagingException e) {
            log.error("发送邮件失败",e);
            return false;
        }
        return true;
    }
}
