package cn.xuanyuanli.boot.aliyunsls;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * SLS Logback自动配置集成测试
 * 
 * @author xuanyuanli
 */
class SlsLogbackAutoConfigurationIntegrationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(SlsLogbackConfiguration.class));

    @Test
    void testAutoConfigurationEnabledWithValidProperties() {
        contextRunner
                .withPropertyValues(
                        "aliyun.sls.endpoint=https://cn-hangzhou.log.aliyuncs.com",
                        "aliyun.sls.access-key-id=test-key-id",
                        "aliyun.sls.access-key-secret=test-key-secret",
                        "aliyun.sls.project=test-project",
                        "aliyun.sls.log-store=test-logstore",
                        "spring.application.name=test-app"
                )
                .run(context -> {
                    assertThat(context).hasSingleBean(SlsLogbackConfiguration.class);
                    assertThat(context).hasSingleBean(SlsLogbackProperties.class);
                    
                    SlsLogbackProperties properties = context.getBean(SlsLogbackProperties.class);
                    assertThat(properties.getEndpoint()).isEqualTo("https://cn-hangzhou.log.aliyuncs.com");
                    assertThat(properties.getAccessKeyId()).isEqualTo("test-key-id");
                    assertThat(properties.getAccessKeySecret()).isEqualTo("test-key-secret");
                    assertThat(properties.getProject()).isEqualTo("test-project");
                    assertThat(properties.getLogStore()).isEqualTo("test-logstore");
                });
    }

    @Test
    void testAutoConfigurationEnabledWithTopic() {
        contextRunner
                .withPropertyValues(
                        "aliyun.sls.endpoint=https://cn-hangzhou.log.aliyuncs.com",
                        "aliyun.sls.access-key-id=test-key-id",
                        "aliyun.sls.access-key-secret=test-key-secret",
                        "aliyun.sls.project=test-project",
                        "aliyun.sls.log-store=test-logstore",
                        "aliyun.sls.topic=custom-topic",
                        "spring.application.name=test-app"
                )
                .run(context -> {
                    assertThat(context).hasSingleBean(SlsLogbackConfiguration.class);
                    assertThat(context).hasSingleBean(SlsLogbackProperties.class);
                    
                    SlsLogbackProperties properties = context.getBean(SlsLogbackProperties.class);
                    assertThat(properties.getTopic()).isEqualTo("custom-topic");
                });
    }

    @Test
    void testAutoConfigurationDisabledWithoutEndpoint() {
        contextRunner
                .withPropertyValues(
                        "aliyun.sls.access-key-id=test-key-id",
                        "aliyun.sls.access-key-secret=test-key-secret",
                        "aliyun.sls.project=test-project",
                        "aliyun.sls.log-store=test-logstore",
                        "spring.application.name=test-app"
                )
                .run(context -> {
                    assertThat(context).doesNotHaveBean(SlsLogbackConfiguration.class);
                    assertThat(context).doesNotHaveBean(SlsLogbackProperties.class);
                });
    }

    @Test
    void testAutoConfigurationWithEmptyEndpointSucceeds() {
        contextRunner
                .withPropertyValues(
                        "aliyun.sls.endpoint=",
                        "aliyun.sls.access-key-id=test-key-id",
                        "aliyun.sls.access-key-secret=test-key-secret",
                        "aliyun.sls.project=test-project",
                        "aliyun.sls.log-store=test-logstore",
                        "spring.application.name=test-app"
                )
                .run(context -> {
                    // 没有Bean Validation，配置会正常加载，验证在运行时进行
                    assertThat(context).hasSingleBean(SlsLogbackConfiguration.class);
                    assertThat(context).hasSingleBean(SlsLogbackProperties.class);
                });
    }

    @Test
    void testPropertiesBindingWithKebabCase() {
        contextRunner
                .withPropertyValues(
                        "aliyun.sls.endpoint=https://cn-hangzhou.log.aliyuncs.com",
                        "aliyun.sls.access-key-id=test-key-id",
                        "aliyun.sls.access-key-secret=test-key-secret",
                        "aliyun.sls.project=test-project",
                        "aliyun.sls.log-store=test-logstore",
                        "spring.application.name=test-app"
                )
                .run(context -> {
                    SlsLogbackProperties properties = context.getBean(SlsLogbackProperties.class);
                    assertThat(properties.getAccessKeyId()).isEqualTo("test-key-id");
                    assertThat(properties.getAccessKeySecret()).isEqualTo("test-key-secret");
                    assertThat(properties.getLogStore()).isEqualTo("test-logstore");
                });
    }

    @Test
    void testPropertiesBindingWithCamelCase() {
        contextRunner
                .withPropertyValues(
                        "aliyun.sls.endpoint=https://cn-hangzhou.log.aliyuncs.com",
                        "aliyun.sls.accessKeyId=test-key-id",
                        "aliyun.sls.accessKeySecret=test-key-secret",
                        "aliyun.sls.project=test-project",
                        "aliyun.sls.logStore=test-logstore",
                        "spring.application.name=test-app"
                )
                .run(context -> {
                    SlsLogbackProperties properties = context.getBean(SlsLogbackProperties.class);
                    assertThat(properties.getAccessKeyId()).isEqualTo("test-key-id");
                    assertThat(properties.getAccessKeySecret()).isEqualTo("test-key-secret");
                    assertThat(properties.getLogStore()).isEqualTo("test-logstore");
                });
    }

    @Test
    void testDefaultApplicationNameWhenNotProvided() {
        contextRunner
                .withPropertyValues(
                        "aliyun.sls.endpoint=https://cn-hangzhou.log.aliyuncs.com",
                        "aliyun.sls.access-key-id=test-key-id",
                        "aliyun.sls.access-key-secret=test-key-secret",
                        "aliyun.sls.project=test-project",
                        "aliyun.sls.log-store=test-logstore"
                        // 不设置 spring.application.name
                )
                .run(context -> {
                    assertThat(context).hasSingleBean(SlsLogbackConfiguration.class);
                    // 验证配置能够正常初始化，即使没有提供应用名称
                });
    }
}