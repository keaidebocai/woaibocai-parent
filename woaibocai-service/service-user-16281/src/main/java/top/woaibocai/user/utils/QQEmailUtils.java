package top.woaibocai.user.utils;

import jakarta.activation.DataHandler;
import jakarta.activation.FileDataSource;
import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import lombok.extern.slf4j.Slf4j;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import top.woaibocai.model.entity.email.Content;

import java.io.UnsupportedEncodingException;
import java.time.Duration;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
            String stringBuilder = "<!DOCTYPE html>\n<html>\n<head>\n<meta charset=\"utf-8\">\n<title>菠菜的小窝</title>\n</head>\n<body>\n " + html + "\n</body>\n</html>";
            helper.setFrom(username,"菠菜的时光邮局");
            helper.setTo(to);
            helper.setSubject(title);
            helper.setSentDate(new Date());
            helper.setText(stringBuilder,true);
            javaMailSender.send(mailMessage);
        } catch (MessagingException e) {
            log.error("发送邮件失败",e);
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            log.error("发送邮件失败",e);
            return false;
        }
        return true;
    }

    public Boolean noticeDeliverySendHtml(Content content) {
        MimeMessage noticeMailMessage = javaMailSender.createMimeMessage();
        // 需要借助 Helper 类
        MimeMessageHelper helper = new MimeMessageHelper(noticeMailMessage);
        try {
            String time = content.getDeliveryTime().toString().substring(0, 10).replaceFirst("-","年").replaceFirst("-","月");
            String stringBuilder = "<!DOCTYPE html>\n<html>\n<head>\n<meta charset=\"utf-8\">\n<title>菠菜的小窝-时光邮局</title>\n</head>\n<body>\n "
                    + "<div style='background-color: #a0cfff;text-align: center;display: flex;justify-content: center;align-items: center;height:100px;'>\n"
                    + "<h1 style='color: #409EFF;'>菠菜的小窝</h1>\n"
                    + "</div>"
                    + "<div style='width:100%;padding:10px 0px;'>"
                    + "<h2>亲爱的用户，您好！</h2>\n"
                    + "在此很高兴地通知您，菠菜小邮差已将您于" + time + "日封缄的信件<p style='color: red'>《" + content.getTitle() +"》</p>送达收件人！这封信承载着您的思念与真情，已经顺利到达您指定的收件人手中。\n"
                    + "感谢您选择【菠菜的小窝-时光邮箱】，让我们有机会帮助传达您的心意。<br>"
                    + "<div style='background-color: #d9ecff;border-radius: 5px;text-align: center;border: 1px solid #337ecc;display: flex;justify-content: center;align-items: center;height:60px;margin: 20px 0px;'>\n"
                    + "<a style='text-decoration: none;color: #409EFF;' href='https://www.likebocai.com/letter/text/" + content.getId()  +  "' target='_blank' >\n"
                    + "➡️&nbsp;去菠菜的小窝查看您寄出的这封信\n"
                    + "</a>\n"
                    + "</div>\n"
                    + "祝好！<br>\n"
                    + "菠菜的小窝敬上\n"
                    + "<hr style='width: 100%;border-top: 1px solid #005293;'>\n"
                    + "如果您点击上方的\"➡️&nbsp;去菠菜的小窝查看您寄出的这封信\"有问题，可将下面的链接复制粘贴到浏览器中打开："
                    + "<a href='https://www.likebocai.com/letter/text/" + content.getId() + "' target='_blank' >"
                    + "https://www.likebocai.com/letter/text/" + content.getId()
                    + "</a>\n"
                    + "</div>\n"
                    + "\n</body>\n</html>";
            helper.setFrom(username,"菠菜的小窝-时光邮局");
            helper.setTo(content.getSenderEmail());
            helper.setSubject("【菠菜的小窝-时光邮局】🌹邮件到达通知");
            helper.setSentDate(new Date());
            helper.setText(stringBuilder,true);
            javaMailSender.send(noticeMailMessage);
        } catch (MessagingException e) {
            log.error("发送邮件失败",e);
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            log.error("发送邮件失败",e);
            return false;
        }
        return true;
    }

    public Boolean deliverySendHtml(Content content) {
        MimeMessage deliveryMailMessage = javaMailSender.createMimeMessage();
        // 需要借助 Helper 类
        MimeMessageHelper helper = new MimeMessageHelper(deliveryMailMessage);
        try {
            // 将 md 转为 html
            Parser parser = Parser.builder().build();
            Node document = parser.parse(content.getContent());
            HtmlRenderer renderer = HtmlRenderer.builder().build();
            String htmlContent = renderer.render(document);
            // 计算时间
            String time = TimeUtils.localDateTimeBetween(content.getWritingEmailTime(), content.getDeliveryTime());
            String stringBuilder = "<!DOCTYPE html>\n<html>\n<head>\n<meta charset=\"utf-8\">\n<title>菠菜的小窝-时光邮局</title>\n</head>\n<body>\n "
                    + "<div style='width:100%;'>"
                    + "<h2>【菠菜的小窝-时光邮局】</h2>\n"
//                    + "寄信人：" + content.getSenderEmail()
                    + "<br>书写于&nbsp;" + content.getWritingEmailTime().toString().replace("T"," ")
                    + "<br>经历了" + time
                    + "<br>于&nbsp;" + content.getDeliveryTime().toString().replace("T"," ") + "&emsp;投递\n"
                    + "<div style='display: flex;justify-content: center;align-items: center;height: 60px;'>\n"
                    + "<hr style='width: calc(60% - 100px);border-top: 1px solid #7e8e9b;margin-right: 10px;'>\n"
                    + "<p style='color: #7e8e9b;margin-left: 5px;'>💌书信内容</p>\n"
                    + "<hr style='width: calc(60% - 100px);border-top: 1px solid #7e8e9b;margin-left: 10px;'>\n"
                    + "</div>\n"
                    + htmlContent
                    + "\n<div style='display: flex;justify-content: center;align-items: center;height: 60px;'>\n"
                    + "<hr style='width: calc(60% - 100px);border-top: 1px solid #7e8e9b;margin-right: 10px;'>\n"
                    + "<p style='color: #7e8e9b;margin-left: 5px;'>"
                    + "🖊️就此搁笔"
                    + "</p>\n"
                    + "<hr style='width: calc(60% - 100px);border-top: 1px solid #7e8e9b;margin-left: 10px;'>\n"
                    + "</div>\n"
                    + "</div>\n"
                    + "<h2><strong>请勿直接回复此邮件</strong></h2>\n"
                    + "您可以在"+ "<a href='https://www.likebocai.com/letter/text/" + content.getId() + "' target='_blank' >"
                    + "https://www.likebocai.com/letter/text/" + content.getId()
                    + "</a>"
                    + "<br>查看精美的内容与图片\n"
                    + "这是来自" + "<a href='https://www.likebocai.com' target='_blank' >"
                    + "https://www.likebocai.com"
                    + "</a>的服务，您可以在菠菜的小窝-时光邮局中寄一封信给未来的自己，或在意的人。让你的话语穿越时空，带着爱和记忆，安静的抵达未来！\n"
                    + "\n</body>\n</html>";
            helper.setFrom(username,"菠菜的小窝-时光邮局");
            helper.setTo(content.getRecipientEmail());
            helper.setSubject("🌟" + content.getTitle());
            helper.setSentDate(new Date());
            helper.setText(stringBuilder,true);
            javaMailSender.send(deliveryMailMessage);
        } catch (MessagingException e) {
            log.error("发送邮件失败",e);
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            log.error("发送邮件失败",e);
            return false;
        }
        return true;
    }
}
