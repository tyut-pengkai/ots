package org.dromara.common.core.service;

/**
 * 通用 附件
 *
 * @author Lion Li
 */
public interface AttachmentUrlService {

    /**
     * 通过用户ID查询附件url
     */
    String selectAttachmentUrlById(Long id);

    String selectAttachmentUrlByBusinessId(Long id);
}
