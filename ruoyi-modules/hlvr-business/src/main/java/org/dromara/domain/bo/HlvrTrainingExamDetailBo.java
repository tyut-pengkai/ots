package org.dromara.domain.bo;

import org.dromara.domain.HlvrTrainingExamDetail;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 培训配置详情业务对象 hlvr_training_detail
 *
 * @author admin
 * @date 2025-11-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = HlvrTrainingExamDetail.class, reverseConvertGenerate = false)
public class HlvrTrainingExamDetailBo extends BaseEntity {

    /**
     * id
     */
    @NotNull(message = "id不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 培训配置表id
     */
    private Long htId;

    /**
     * 操作卡id
     */
    private Long hscId;


}
