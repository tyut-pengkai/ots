package org.dromara.common.translation.core.impl;

import lombok.AllArgsConstructor;
import org.dromara.common.core.domain.dto.AttachmentDTO;
import org.dromara.common.core.service.AttachmentService;
import org.dromara.common.core.service.ConfigService;
import org.dromara.common.translation.annotation.TranslationType;
import org.dromara.common.translation.constant.TransConstant;
import org.dromara.common.translation.core.TranslationInterface;

import java.util.List;

@AllArgsConstructor
@TranslationType(type = TransConstant.FULL_PATH)
public class FullPathTranslationImpl implements TranslationInterface<String> {
    private final ConfigService configService;
    @Override
    public String translation(Object key, String other) {
        return configService.getConfigValue("sys.oss.preview.prefix") + key.toString();
    }
}
