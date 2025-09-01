package cn.xuanyuanli.boot.aliyunsls;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * SlsLogbackProperties单元测试
 * 
 * @author xuanyuanli
 */
class SlsLogbackPropertiesTest {

    private Validator validator;
    private SlsLogbackProperties properties;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        properties = new SlsLogbackProperties();
    }

    @Test
    void testValidProperties() {
        // 设置有效属性
        properties.setEndpoint("https://cn-hangzhou.log.aliyuncs.com");
        properties.setAccessKeyId("test-key-id");
        properties.setAccessKeySecret("test-key-secret");
        properties.setProject("test-project");
        properties.setLogStore("test-logstore");
        properties.setTopic("test-topic");

        Set<ConstraintViolation<SlsLogbackProperties>> violations = validator.validate(properties);
        assertTrue(violations.isEmpty(), "有效属性不应该有验证错误");
    }

    @Test
    void testEndpointValidation() {
        // 测试endpoint为空的情况
        properties.setAccessKeyId("test-key-id");
        properties.setAccessKeySecret("test-key-secret");
        properties.setProject("test-project");
        properties.setLogStore("test-logstore");

        Set<ConstraintViolation<SlsLogbackProperties>> violations = validator.validate(properties);
        assertEquals(1, violations.size());
        ConstraintViolation<SlsLogbackProperties> violation = violations.iterator().next();
        assertEquals("endpoint", violation.getPropertyPath().toString());
        assertEquals("阿里云SLS端点不能为空", violation.getMessage());
    }

    @Test
    void testAccessKeyIdValidation() {
        properties.setEndpoint("https://cn-hangzhou.log.aliyuncs.com");
        properties.setAccessKeySecret("test-key-secret");
        properties.setProject("test-project");
        properties.setLogStore("test-logstore");

        Set<ConstraintViolation<SlsLogbackProperties>> violations = validator.validate(properties);
        assertEquals(1, violations.size());
        ConstraintViolation<SlsLogbackProperties> violation = violations.iterator().next();
        assertEquals("accessKeyId", violation.getPropertyPath().toString());
        assertEquals("阿里云访问密钥ID不能为空", violation.getMessage());
    }

    @Test
    void testAccessKeySecretValidation() {
        properties.setEndpoint("https://cn-hangzhou.log.aliyuncs.com");
        properties.setAccessKeyId("test-key-id");
        properties.setProject("test-project");
        properties.setLogStore("test-logstore");

        Set<ConstraintViolation<SlsLogbackProperties>> violations = validator.validate(properties);
        assertEquals(1, violations.size());
        ConstraintViolation<SlsLogbackProperties> violation = violations.iterator().next();
        assertEquals("accessKeySecret", violation.getPropertyPath().toString());
        assertEquals("阿里云访问密钥Secret不能为空", violation.getMessage());
    }

    @Test
    void testProjectValidation() {
        properties.setEndpoint("https://cn-hangzhou.log.aliyuncs.com");
        properties.setAccessKeyId("test-key-id");
        properties.setAccessKeySecret("test-key-secret");
        properties.setLogStore("test-logstore");

        Set<ConstraintViolation<SlsLogbackProperties>> violations = validator.validate(properties);
        assertEquals(1, violations.size());
        ConstraintViolation<SlsLogbackProperties> violation = violations.iterator().next();
        assertEquals("project", violation.getPropertyPath().toString());
        assertEquals("SLS项目名称不能为空", violation.getMessage());
    }

    @Test
    void testLogStoreValidation() {
        properties.setEndpoint("https://cn-hangzhou.log.aliyuncs.com");
        properties.setAccessKeyId("test-key-id");
        properties.setAccessKeySecret("test-key-secret");
        properties.setProject("test-project");

        Set<ConstraintViolation<SlsLogbackProperties>> violations = validator.validate(properties);
        assertEquals(1, violations.size());
        ConstraintViolation<SlsLogbackProperties> violation = violations.iterator().next();
        assertEquals("logStore", violation.getPropertyPath().toString());
        assertEquals("SLS日志库名称不能为空", violation.getMessage());
    }

    @Test
    void testTopicIsOptional() {
        // topic是可选的，不设置topic应该不会有验证错误
        properties.setEndpoint("https://cn-hangzhou.log.aliyuncs.com");
        properties.setAccessKeyId("test-key-id");
        properties.setAccessKeySecret("test-key-secret");
        properties.setProject("test-project");
        properties.setLogStore("test-logstore");
        // 不设置topic

        Set<ConstraintViolation<SlsLogbackProperties>> violations = validator.validate(properties);
        assertTrue(violations.isEmpty(), "topic是可选的，不应该有验证错误");
    }

    @Test
    void testMultipleValidationErrors() {
        // 不设置任何必要属性，应该有多个验证错误
        Set<ConstraintViolation<SlsLogbackProperties>> violations = validator.validate(properties);
        assertEquals(5, violations.size(), "应该有5个验证错误（所有必要字段都为空）");
    }

    @Test
    void testBlankStringsAreInvalid() {
        // 测试空白字符串也被认为是无效的
        properties.setEndpoint("  ");
        properties.setAccessKeyId("");
        properties.setAccessKeySecret("   ");
        properties.setProject("");
        properties.setLogStore("  ");

        Set<ConstraintViolation<SlsLogbackProperties>> violations = validator.validate(properties);
        assertEquals(5, violations.size(), "空白字符串应该被认为是无效的");
    }
}