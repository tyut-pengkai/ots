package org.dromara.domain;

import org.dromara.common.mybatis.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.translation.annotation.Translation;
import org.dromara.common.translation.constant.TransConstant;

import java.io.Serial;

/**
 * 问题选项对象 hlvr_question_item
 *
 * @author admin
 * @date 2025-11-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("hlvr_question_item")
public class HlvrQuestionItem extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id")
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

    /**
     * 选项描述
     */
    private String itemContentSelect;

    /**
     * 跳转步骤
     */
    private Long hscsoId;

    /**
     * $column.columnComment
     */
//    @TableLogic
    private String delFlag;

    /**
     * 是否是正确选项1=是，0=否
     */
    private String isAnswer;

    /**
     * 交互模型id
     */
    private Long elementId;


}
