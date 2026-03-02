package org.dromara.domain.bo;

import org.dromara.domain.HlvrQuestionItem;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 问题选项业务对象 hlvr_question_item
 *
 * @author admin
 * @date 2025-11-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = HlvrQuestionItem.class, reverseConvertGenerate = false)
public class HlvrQuestionItemBo extends BaseEntity {

    /**
     * id
     */
    @NotNull(message = "id不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 问题id
     */
    private Long hqId;

    /**
     * 选项标签：A,B,C,D,正确,错误
     */
    private String itemTag;

    /**
     * 选项描述
     */
    private String itemDescription;

    private String itemContentSelect;

    /**
     * 跳转步骤
     */
    private Long hscsoId;

    /**
     * 是否是正确选项1=是，0=否
     */
    private String isAnswer;

    /**
     * 交互模型id
     */
    private Long elementId;


}
