package top.woaibocai.qcloud.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class ConstantPropertiesUtils implements InitializingBean {
    //读取配置文件内容
    //密钥id
    @Value("${qcloud.cos.file.secretId}")
    private String secretId;
    //密钥密码
    @Value("${qcloud.cos.file.secretKey}")
    private String secretKey;
    //cos存储桶的地域
    @Value("${qcloud.cos.file.COS_REGION}")
    private String cosRegion;
    //存储桶的名称
    @Value("${qcloud.cos.file.bucket}")
    private String bucket;
    @Value("${qcloud.cos.file.url}")
    private String url;

    //定义公开的静态量
    //密钥id
    public static String SECRET_ID;
    //密钥密码
    public static String SECRET_KEY;
    //cos存储桶的地域
    public static String COS_REGION;
    //存储桶的名称
    public static String BUCKET;
    public static String URL;

    @Override
    public void afterPropertiesSet() throws Exception{
        //密钥id
        SECRET_ID = secretId;
        //密钥密码
        SECRET_KEY = secretKey;
        //cos存储桶的地域
        COS_REGION = cosRegion;
        //存储桶的名称
        BUCKET = bucket;
        URL = url;
    }

}
