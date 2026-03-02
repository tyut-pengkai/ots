package org.dromara.domain.bo;

import org.dromara.domain.HlvrSopCardStepGroup;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 操作卡步骤分组业务对象 hlvr_sop_card_step_group
 *
 * @author Lion Li
 * @date 2025-11-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = HlvrSopCardStepGroup.class, reverseConvertGenerate = false)
public class HlvrSopCardStepGroupBo extends BaseEntity {

    /**
     * $column.columnComment
     */
    @NotNull(message = "$column.columnComment不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 操作卡id
     */
    private Long hscId;

    /**
     * 步骤分组名称
     */
    private String groupName;

    /**
     * 步骤分组描述
     */
    private String groupContent;

    /**
     * 步骤分组详细描述
     */
    private String groupContentDetail;

    /**
     * 步骤顺序
     */
    private Integer indexing;


}
