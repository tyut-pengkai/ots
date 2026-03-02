package org.dromara.service;

import org.dromara.domain.HlvrAttachment;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IHlvrAttachmentService {
    HlvrAttachment uploadAttachment(MultipartFile file);

    void setBusinessId(Long businessId);
    Long getBusinessId();
}
