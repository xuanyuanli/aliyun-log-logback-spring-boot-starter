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

        // 设置 MDC 字段（作为 SLS 独立索引字段上报）
        if (StringUtils.hasText(slsLogbackProperties.getMdcFields())) {
            appender.setMdcFields(slsLogbackProperties.getMdcFields());
        }

        // 设置日志来源
        if (StringUtils.hasText(slsLogbackProperties.getSource())) {
            appender.setSource(slsLogbackProperties.getSource());
        }

        // 设置时间格式和时区
        if (StringUtils.hasText(slsLogbackProperties.getTimeFormat())) {
            appender.setTimeFormat(slsLogbackProperties.getTimeFormat());
        }
        if (StringUtils.hasText(slsLogbackProperties.getTimeZone())) {
            appender.setTimeZone(slsLogbackProperties.getTimeZone());
        }

        // 设置是否包含 Location 字段
        if (slsLogbackProperties.getIncludeLocation() != null) {
            appender.setIncludeLocation(slsLogbackProperties.getIncludeLocation());
        }

        // 设置异常堆栈最大长度
        if (slsLogbackProperties.getMaxThrowable() != null) {
            appender.setMaxThrowable(slsLogbackProperties.getMaxThrowable());
        }

        // 设置时间精度
        if (StringUtils.hasText(slsLogbackProperties.getTimePrecision())) {
            appender.setTimePrecision(slsLogbackProperties.getTimePrecision());
        }

        // 设置性能调优参数
        if (slsLogbackProperties.getTotalSizeInBytes() != null) {
            appender.setTotalSizeInBytes(slsLogbackProperties.getTotalSizeInBytes());
        }
        if (slsLogbackProperties.getMaxBlockMs() != null) {
            appender.setMaxBlockMs(slsLogbackProperties.getMaxBlockMs());
        }
        if (slsLogbackProperties.getIoThreadCount() != null) {
            appender.setIoThreadCount(slsLogbackProperties.getIoThreadCount());
        }
        if (slsLogbackProperties.getBatchSizeThresholdInBytes() != null) {
            appender.setBatchSizeThresholdInBytes(slsLogbackProperties.getBatchSizeThresholdInBytes());
        }
        if (slsLogbackProperties.getBatchCountThreshold() != null) {
            appender.setBatchCountThreshold(slsLogbackProperties.getBatchCountThreshold());
        }
        if (slsLogbackProperties.getLingerMs() != null) {
            appender.setLingerMs(slsLogbackProperties.getLingerMs());
        }
        if (slsLogbackProperties.getRetries() != null) {
            appender.setRetries(slsLogbackProperties.getRetries());
        }

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
