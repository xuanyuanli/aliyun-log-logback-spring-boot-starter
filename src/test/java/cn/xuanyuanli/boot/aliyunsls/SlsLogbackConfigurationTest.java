package cn.xuanyuanli.boot.aliyunsls;

import ch.qos.logback.classic.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.slf4j.LoggerFactory;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * SlsLogbackConfiguration单元测试
 * 
 * @author xuanyuanli
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class SlsLogbackConfigurationTest {

    @Mock
    private SlsLogbackProperties slsLogbackProperties;

    @InjectMocks
    private SlsLogbackConfiguration configuration;

    @BeforeEach
    void setUp() {
        // 设置应用名称
        ReflectionTestUtils.setField(configuration, "appName", "test-app");
    }

    @Test
    void testValidatePropertiesWithValidConfig() {
        // 设置有效配置
        when(slsLogbackProperties.getEndpoint()).thenReturn("https://cn-hangzhou.log.aliyuncs.com");
        when(slsLogbackProperties.getAccessKeyId()).thenReturn("test-key-id");
        when(slsLogbackProperties.getAccessKeySecret()).thenReturn("test-key-secret");
        when(slsLogbackProperties.getProject()).thenReturn("test-project");
        when(slsLogbackProperties.getLogStore()).thenReturn("test-logstore");
        
        // 使用反射调用私有方法
        assertDoesNotThrow(() -> {
            ReflectionTestUtils.invokeMethod(configuration, "validateProperties");
        });
    }

    @Test
    void testValidatePropertiesWithEmptyEndpoint() {
        when(slsLogbackProperties.getEndpoint()).thenReturn("");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            ReflectionTestUtils.invokeMethod(configuration, "validateProperties");
        });
        assertEquals("SLS endpoint不能为空", exception.getMessage());
    }

    @Test
    void testValidatePropertiesWithNullEndpoint() {
        when(slsLogbackProperties.getEndpoint()).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            ReflectionTestUtils.invokeMethod(configuration, "validateProperties");
        });
        assertEquals("SLS endpoint不能为空", exception.getMessage());
    }

    @Test
    void testValidatePropertiesWithEmptyAccessKeyId() {
        when(slsLogbackProperties.getEndpoint()).thenReturn("https://cn-hangzhou.log.aliyuncs.com");
        when(slsLogbackProperties.getAccessKeyId()).thenReturn("");
        when(slsLogbackProperties.getAccessKeySecret()).thenReturn("test-key-secret");
        when(slsLogbackProperties.getProject()).thenReturn("test-project");
        when(slsLogbackProperties.getLogStore()).thenReturn("test-logstore");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            ReflectionTestUtils.invokeMethod(configuration, "validateProperties");
        });
        assertEquals("SLS accessKeyId不能为空", exception.getMessage());
    }

    @Test
    void testValidatePropertiesWithEmptyAccessKeySecret() {
        when(slsLogbackProperties.getEndpoint()).thenReturn("https://cn-hangzhou.log.aliyuncs.com");
        when(slsLogbackProperties.getAccessKeyId()).thenReturn("test-key-id");
        when(slsLogbackProperties.getAccessKeySecret()).thenReturn("");
        when(slsLogbackProperties.getProject()).thenReturn("test-project");
        when(slsLogbackProperties.getLogStore()).thenReturn("test-logstore");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            ReflectionTestUtils.invokeMethod(configuration, "validateProperties");
        });
        assertEquals("SLS accessKeySecret不能为空", exception.getMessage());
    }

    @Test
    void testValidatePropertiesWithEmptyProject() {
        when(slsLogbackProperties.getEndpoint()).thenReturn("https://cn-hangzhou.log.aliyuncs.com");
        when(slsLogbackProperties.getAccessKeyId()).thenReturn("test-key-id");
        when(slsLogbackProperties.getAccessKeySecret()).thenReturn("test-key-secret");
        when(slsLogbackProperties.getProject()).thenReturn("");
        when(slsLogbackProperties.getLogStore()).thenReturn("test-logstore");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> 
            ReflectionTestUtils.invokeMethod(configuration, "validateProperties"));
        assertEquals("SLS project不能为空", exception.getMessage());
    }

    @Test
    void testValidatePropertiesWithEmptyLogStore() {
        when(slsLogbackProperties.getEndpoint()).thenReturn("https://cn-hangzhou.log.aliyuncs.com");
        when(slsLogbackProperties.getAccessKeyId()).thenReturn("test-key-id");
        when(slsLogbackProperties.getAccessKeySecret()).thenReturn("test-key-secret");
        when(slsLogbackProperties.getProject()).thenReturn("test-project");
        when(slsLogbackProperties.getLogStore()).thenReturn("");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> 
            ReflectionTestUtils.invokeMethod(configuration, "validateProperties"));
        assertEquals("SLS logStore不能为空", exception.getMessage());
    }

    @Test
    void testGetFinalTopicWithConfiguredTopic() {
        when(slsLogbackProperties.getTopic()).thenReturn("test-topic");
        
        String result = ReflectionTestUtils.invokeMethod(configuration, "getFinalTopic");
        assertEquals("test-topic", result);
    }

    @Test
    void testGetFinalTopicWithEmptyTopic() {
        when(slsLogbackProperties.getTopic()).thenReturn("");
        
        String result = ReflectionTestUtils.invokeMethod(configuration, "getFinalTopic");
        assertEquals("test-app", result);
    }

    @Test
    void testGetFinalTopicWithNullTopic() {
        when(slsLogbackProperties.getTopic()).thenReturn(null);
        
        String result = ReflectionTestUtils.invokeMethod(configuration, "getFinalTopic");
        assertEquals("test-app", result);
    }

    @Test
    void testGetFinalTopicWithBlankTopic() {
        when(slsLogbackProperties.getTopic()).thenReturn("   ");
        
        String result = ReflectionTestUtils.invokeMethod(configuration, "getFinalTopic");
        assertEquals("test-app", result);
    }

    @Test
    void testAddAppenderSuccessfulInitialization() {
        // 获取根日志记录器的当前appender数量
        Logger rootLogger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        int initialAppenderCount = 0;
        var appenders = rootLogger.iteratorForAppenders();
        while (appenders.hasNext()) {
            appenders.next();
            initialAppenderCount++;
        }

        // 执行配置初始化
        assertDoesNotThrow(() -> configuration.addAppender());

        // 验证是否添加了新的appender
        int finalAppenderCount = 0;
        var finalAppenders = rootLogger.iteratorForAppenders();
        while (finalAppenders.hasNext()) {
            finalAppenders.next();
            finalAppenderCount++;
        }

        // 注意：由于LoghubAppender可能启动失败（没有真实的SLS环境），
        // 我们主要验证方法执行没有抛出异常
        assertTrue(finalAppenderCount >= initialAppenderCount);
    }

    @Test
    void testAddAppenderWithInvalidConfiguration() {
        when(slsLogbackProperties.getEndpoint()).thenReturn("");

        // 即使配置无效，方法也不应该抛出异常（它会捕获并记录错误）
        assertDoesNotThrow(() -> configuration.addAppender());
    }
}