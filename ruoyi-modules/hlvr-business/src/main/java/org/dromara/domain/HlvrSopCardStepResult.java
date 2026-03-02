package org.dromara.domain;

import org.dromara.common.mybatis.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.translation.annotation.Translation;
import org.dromara.common.translation.constant.TransConstant;

import java.io.Serial;

/**
 * 操作卡步骤影响结果对象 hlvr_sop_card_step_result
 *
 * @author Lion Li
 * @date 2025-11-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("hlvr_sop_card_step_result")
public class HlvrSopCardStepResult extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * $column.columnComment
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 步骤id
     */
    private Long hscsoId;

    /**
     * 结果类别
     */
    private String resultType;

    /**
     * 资产id
     */
    private Long resourceId;

    /**
     * 资产属性
     */
    private String resourceAttr;

    /**
     * 资产延迟播放时间
     */
    private Integer resourceDelayTime;


    /**
     * $column.columnComment
     */
    @TableLogic
    private String delFlag;


}
