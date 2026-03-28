package cn.xuanyuanli.boot.aliyunsls;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * SlsLogbackProperties单元测试
 * 
 * @author xuanyuanli
 */
class SlsLogbackPropertiesTest {

    private SlsLogbackProperties properties;

    @BeforeEach
    void setUp() {
        properties = new SlsLogbackProperties();
    }

    @Test
    void testSetAndGetProperties() {
        // 设置所有属性
        properties.setEndpoint("https://cn-hangzhou.log.aliyuncs.com");
        properties.setAccessKeyId("test-key-id");
        properties.setAccessKeySecret("test-key-secret");
        properties.setProject("test-project");
        properties.setLogStore("test-logstore");
        properties.setTopic("test-topic");

        // 验证所有属性都能正确设置和获取
        assertEquals("https://cn-hangzhou.log.aliyuncs.com", properties.getEndpoint());
        assertEquals("test-key-id", properties.getAccessKeyId());
        assertEquals("test-key-secret", properties.getAccessKeySecret());
        assertEquals("test-project", properties.getProject());
        assertEquals("test-logstore", properties.getLogStore());
        assertEquals("test-topic", properties.getTopic());
    }

    @Test
    void testSetAndGetExtendedProperties() {
        // 设置扩展属性 - 日志来源与格式
        properties.setSource("192.168.1.1");
        properties.setTimeFormat("yyyy-MM-dd HH:mm:ss");
        properties.setTimeZone("Asia/Shanghai");
        properties.setIncludeLocation(false);
        properties.setMaxThrowable(1000);
        properties.setTimePrecision("ms");

        // 设置扩展属性 - 性能调优
        properties.setTotalSizeInBytes(209715200);
        properties.setMaxBlockMs(0);
        properties.setIoThreadCount(4);
        properties.setBatchSizeThresholdInBytes(1048576);
        properties.setBatchCountThreshold(2048);
        properties.setLingerMs(1000);
        properties.setRetries(5);

        // 验证扩展属性
        assertEquals("192.168.1.1", properties.getSource());
        assertEquals("yyyy-MM-dd HH:mm:ss", properties.getTimeFormat());
        assertEquals("Asia/Shanghai", properties.getTimeZone());
        assertEquals(false, properties.getIncludeLocation());
        assertEquals(1000, properties.getMaxThrowable());
        assertEquals("ms", properties.getTimePrecision());

        assertEquals(209715200, properties.getTotalSizeInBytes());
        assertEquals(0, properties.getMaxBlockMs());
        assertEquals(4, properties.getIoThreadCount());
        assertEquals(1048576, properties.getBatchSizeThresholdInBytes());
        assertEquals(2048, properties.getBatchCountThreshold());
        assertEquals(1000, properties.getLingerMs());
        assertEquals(5, properties.getRetries());
    }

    @Test
    void testDefaultValues() {
        // 验证所有属性的默认值都是null
        assertNull(properties.getEndpoint());
        assertNull(properties.getAccessKeyId());
        assertNull(properties.getAccessKeySecret());
        assertNull(properties.getProject());
        assertNull(properties.getLogStore());
        assertNull(properties.getTopic());

        // 验证扩展属性的默认值都是null
        assertNull(properties.getSource());
        assertNull(properties.getTimeFormat());
        assertNull(properties.getTimeZone());
        assertNull(properties.getIncludeLocation());
        assertNull(properties.getMaxThrowable());
        assertNull(properties.getTimePrecision());

        assertNull(properties.getTotalSizeInBytes());
        assertNull(properties.getMaxBlockMs());
        assertNull(properties.getIoThreadCount());
        assertNull(properties.getBatchSizeThresholdInBytes());
        assertNull(properties.getBatchCountThreshold());
        assertNull(properties.getLingerMs());
        assertNull(properties.getRetries());
    }

    @Test
    void testTopicCanBeNull() {
        // 设置其他必要属性
        properties.setEndpoint("https://cn-hangzhou.log.aliyuncs.com");
        properties.setAccessKeyId("test-key-id");
        properties.setAccessKeySecret("test-key-secret");
        properties.setProject("test-project");
        properties.setLogStore("test-logstore");
        // topic保持为null

        assertNull(properties.getTopic());
    }

    @Test
    void testTopicCanBeEmpty() {
        // 设置其他必要属性
        properties.setEndpoint("https://cn-hangzhou.log.aliyuncs.com");
        properties.setAccessKeyId("test-key-id");
        properties.setAccessKeySecret("test-key-secret");
        properties.setProject("test-project");
        properties.setLogStore("test-logstore");
        properties.setTopic("");

        assertEquals("", properties.getTopic());
    }

    @Test
    void testPropertiesCanBeEmptyStrings() {
        // 测试属性可以设置为空字符串（验证逻辑在Configuration中处理）
        properties.setEndpoint("");
        properties.setAccessKeyId("");
        properties.setAccessKeySecret("");
        properties.setProject("");
        properties.setLogStore("");
        properties.setTopic("");

        assertEquals("", properties.getEndpoint());
        assertEquals("", properties.getAccessKeyId());
        assertEquals("", properties.getAccessKeySecret());
        assertEquals("", properties.getProject());
        assertEquals("", properties.getLogStore());
        assertEquals("", properties.getTopic());
    }
}