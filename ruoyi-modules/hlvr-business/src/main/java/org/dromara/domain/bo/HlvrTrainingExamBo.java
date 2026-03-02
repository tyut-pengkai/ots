package org.dromara.domain.bo;

import org.dromara.domain.HlvrTrainingExam;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

import java.util.List;

/**
 * 培训配置业务对象 hlvr_training
 *
 * @author admin
 * @date 2025-11-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = HlvrTrainingExam.class, reverseConvertGenerate = false)
public class HlvrTrainingExamBo extends BaseEntity {

    /**
     * id
     */
    @NotNull(message = "id不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 培训名称
     */
    private String name;

    /**
     * 参与组织
     */
    private Long deptId;

    /**
     * 模式： train=培训，exam=考核
     */
    private String type;



    private List<HlvrTrainingExamDetailBo> details;
}
