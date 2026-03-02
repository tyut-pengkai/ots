package org.dromara.common.translation.core.impl;

import cn.hutool.core.util.ObjectUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.domain.dto.ElementDTO;
import org.dromara.common.core.service.ConfigService;
import org.dromara.common.core.service.ElementService;
import org.dromara.common.translation.annotation.TranslationType;
import org.dromara.common.translation.constant.TransConstant;
import org.dromara.common.translation.core.TranslationInterface;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.Map;


@AllArgsConstructor
@TranslationType(type = TransConstant.OVERVIEW_TO_URL)
@Slf4j
public class ElementOverviewTranslationImpl implements TranslationInterface<Object> {
    private final ConfigService configService;
    @Override
    public Object translation(Object key, String other) {
        if(ObjectUtil.isEmpty(key)|| (long) key == 0){
            return null;
        }
        return configService.getConfigValue("sys.oss.preview.prefix") + "/viewoverview?id=" + key+"&t=" + System.currentTimeMillis();
    }
}
