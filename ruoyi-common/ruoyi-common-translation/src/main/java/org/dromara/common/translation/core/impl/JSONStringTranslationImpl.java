package org.dromara.common.translation.core.impl;

import cn.hutool.core.util.ObjectUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
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
@TranslationType(type = TransConstant.STRING_TO_JSON)
@Slf4j
public class JSONStringTranslationImpl implements TranslationInterface<Object> {
    @Override
    public Object translation(Object key, String other)  {
        if(ObjectUtil.isEmpty(key)){
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readTree(key.toString());
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            return key;
        }
    }
}
