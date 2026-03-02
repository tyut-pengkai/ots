package org.dromara.domain.bo;

import org.dromara.domain.HlvrSopCardStepOptHistory;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 操作卡步骤记录业务对象 hlvr_sop_card_step_opt_history
 *
 * @author admin
 * @date 2025-11-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = HlvrSopCardStepOptHistory.class, reverseConvertGenerate = false)
public class HlvrSopCardStepOptHistoryBo extends BaseEntity {

    /**
     * id
     */
    @NotNull(message = "id不能为空", groups = { EditGroup.class })
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
     * 答案id
     */
    private Long answer;

    private Long batchId;
}
