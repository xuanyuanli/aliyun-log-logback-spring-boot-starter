package cn.xuanyuanli.boot.aliyunsls;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * 阿里云日志服务(SLS) Logback配置属性
 * 
 * @author xuanyuanli
 */
@ConfigurationProperties(prefix = "aliyun.sls")
@Data
@Validated
public class SlsLogbackProperties {
    
    /**
     * 阿里云SLS服务端点，例如：https://cn-hangzhou.log.aliyuncs.com
     */
    @NotBlank(message = "阿里云SLS端点不能为空")
    private String endpoint;
    
    /**
     * 阿里云访问密钥ID
     */
    @NotBlank(message = "阿里云访问密钥ID不能为空")
    private String accessKeyId;
    
    /**
     * 阿里云访问密钥Secret
     */
    @NotBlank(message = "阿里云访问密钥Secret不能为空")
    private String accessKeySecret;
    
    /**
     * SLS项目名称
     */
    @NotBlank(message = "SLS项目名称不能为空")
    private String project;
    
    /**
     * SLS日志库名称
     */
    @NotBlank(message = "SLS日志库名称不能为空")
    private String logStore;
    
    /**
     * 日志主题，如果未设置则使用spring.application.name
     */
    private String topic;

}
