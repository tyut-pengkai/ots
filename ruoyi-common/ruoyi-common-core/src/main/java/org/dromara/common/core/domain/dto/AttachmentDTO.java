package org.dromara.common.core.domain.dto;

import lombok.Data;

@Data
public class AttachmentDTO {
    private Long id;
    private String name;
    private String path;
    private String updateName;
    private Long businessId;
    private String suffix;
}
