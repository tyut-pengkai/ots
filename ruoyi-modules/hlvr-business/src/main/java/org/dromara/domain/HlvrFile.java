package org.dromara.domain;

import org.dromara.common.mybatis.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 文件资源管理对象 hlvr_file
 *
 * @author admin
 * @date 2025-11-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("hlvr_file")
public class HlvrFile extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * $column.columnComment
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 资源类别
     */
    private String assetType;

    /**
     * $column.columnComment
     */
    @TableLogic
    private String delFlag;


}
