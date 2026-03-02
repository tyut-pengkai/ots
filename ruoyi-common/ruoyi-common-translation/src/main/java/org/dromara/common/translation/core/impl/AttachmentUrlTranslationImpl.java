package org.dromara.common.translation.core.impl;

import lombok.AllArgsConstructor;
import org.dromara.common.core.domain.dto.AttachmentDTO;
import org.dromara.common.core.service.AttachmentService;
import org.dromara.common.core.service.AttachmentUrlService;
import org.dromara.common.translation.annotation.TranslationType;
import org.dromara.common.translation.constant.TransConstant;
import org.dromara.common.translation.core.TranslationInterface;

import java.util.List;

@AllArgsConstructor
@TranslationType(type = TransConstant.ID_TO_ATTACHMENT_URL)
public class AttachmentUrlTranslationImpl implements TranslationInterface<String> {
    private final AttachmentUrlService attachmentUrlService;
    @Override
    public String translation(Object key, String other) {
        return attachmentUrlService.selectAttachmentUrlById((long) key);
    }
}
