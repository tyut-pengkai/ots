package org.dromara.domain.bo;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.dromara.common.translation.annotation.FieldToString;
import org.dromara.domain.HlvrSopCardStepResult;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 操作卡步骤影响结果业务对象 hlvr_sop_card_step_result
 *
 * @author Lion Li
 * @date 2025-11-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = HlvrSopCardStepResult.class, reverseConvertGenerate = false)
public class HlvrSopCardStepResultBo extends BaseEntity {

    /**
     * $column.columnComment
     */
    @NotNull(message = "$column.columnComment不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 步骤id
     */
    private Long hscsoId;

    private String resultType;

//    private Long resourceId;
    @FieldToString
    @JsonProperty("resourceAttr")
    private String resourceAttr;

    private Integer resourceDelayTime;

    private boolean choose;
}
