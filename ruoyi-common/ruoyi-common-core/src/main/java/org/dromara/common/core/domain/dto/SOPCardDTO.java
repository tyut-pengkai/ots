package org.dromara.common.core.domain.dto;

import lombok.Data;

@Data
public class SOPCardDTO {

    private String name;

    private Long initialX;

    private Long initialY;

    private Long initialZ;

    private Long sopCardType;

    private Long indexing;

    private String cardMap;
}
