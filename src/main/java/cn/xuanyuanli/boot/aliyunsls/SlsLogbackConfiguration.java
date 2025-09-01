package cn.xuanyuanli.boot.aliyunsls;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.aliyun.openservices.log.logback.LoghubAppender;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.util.StringUtils;

/**
 * 阿里云日志服务(SLS) Logback自动配置类
 * 
 * @author xuanyuanli
 */
@Slf4j
@AutoConfiguration
@ConditionalOnProperty(prefix = "aliyun.sls", name = "endpoint")
@EnableConfigurationProperties(SlsLogbackProperties.class)
public class SlsLogbackConfiguration {

    @Autowired
    private SlsLogbackProperties slsLogbackProperties;

    @Value("${spring.application.name:unknown}")
    private String appName;

    /**
     * 初始化并添加SLS Logback Appender
     */
    @PostConstruct
    public void addAppender() {
        try {
            validateProperties();
            
            LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
            
            LoghubAppender<ILoggingEvent> appender = createAndConfigureAppender(loggerContext);
            
            if (!appender.isStarted()) {
                log.error("Failed to start SLS LoghubAppender");
                return;
            }
            
            attachAppenderToRootLogger(appender);
            
            String finalTopic = getFinalTopic();
            log.debug("SLS Logback配置初始化成功 - endpoint: {}, project: {}, logStore: {}, topic: {}", 
                    slsLogbackProperties.getEndpoint(),
                    slsLogbackProperties.getProject(), 
                    slsLogbackProperties.getLogStore(), 
                    finalTopic);
                    
        } catch (Exception e) {
            log.error("SLS Logback配置初始化失败", e);
        }
    }
    
    /**
     * 验证配置属性
     */
    private void validateProperties() {
        if (!StringUtils.hasText(slsLogbackProperties.getEndpoint())) {
            throw new IllegalArgumentException("SLS endpoint不能为空");
        }
        if (!StringUtils.hasText(slsLogbackProperties.getAccessKeyId())) {
            throw new IllegalArgumentException("SLS accessKeyId不能为空");
        }
        if (!StringUtils.hasText(slsLogbackProperties.getAccessKeySecret())) {
            throw new IllegalArgumentException("SLS accessKeySecret不能为空");
        }
        if (!StringUtils.hasText(slsLogbackProperties.getProject())) {
            throw new IllegalArgumentException("SLS project不能为空");
        }
        if (!StringUtils.hasText(slsLogbackProperties.getLogStore())) {
            throw new IllegalArgumentException("SLS logStore不能为空");
        }
    }
    
    /**
     * 创建并配置LoghubAppender
     */
    private LoghubAppender<ILoggingEvent> createAndConfigureAppender(LoggerContext loggerContext) {
        LoghubAppender<ILoggingEvent> appender = new LoghubAppender<>();
        appender.setEndpoint(slsLogbackProperties.getEndpoint());
        appender.setAccessKeyId(slsLogbackProperties.getAccessKeyId());
        appender.setAccessKeySecret(slsLogbackProperties.getAccessKeySecret());
        appender.setProject(slsLogbackProperties.getProject());
        appender.setLogStore(slsLogbackProperties.getLogStore());
        appender.setTopic(getFinalTopic());
        appender.setContext(loggerContext);
        appender.start();
        return appender;
    }
    
    /**
     * 将appender附加到根日志记录器
     */
    private void attachAppenderToRootLogger(LoghubAppender<ILoggingEvent> appender) {
        Logger rootLogger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        rootLogger.addAppender(appender);
    }
    
    /**
     * 获取最终的topic值
     */
    private String getFinalTopic() {
        return StringUtils.hasText(slsLogbackProperties.getTopic()) ? 
                slsLogbackProperties.getTopic() : appName;
    }

}
