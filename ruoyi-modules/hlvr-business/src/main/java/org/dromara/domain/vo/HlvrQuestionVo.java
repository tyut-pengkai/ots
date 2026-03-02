package org.dromara.domain.vo;

import org.dromara.common.translation.annotation.Translation;
import org.dromara.common.translation.constant.TransConstant;
import org.dromara.domain.HlvrQuestion;
import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import org.dromara.common.excel.annotation.ExcelDictFormat;
import org.dromara.common.excel.convert.ExcelDictConvert;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * 问题视图对象 hlvr_question
 *
 * @author admin
 * @date 2025-11-18
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = HlvrQuestion.class)
public class HlvrQuestionVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ExcelProperty(value = "id")
    private Long id;

    /**
     * 操作卡id
     */
    @ExcelProperty(value = "操作卡id")
    private Long hscId;

    @Translation(type = TransConstant.ID_TO_CARD, mapper = "hscId")
    private Long hscName;
    /**
     * 问题名称
     */
    @ExcelProperty(value = "问题名称")
    private String questionName;

    /**
     * 问题描述
     */
    @ExcelProperty(value = "问题描述")
    private String questionDescription;

    /**
     * 问题类型，0=判断，1=选择，2=操作匹配
     */
    @ExcelProperty(value = "问题类型，0=判断，1=选择，2=操作匹配")
    private String questionType;

    private List<HlvrQuestionItemVo> questionItemBos;
}
