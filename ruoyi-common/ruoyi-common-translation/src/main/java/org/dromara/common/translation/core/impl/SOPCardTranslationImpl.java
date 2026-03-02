package org.dromara.common.translation.core.impl;

import lombok.AllArgsConstructor;
import org.dromara.common.core.service.SOPCardService;
import org.dromara.common.translation.annotation.TranslationType;
import org.dromara.common.translation.constant.TransConstant;
import org.dromara.common.translation.core.TranslationInterface;

@AllArgsConstructor
@TranslationType(type = TransConstant.ID_TO_CARD)
public class SOPCardTranslationImpl implements TranslationInterface<String> {

    private final SOPCardService cardService;
    @Override
    public String translation(Object key, String other) {
        return cardService.selectEntity((long) key);
    }
}
