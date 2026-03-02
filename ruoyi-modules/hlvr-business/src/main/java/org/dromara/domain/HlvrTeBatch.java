package org.dromara.domain;

import org.dromara.common.mybatis.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.Date;

/**
 * 培训/考核 批次对象 hlvr_te_batch
 *
 * @author admin
 * @date 2025-11-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("hlvr_te_batch")
public class HlvrTeBatch extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 培训，考核id
     */
    private Long hteId;
    private Long htedId;
    /**
     * $column.columnComment
     */
    @TableLogic
    private String delFlag;

    private Date endTime;
}
