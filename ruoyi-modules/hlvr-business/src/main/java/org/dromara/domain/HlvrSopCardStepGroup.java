package org.dromara.domain;

import org.dromara.common.mybatis.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 操作卡步骤分组对象 hlvr_sop_card_step_group
 *
 * @author Lion Li
 * @date 2025-11-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("hlvr_sop_card_step_group")
public class HlvrSopCardStepGroup extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * $column.columnComment
     */
    @TableId(value = "id")
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
     * $column.columnComment
     */
    @TableLogic
    private String delFlag;

    /**
     * 步骤顺序
     */
    private Integer indexing;


}
