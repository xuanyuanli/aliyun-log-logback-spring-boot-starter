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

    /**
     * 需要作为 SLS 独立字段上报的 MDC 字段名，逗号分隔
     * 例如：traceId,canary,canaryMode,canaryValue
     */
    private String mdcFields;

    // ==================== 日志来源与格式配置 ====================

    /**
     * 日志来源，默认为应用程序所在宿主机的 IP
     */
    private String source;

    /**
     * 输出到日志服务的时间格式，默认是 yyyy-MM-dd'T'HH:mmZ
     */
    private String timeFormat;

    /**
     * 输出到日志服务的时间时区，默认是 UTC
     * 例如：Asia/Shanghai 表示东八区
     */
    private String timeZone;

    /**
     * 是否要记录 Location 字段（日志打印位置），默认为 true
     * 设为 false 可减少性能开销
     */
    private Boolean includeLocation;

    /**
     * 异常堆栈最大记录长度，超出此长度会被截断，默认值为 500
     */
    private Integer maxThrowable;

    /**
     * 设置上传到 sls 的日志时间精度，支持秒 s 与毫秒 ms，默认为秒
     */
    private String timePrecision;

    // ==================== 性能调优配置 ====================

    /**
     * 单个 producer 实例能缓存的日志大小上限，默认为 100MB（104857600字节）
     */
    private Integer totalSizeInBytes;

    /**
     * 如果 producer 可用空间不足，调用者在 send 方法上的最大阻塞时间，默认为 60000 毫秒（60秒）
     * 为了不阻塞打印日志的线程，建议设为 0
     */
    private Integer maxBlockMs;

    /**
     * 执行日志发送任务的线程池大小，默认为可用处理器个数
     */
    private Integer ioThreadCount;

    /**
     * 当一个 ProducerBatch 中缓存的日志大小大于等于此值时，该 batch 将被发送
     * 默认为 524288 字节（512KB），最大可设置成 5MB
     */
    private Integer batchSizeThresholdInBytes;

    /**
     * 当一个 ProducerBatch 中缓存的日志条数大于等于此值时，该 batch 将被发送
     * 默认为 4096，最大可设置成 40960
     */
    private Integer batchCountThreshold;

    /**
     * 一个 ProducerBatch 从创建到可发送的逗留时间，默认为 2000 毫秒（2秒）
     * 最小可设置成 100 毫秒
     */
    private Integer lingerMs;

    /**
     * 如果某个 ProducerBatch 首次发送失败，能够对其重试的次数，默认为 10 次
     */
    private Integer retries;

}
