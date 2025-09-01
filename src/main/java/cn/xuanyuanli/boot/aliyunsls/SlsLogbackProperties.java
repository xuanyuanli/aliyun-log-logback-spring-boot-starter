package cn.xuanyuanli.boot.aliyunsls;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 阿里云日志服务(SLS) Logback配置属性
 * 
 * @author xuanyuanli
 */
@ConfigurationProperties(prefix = "aliyun.sls")
@Data
public class SlsLogbackProperties {
    
    /**
     * 阿里云SLS服务端点，例如：https://cn-hangzhou.log.aliyuncs.com
     */
    private String endpoint;
    
    /**
     * 阿里云访问密钥ID
     */
    private String accessKeyId;
    
    /**
     * 阿里云访问密钥Secret
     */
    private String accessKeySecret;
    
    /**
     * SLS项目名称
     */
    private String project;
    
    /**
     * SLS日志库名称
     */
    private String logStore;
    
    /**
     * 日志主题，如果未设置则使用spring.application.name
     */
    private String topic;

}
