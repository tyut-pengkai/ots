package org.dromara.domain.vo;

import cn.hutool.json.JSONObject;
import org.dromara.common.translation.annotation.Translation;
import org.dromara.common.translation.constant.TransConstant;
import org.dromara.domain.HlvrSopCardStepOpt;
import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import org.dromara.common.excel.annotation.ExcelDictFormat;
import org.dromara.common.excel.convert.ExcelDictConvert;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.domain.bo.HlvrSopCardStepResultBo;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 操作卡步骤视图对象 hlvr_sop_card_step_opt
 *
 * @author Lion Li
 * @date 2025-11-07
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = HlvrSopCardStepOpt.class)
public class HlvrSopCardStepOptVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * $column.columnComment
     */
    @ExcelProperty(value = "${comment}", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "$column.readConverterExp()")
    private Long id;

    /**
     * 操作卡分组id
     */
    @ExcelProperty(value = "操作卡id")
    private Long hscsgId;

    private String groupName;

    /**
     * 操作内容
     */
    @ExcelProperty(value = "操作内容")
    private String optContent;

    /**
     * 0=never,1=before,2=after不播放，操作前，操作后
     */
    @ExcelProperty(value = "0=never,1=before,2=after不播放，操作前，操作后")
    private String voicePlayTiming;

    /**
     * 影响对象id
     */
    @ExcelProperty(value = "影响对象id")
    @Translation(type = TransConstant.ID_TO_ELEMENT, field = true, suffix = "targetObject")
    private Long targetObject;
    private String targetObjectUeId;
    private String targetObjectElementType;
    private String targetObjectName;
    /**
     * 交互对象id
     */
    @ExcelProperty(value = "交互对象id")
    @Translation(type = TransConstant.ID_TO_ELEMENT, field = true, suffix = "mainObject")
    private Long mainObject;
    private String mainObjectUeId;
    private String mainObjectElementType;
    private String mainObjectName;
    /**
     * 影响对象的初始状态
     */
    @ExcelProperty(value = "影响对象的初始状态")
    @Translation(type = TransConstant.STRING_TO_JSON)
    private String targetObjectInitalStatus;

    /**
     * 影响对象的交互后状态
     */
    @ExcelProperty(value = "影响对象的交互后状态")
    @Translation(type = TransConstant.STRING_TO_JSON)
    private String targetObjectEndingStatus;

    /**
     * 0=assessment,1=training;考核，训练
     */
    @ExcelProperty(value = "assessment,training;考核，训练")
    private String optType;

    /**
     * 交互方式
     */
    @ExcelProperty(value = "交互方式")
    private String eventType;

    /**
     * 操作解说
     */
    @ExcelProperty(value = "操作解说")
    private String optCommentary;

    /**
     * 错误提示
     */
    @ExcelProperty(value = "错误提示")
    private String errorMsg;

    /**
     * 错误提示语音开关
     */
    @ExcelProperty(value = "错误提示语音开关")
    private Long errorVoiceStatus;

    /**
     * 交互对象的初始状态
     */
    @ExcelProperty(value = "交互对象的初始状态")
    @Translation(type = TransConstant.STRING_TO_JSON)
    private String mainObjectInitalStatus;

    /**
     * 交互对象的交互后状态
     */
    @ExcelProperty(value = "交互对象的交互后状态")
    @Translation(type = TransConstant.STRING_TO_JSON)
    private String mainObjectEndingStatus;
    /**
     * 步骤顺序
     */
    private Integer indexing;

    private int score;

    private String scoringCriteria;

    private Map<String, HlvrSopCardStepResultVo> effect;
}
