package org.dromara.common.core.domain.dto;

import lombok.Builder;
import lombok.Data;

/**
 * 上传返回体
 *
 * @author Lion Li
 */
@Data
@Builder
public class UploadResultDTO {

    /**
     * 文件路径
     */
    private String path;

    /**
     * 文件名
     */
    private String name;

    /**
     * 文件名
     */
    private String updateName;

}
