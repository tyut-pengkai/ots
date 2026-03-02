package org.dromara.domain.bo;

import org.dromara.domain.HlvrSopCard;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 操作卡业务对象 hlvr_sop_card
 *
 * @author Lion Li
 * @date 2025-11-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = HlvrSopCard.class, reverseConvertGenerate = false)
public class HlvrSopCardBo extends BaseEntity {

    /**
     * $column.columnComment
     */
    @NotNull(message = "$column.columnComment不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 操作卡名称
     */
    private String name;

    private String initialZB;
    /**
     * 0=VR,1=PC
     */
    private Long sopCardType;

    /**
     * 关卡地图
     */
    private String cardMap;

    /**
     * 操作卡模式，train=培训，exam=考核
     */
    private String sopCardModel;
}
