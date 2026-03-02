package org.dromara.domain.bo;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.dromara.common.translation.annotation.FieldToString;
import org.dromara.domain.HlvrSopCardStepOpt;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import org.dromara.domain.HlvrSopCardStepResult;

import java.util.List;
import java.util.Map;

/**
 * 操作卡步骤业务对象 hlvr_sop_card_step_opt
 *
 * @author Lion Li
 * @date 2025-11-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = HlvrSopCardStepOpt.class, reverseConvertGenerate = false)
public class HlvrSopCardStepOptBo extends BaseEntity {

    /**
     * $column.columnComment
     */
    @NotNull(message = "$column.columnComment不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 操作卡分组id
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
    @FieldToString
    @JsonProperty("targetObjectInitalStatus")
    private String targetObjectInitalStatus;

    /**
     * 影响对象的交互后状态
     */
    @FieldToString
    @JsonProperty("targetObjectEndingStatus")
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
    @FieldToString
    @JsonProperty("mainObjectInitalStatus")
    private String mainObjectInitalStatus;

    /**
     * 交互对象的交互后状态
     */
    @FieldToString
    @JsonProperty("mainObjectEndingStatus")
    private String mainObjectEndingStatus;
    /**
     * 步骤顺序
     */
    private Integer indexing;

    private int score;

    private String scoringCriteria;

    private Map<String, HlvrSopCardStepResultBo> effect;

    // 向上插入0，向下插入1，新增null
    private String insertLocal;
}
