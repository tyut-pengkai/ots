package org.dromara.common.json.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.json.handler.BigNumberSerializer;
import org.dromara.common.json.handler.CustomDateDeserializer;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

/**
 * jackson 配置
 *
 * @author Lion Li
 */
@Slf4j
@AutoConfiguration(before = JacksonAutoConfiguration.class)
public class JacksonConfig {

    @Bean
    public Module registerJavaTimeModule() {
        // 全局配置序列化返回 JSON 处理
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(Long.class, BigNumberSerializer.INSTANCE);
        javaTimeModule.addSerializer(Long.TYPE, BigNumberSerializer.INSTANCE);
        javaTimeModule.addSerializer(BigInteger.class, BigNumberSerializer.INSTANCE);
        javaTimeModule.addSerializer(BigDecimal.class, ToStringSerializer.instance);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(formatter));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(formatter));
        javaTimeModule.addDeserializer(Date.class, new CustomDateDeserializer());
        return javaTimeModule;
    }

    /**
     * 为空值的序列化处理模块
     * 需求：当对象的 String 类型字段为 null 时，JSON 返回空字符串 ""，当返回对象的 List / Set /数组为 null 时，JSON 中输出 [] ；当 Map 为 null 时输出 {}
     * 说明：
     * - 通过 BeanSerializerModifier 为所有 Bean 的 String 属性分配空值序列化器
     * - 仅影响属性为 null 的情况，不改变非 null 的正常序列化
     * - 不改动已有的业务序列化逻辑（如自定义的 Serializer 显式写出 null 的场景保持原样）
     * - 在 Jackson 的 BeanSerializerModifier 中为集合、数组、映射类型统一指定空值序列化器。
     */
    @Bean
    public Module nullStringModule() {
        // 原本未对 String 类型的 null 值进行统一处理
        // 下面新增一个 SimpleModule，通过 BeanSerializerModifier 指定空值写为 ""
        SimpleModule module = new SimpleModule();
        module.setSerializerModifier(new BeanSerializerModifier() {
            @Override
            public java.util.List<BeanPropertyWriter> changeProperties(
                SerializationConfig config,
                BeanDescription beanDesc,
                java.util.List<BeanPropertyWriter> beanProperties
            ) {
                for (BeanPropertyWriter writer : beanProperties) {
                    // 仅针对 String 类型的属性设置空值序列化器
                    if (writer.getType() != null && writer.getType().isTypeOrSubTypeOf(String.class)) {
                        // 当该属性值为 null 时，序列化为 ""（空字符串）
                        try {
                            writer.assignNullSerializer(new JsonSerializer<Object>() {
                                @Override
                                public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws java.io.IOException {
                                    gen.writeString("");
                                }
                            });
                        } catch (IllegalStateException ignore) {
                            // 已存在自定义 null 序列化器时不覆盖
                        }
                        continue;
                    }
                    // List/Set/数组等集合类型：当为 null 时，序列化为 [] 空数组
                    // isCollectionLikeType 能同时覆盖集合与数组的场景
                    if (writer.getType() != null && writer.getType().isCollectionLikeType()) {
                        try {
                            writer.assignNullSerializer(new JsonSerializer<Object>() {
                                @Override
                                public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws java.io.IOException {
                                    gen.writeStartArray();
                                    gen.writeEndArray();
                                }
                            });
                        } catch (IllegalStateException ignore) {
                        }
                        continue;
                    }
                    // Map 类型：当为 null 时，序列化为 {} 空对象
                    if (writer.getType() != null && writer.getType().isMapLikeType()) {
                        try {
                            writer.assignNullSerializer(new JsonSerializer<Object>() {
                                @Override
                                public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws java.io.IOException {
                                    gen.writeStartObject();
                                    gen.writeEndObject();
                                }
                            });
                        } catch (IllegalStateException ignore) {
                        }
                        // continue; 最后一个分支可省略
                    }
                }
                return beanProperties;
            }
        });
        return module;
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer customizer() {
        return builder -> {
            builder.timeZone(TimeZone.getDefault());
            log.info("初始化 jackson 配置");
        };
    }

}
