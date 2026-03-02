package org.dromara.domain;

import org.dromara.common.mybatis.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 操作卡对象 hlvr_sop_card
 *
 * @author Lion Li
 * @date 2025-11-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("hlvr_sop_card")
public class HlvrSopCard extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * $column.columnComment
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 操作卡名称
     */
    private String name;

    @TableField(value = "initial_ZB")
    private String initialZB;

    /**
     * 0=VR,1=PC
     */
    private Long sopCardType;

    /**
     * 删除标记
     */
    @TableLogic
    private String delFlag;

    /**
     * 关卡地图
     */
    private String cardMap;
    /**
     * 操作卡模式 train=培训，exam=考核
     */
    private String sopCardModel;
}
