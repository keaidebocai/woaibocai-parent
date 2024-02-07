package top.woaibocai.blog.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @program: woaibocai-parent
 * @description: 真是ip获取工具
 * @author: woaibocai
 * @create: 2024-01-13 17:16
 **/
public class IPutil {
    private static final String LOCAL_IP = "127.0.0.1";
    private static final String LOCAL_HOST = "localhost";
    /**
     * 获取 IP 地址
     *
     * @param ip
     * @return
     */
    public String getIpAddr(String ip) {
        ip = ip.substring(0,ip.indexOf(","));
        // 本机访问
        if (LOCAL_IP.equalsIgnoreCase(ip) || LOCAL_HOST.equalsIgnoreCase(ip) || "0:0:0:0:0:0:0:1".equalsIgnoreCase(ip)) {
            // 根据网卡取本机配置的 IP
            try {
                InetAddress localHost = InetAddress.getLocalHost();
                ip = localHost.getHostAddress();
            } catch (UnknownHostException e) {

                e.printStackTrace();
            }
            finally {
                System.out.println(ip+"=============================================================");
            }
        }

        // 对于通过多个代理的情况，第一个 IP 为客户端真实 IP,多个 IP 按照','分割
        if (ip != null && ip.length() > 15) {
            if (ip.indexOf(",") > 15) {
                ip = ip.substring(0, ip.indexOf(","));
            }
        }

        return ip;
    }
}
