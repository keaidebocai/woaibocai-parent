package top.woaibocai.qcloud.service.impl;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.model.StorageClass;
import com.qcloud.cos.region.Region;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import top.woaibocai.qcloud.service.CosService;
import top.woaibocai.qcloud.utils.ConstantPropertiesUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

@Service
public class CosServiceImpl implements CosService {
    @Override
    public String uploadThumbnail(MultipartFile file) throws IOException {
        //获取文件的原始名称，充当cos里的对象的key值

        //1.在文件名称里面添加随机的唯一的值
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        //2.把文件按照日期进行分类
        //2023/10/03/xxxxxx.jpg
        //获取当前日期 用joda工具类
        String datePath = new DateTime().toString("yyyy/MM/dd");
        //拼接
        String fileName = datePath + "/" + uuid + "-" + file.getOriginalFilename();


        // 调用 COS 接口之前必须保证本进程存在一个 COSClient 实例，如果没有则创建
        // 详细代码参见本页：简单操作 -> 创建 COSClient

        COSClient cosClient = createCOSClient();
        // 存储桶的命名格式为 BucketName-APPID，此处填写的存储桶名称必须为此格式
        String bucketName = ConstantPropertiesUtils.BUCKET;
        // 对象键(Key)是对象在存储桶中的唯一标识。
        String key = "/bcblog/thumbnail/" + fileName;
        // 这里创建一个 ByteArrayInputStream 来作为示例，实际中这里应该是您要上传的 InputStream 类型的流
        InputStream inputStream = new BufferedInputStream(file.getInputStream());

        ObjectMetadata objectMetadata = new ObjectMetadata();
//        // 上传的流如果能够获取准确的流长度，则推荐一定填写 content-length
//        // 如果确实没办法获取到，则下面这行可以省略，但同时高级接口也没办法使用分块上传了
        objectMetadata.setContentLength(file.getSize());
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, inputStream, objectMetadata);
        // 设置存储类型（如有需要，不需要请忽略此行代码）, 默认是标准(Standard), 低频(standard_ia)
        // 更多存储类型请参见 https://cloud.tencent.com/document/product/436/33417
        putObjectRequest.setStorageClass(StorageClass.Standard);
        try {
            PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
        } catch (CosServiceException e) {
            e.printStackTrace();
        } catch (CosClientException e) {
            e.printStackTrace();
        }
//        catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        // 确认本进程不再使用 cosClient 实例之后，关闭即可
        cosClient.shutdown();
        //需要把cos上传到的文件路径返回
        String url = "https://"
                + ConstantPropertiesUtils.BUCKET
                + ".cos."
                + ConstantPropertiesUtils.COS_REGION
                + ".myqcloud.com"
                + "/bcblog/thumbnail/"
                + fileName;
        return url;
    }

    @Override
    public List<String> userUpload(List<MultipartFile> files) {

        return null;
    }

    COSClient createCOSClient() {
        // 设置用户身份信息。
        // SECRETID 和 SECRETKEY 请登录访问管理控制台 https://console.cloud.tencent.com/cam/capi 进行查看和管理
        String secretId = ConstantPropertiesUtils.SECRET_ID;//用户的 SecretId，建议使用子账号密钥，授权遵循最小权限指引，降低使用风险。子账号密钥获取可参见 https://cloud.tencent.com/document/product/598/37140
        String secretKey = ConstantPropertiesUtils.SECRET_KEY;//用户的 SecretKey，建议使用子账号密钥，授权遵循最小权限指引，降低使用风险。子账号密钥获取可参见 https://cloud.tencent.com/document/product/598/37140
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        // ClientConfig 中包含了后续请求 COS 的客户端设置：
        ClientConfig clientConfig = new ClientConfig();
        // 设置 bucket 的地域
        // COS_REGION 请参见 https://cloud.tencent.com/document/product/436/6224
        clientConfig.setRegion(new Region(ConstantPropertiesUtils.COS_REGION));
        // 设置请求协议, http 或者 https
        // 5.6.53 及更低的版本，建议设置使用 https 协议
        // 5.6.54 及更高版本，默认使用了 https
        clientConfig.setHttpProtocol(HttpProtocol.http);
        // 以下的设置，是可选的：
        // 设置 socket 读取超时，默认 30s
        clientConfig.setSocketTimeout(30*1000);
        // 设置建立连接超时，默认 30s
        clientConfig.setConnectionTimeout(30*1000);
//        // 如果需要的话，设置 http 代理，ip 以及 port
//        clientConfig.setHttpProxyIp("httpProxyIp");
//        clientConfig.setHttpProxyPort(80);
        // 生成 cos 客户端。
        return new COSClient(cred,clientConfig);
    }
}
