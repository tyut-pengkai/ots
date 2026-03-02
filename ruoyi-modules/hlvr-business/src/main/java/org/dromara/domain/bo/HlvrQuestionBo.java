package org.dromara.domain.bo;

import org.dromara.domain.HlvrQuestion;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

import java.util.List;

/**
 * 问题业务对象 hlvr_question
 *
 * @author admin
 * @date 2025-11-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = HlvrQuestion.class, reverseConvertGenerate = false)
public class HlvrQuestionBo extends BaseEntity {

    /**
     * id
     */
    @NotNull(message = "id不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 操作卡id
     */
    private Long hscId;

    /**
     * 问题名称
     */
    private String questionName;

    /**
     * 问题描述
     */
    private String questionDescription;

    /**
     * 问题类型，judge=判断，select=选择，step=步骤跳转，result=提示选择结果
     */
    private String questionType;

    private List<HlvrQuestionItemBo> questionItemBos;
}
