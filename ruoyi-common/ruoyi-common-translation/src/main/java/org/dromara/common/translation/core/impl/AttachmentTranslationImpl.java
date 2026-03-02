package org.dromara.common.translation.core.impl;

import lombok.AllArgsConstructor;
import org.dromara.common.core.domain.dto.AttachmentDTO;
import org.dromara.common.core.service.AttachmentService;
import org.dromara.common.translation.annotation.TranslationType;
import org.dromara.common.translation.constant.TransConstant;
import org.dromara.common.translation.core.TranslationInterface;

import java.util.List;

@AllArgsConstructor
@TranslationType(type = TransConstant.ID_TO_ATTACHMENT)
public class AttachmentTranslationImpl implements TranslationInterface<List<AttachmentDTO>> {
    private final AttachmentService attachmentService;
    @Override
    public List<AttachmentDTO> translation(Object key, String other) {
        return attachmentService.selectAttachmentById((long) key);
    }
}
