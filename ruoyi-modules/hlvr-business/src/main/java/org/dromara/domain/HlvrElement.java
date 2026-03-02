package org.dromara.domain;

import org.dromara.common.mybatis.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 资产管理对象 hlvr_element
 *
 * @author admin
 * @date 2025-11-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("hlvr_element")
public class HlvrElement extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * $column.columnComment
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 模型id
     */
    private String ueId;

    /**
     * 模型类别字典值
     */
    private String elementType;

    /**
     * 模型名称
     */
    private String name;

    /**
     * 坐标
     */
    private String position;

    /**
     * 描述
     */
    private String description;




    /**
     * $column.columnComment
     */
    @TableLogic
    private String delFlag;


}
