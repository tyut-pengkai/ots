package org.dromara.domain;

import org.dromara.common.mybatis.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 培训配置详情对象 hlvr_training_detail
 *
 * @author admin
 * @date 2025-11-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("hlvr_training_exam_detail")
public class HlvrTrainingExamDetail extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 培训配置表id
     */
    private Long htId;

    /**
     * 操作卡id
     */
    private Long hscId;

    /**
     * $column.columnComment
     */
    @TableLogic
    private String delFlag;


}
