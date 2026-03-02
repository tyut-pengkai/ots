package org.dromara.domain;

import org.dromara.common.mybatis.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.List;

/**
 * 操作卡步骤对象 hlvr_sop_card_step_opt
 *
 * @author Lion Li
 * @date 2025-11-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("hlvr_sop_card_step_opt")
public class HlvrSopCardStepOpt extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * $column.columnComment
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 操作卡id
     */
    private Long hscsgId;

    /**
     * 操作内容
     */
    private String optContent;

    /**
     * 0=never,1=before,2=after不播放，操作前，操作后
     */
    private String voicePlayTiming;

    /**
     * $column.columnComment
     */
    @TableLogic
    private String delFlag;

    /**
     * 影响对象id
     */
    private Long targetObject;

    /**
     * 交互对象id
     */
    private Long mainObject;

    /**
     * 影响对象的初始状态
     */
    private String targetObjectInitalStatus;

    /**
     * 影响对象的交互后状态
     */
    private String targetObjectEndingStatus;

    /**
     * 0=assessment,1=training;考核，训练
     */
    private String optType;

    /**
     * 交互方式
     */
    private String eventType;

    /**
     * 操作解说
     */
    private String optCommentary;

    /**
     * 错误提示
     */
    private String errorMsg;

    /**
     * 错误提示语音开关
     */
    private Long errorVoiceStatus;

    /**
     * 交互对象的初始状态
     */
    private String mainObjectInitalStatus;

    /**
     * 交互对象的交互后状态
     */
    private String mainObjectEndingStatus;

    /**
     * 步骤顺序
     */
    private Integer indexing;

    private int score;

    private String scoringCriteria;
}
