package org.dromara.domain;

import org.dromara.common.mybatis.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 问题对象 hlvr_question
 *
 * @author admin
 * @date 2025-11-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("hlvr_question")
public class HlvrQuestion extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id")
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
     * 问题类型，0=判断，1=选择，2=操作匹配
     */
    private String questionType;

    /**
     * $column.columnComment
     */
    @TableLogic
    private String delFlag;


}
