# 阿里云日志服务(SLS) Logback Spring Boot Starter

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Maven Central](https://img.shields.io/maven-central/v/cn.xuanyuanli.boot/aliyun-log-logback-spring-boot-starter.svg)](https://search.maven.org/search?q=g:cn.xuanyuanli.boot%20AND%20a:aliyun-log-logback-spring-boot-starter)

> 一个用于集成阿里云日志服务(SLS)的Spring Boot Starter，提供了简化的自动配置，让您的Spring Boot应用能够轻松地将日志发送到阿里云SLS。

## 特性

- ✅ **自动配置**: 基于Spring Boot自动配置机制，零代码集成
- ✅ **参数校验**: 内置配置参数验证，运行时检查配置完整性
- ✅ **错误处理**: 完善的错误处理和降级机制
- ✅ **灵活配置**: 支持多种配置方式和参数绑定
- ✅ **生产就绪**: 包含完整的单元测试和集成测试

## 快速开始

### 1. 添加依赖

在您的 `pom.xml` 文件中添加以下依赖：

```xml
<dependency>
    <groupId>cn.xuanyuanli.boot</groupId>
    <artifactId>aliyun-log-logback-spring-boot-starter</artifactId>
    <version>LATEST</version>
</dependency>
```

### 2. 配置参数

在 `application.yml` 中添加SLS配置：

```yaml
spring:
  application:
    name: my-application

aliyun:
  sls:
    endpoint: https://cn-hangzhou.log.aliyuncs.com  # SLS服务端点
    access-key-id: ${ALIYUN_ACCESS_KEY_ID}          # 阿里云AccessKey ID
    access-key-secret: ${ALIYUN_ACCESS_KEY_SECRET}  # 阿里云AccessKey Secret
    project: my-sls-project                         # SLS项目名称
    log-store: my-logstore                          # SLS日志库名称
    topic: my-topic                                 # 可选：日志主题，默认使用应用名称
```

或使用 `application.properties`：

```properties
spring.application.name=my-application

aliyun.sls.endpoint=https://cn-hangzhou.log.aliyuncs.com
aliyun.sls.access-key-id=${ALIYUN_ACCESS_KEY_ID}
aliyun.sls.access-key-secret=${ALIYUN_ACCESS_KEY_SECRET}
aliyun.sls.project=my-sls-project
aliyun.sls.log-store=my-logstore
aliyun.sls.topic=my-topic
```

### 3. 使用日志

配置完成后，您的应用日志将自动发送到阿里云SLS，无需任何额外代码：

```java
@Slf4j
@RestController
public class HelloController {
    
    @GetMapping("/hello")
    public String hello() {
        log.info("Hello SLS!"); // 这条日志会自动发送到阿里云SLS
        return "Hello World!";
    }
}
```

## 配置参数说明

| 参数 | 必需 | 说明 | 示例 |
|------|------|------|------|
| `aliyun.sls.endpoint` | ✅ | SLS服务端点 | `https://cn-hangzhou.log.aliyuncs.com` |
| `aliyun.sls.access-key-id` | ✅ | 阿里云访问密钥ID | `LTAI***` |
| `aliyun.sls.access-key-secret` | ✅ | 阿里云访问密钥Secret | `xxx***` |
| `aliyun.sls.project` | ✅ | SLS项目名称 | `my-project` |
| `aliyun.sls.log-store` | ✅ | SLS日志库名称 | `my-logstore` |
| `aliyun.sls.topic` | ❌ | 日志主题，默认使用应用名称 | `my-topic` |

## 安全配置建议

为了保护您的访问密钥，强烈建议使用环境变量或加密配置：

### 方式1：环境变量

```bash
export ALIYUN_ACCESS_KEY_ID=your_access_key_id
export ALIYUN_ACCESS_KEY_SECRET=your_access_key_secret
```

```yaml
aliyun:
  sls:
    access-key-id: ${ALIYUN_ACCESS_KEY_ID}
    access-key-secret: ${ALIYUN_ACCESS_KEY_SECRET}
```

### 方式2：Spring Cloud Config

使用Spring Cloud Config Server管理敏感配置。

### 方式3：Docker Secrets

```bash
docker run -e ALIYUN_ACCESS_KEY_ID_FILE=/run/secrets/access_key_id \
           -e ALIYUN_ACCESS_KEY_SECRET_FILE=/run/secrets/access_key_secret \
           your-app
```

## SLS服务端点

不同地域的SLS服务端点：

| 地域 | 端点 |
|------|------|
| 华东1（杭州） | `https://cn-hangzhou.log.aliyuncs.com` |
| 华东2（上海） | `https://cn-shanghai.log.aliyuncs.com` |
| 华北1（青岛） | `https://cn-qingdao.log.aliyuncs.com` |
| 华北2（北京） | `https://cn-beijing.log.aliyuncs.com` |
| 华北3（张家口） | `https://cn-zhangjiakou.log.aliyuncs.com` |
| 华南1（深圳） | `https://cn-shenzhen.log.aliyuncs.com` |

更多地域端点请参考[阿里云SLS官方文档](https://help.aliyun.com/document_detail/29008.html)。

## 条件激活

只有当配置了 `aliyun.sls.endpoint` 属性时，SLS Logback配置才会被激活。如果您需要在某些环境中禁用SLS日志，只需不设置或注释掉该配置即可。

```yaml
# 开发环境 - 不发送到SLS
spring:
  profiles: dev
# aliyun.sls.endpoint 未配置，SLS功能不会激活

---
# 生产环境 - 发送到SLS  
spring:
  profiles: prod
aliyun:
  sls:
    endpoint: https://cn-hangzhou.log.aliyuncs.com
    # ... 其他配置
```

## 故障排除

### 常见问题

1. **应用启动失败或SLS初始化失败**
   - 检查所有必需的配置参数是否正确设置
   - 确保访问密钥有效且具有SLS写入权限

2. **日志没有出现在SLS中**
   - 检查SLS项目和日志库是否存在
   - 验证网络连接和防火墙设置
   - 查看应用日志中的错误信息

3. **配置参数绑定问题**
   - 支持驼峰命名和短横线命名
   - `accessKeyId` 和 `access-key-id` 都可以使用

### 启用调试日志

```yaml
logging:
  level:
    cn.xuanyuanli.boot.aliyunsls: DEBUG
```

## 版本兼容性

| Starter版本 | Spring Boot版本 | Java版本 |
|------------|----------------|--------|
| 1.0.0      | 3.0+           | 21+    |

## 开发和贡献

### 构建项目

```bash
mvn clean compile
```

### 运行测试

```bash
mvn test
```

### 打包

```bash
mvn clean package
```

## 许可证

该项目使用 Apache License 2.0 许可证。详情请参见 [LICENSE](LICENSE) 文件。

## 联系我们

- 作者：xuanyuanli
- 邮箱：xuanyuanli999@gmail.com
- 项目地址：https://github.com/xuanyuanli/aliyun-log-logback-spring-boot-starter
