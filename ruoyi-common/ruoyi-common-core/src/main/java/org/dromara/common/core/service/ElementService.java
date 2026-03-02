package org.dromara.common.core.service;

import org.dromara.common.core.domain.dto.ElementDTO;


/**
 * 通用 附件
 *
 * @author Lion Li
 */
public interface ElementService {

    ElementDTO selectById(Long id);
}
