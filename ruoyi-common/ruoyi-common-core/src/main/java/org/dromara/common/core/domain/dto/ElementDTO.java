package org.dromara.common.core.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class ElementDTO {
    private Long id;
    private String ueId;
    private String elementType;
    private String name;
    private String description;
}
