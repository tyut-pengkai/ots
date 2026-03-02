package org.dromara.common.translation.core.impl;

import cn.hutool.core.util.ObjectUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.domain.dto.ElementDTO;
import org.dromara.common.core.service.ElementService;
import org.dromara.common.translation.annotation.TranslationType;
import org.dromara.common.translation.constant.TransConstant;
import org.dromara.common.translation.core.TranslationInterface;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.Map;


@AllArgsConstructor
@TranslationType(type = TransConstant.ID_TO_ELEMENT)
@Slf4j
public class ElementTranslationImpl implements TranslationInterface<Object> {
    private final ElementService elementService;
    @Override
    public Object translation(Object key, String other) {
        if(ObjectUtil.isEmpty(key)|| (long) key == 0){
            return null;
        }
        return elementService.selectById((long) key);
    }

    @Override
    public Object translation(Object key, String other, JsonGenerator gen, boolean bool, String suffix) {
        if(ObjectUtil.isEmpty(key)){
            return null;
        }
        try {
            ElementDTO element = elementService.selectById((long) key);
            if(element == null) {
                return key;
            }
            if(!bool) {
                return element;
            }
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> objectMap = objectMapper.convertValue(element, Map.class);
            Object object = gen.currentValue();
            Class<?> clazz = object.getClass();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (field.getName().startsWith(suffix) && !field.getName().equals(suffix)) {
                    field.setAccessible(true);
                    Object obj = objectMap.get(StringUtils.uncapitalize(field.getName().replace(suffix, "")));
                    if(obj != null) {
                        field.set(object, obj);
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return key;
        }
        return key;
    }
}
