package org.dromara.domain;

import org.dromara.common.mybatis.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.Date;

/**
 * 操作卡步骤记录对象 hlvr_sop_card_step_opt_history
 *
 * @author admin
 * @date 2025-11-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("hlvr_sop_card_step_opt_history")
public class HlvrSopCardStepOptHistory extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 操作卡步骤id
     */
    private Long hscsoId;

    /**
     * 用户得分
     */
    private Integer score;

    /**
     * 下一步操作步骤id
     */
    private Long nextStep;

    /**
     * $column.columnComment
     */
    @TableLogic
    private String delFlag;

    /**
     * 答案id
     */
    private Long answer;

    private Long batchId;
    private Date endTime;

    private String optContent;

    private String stepName;
}
