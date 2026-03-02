package org.dromara.common.core.service;

import org.dromara.common.core.domain.dto.AttachmentDTO;

import java.util.List;
import java.util.Map;

/**
 * 通用 附件
 *
 * @author Lion Li
 */
public interface AttachmentService {

    /**
     * 通过用户ID查询附件url
     */
    List<AttachmentDTO> selectAttachmentById(Long id);

}
