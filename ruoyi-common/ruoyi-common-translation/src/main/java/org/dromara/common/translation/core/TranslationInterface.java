package org.dromara.common.translation.core;

import com.fasterxml.jackson.core.JsonGenerator;
import org.dromara.common.translation.annotation.TranslationType;

/**
 * 翻译接口 (实现类需标注 {@link TranslationType} 注解标明翻译类型)
 *
 * @author Lion Li
 */
public interface TranslationInterface<T> {

    /**
     * 翻译
     *
     * @param key   需要被翻译的键(不为空)
     * @param other 其他参数
     * @return 返回键对应的值
     */
    T translation(Object key, String other);

    default T translation(Object key, String other, JsonGenerator gen, boolean bool, String suffix){
        return translation(key, other);
    }
}
