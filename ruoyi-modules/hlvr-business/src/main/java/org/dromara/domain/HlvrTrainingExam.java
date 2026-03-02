package org.dromara.domain;

import org.dromara.common.mybatis.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 培训配置对象 hlvr_training
 *
 * @author admin
 * @date 2025-11-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("hlvr_training_exam")
public class HlvrTrainingExam extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id")
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
     * 培训类型  train=培训，exam=考核
     */
    private String type;

    /**
     * $column.columnComment
     */
    @TableLogic
    private String delFlag;


}
