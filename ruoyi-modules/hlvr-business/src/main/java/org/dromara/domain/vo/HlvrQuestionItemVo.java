package org.dromara.domain.vo;

import org.dromara.common.translation.annotation.Translation;
import org.dromara.common.translation.constant.TransConstant;
import org.dromara.domain.HlvrQuestionItem;
import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import org.dromara.common.excel.annotation.ExcelDictFormat;
import org.dromara.common.excel.convert.ExcelDictConvert;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;



/**
 * 问题选项视图对象 hlvr_question_item
 *
 * @author admin
 * @date 2025-11-18
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = HlvrQuestionItem.class)
public class HlvrQuestionItemVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ExcelProperty(value = "id")
    private Long id;

    /**
     * 问题id
     */
    @ExcelProperty(value = "问题id")
    private Long hqId;

    /**
     * 选项标签：A,B,C,D,正确,错误
     */
    @ExcelProperty(value = "选项标签：A,B,C,D,正确,错误")
    private String itemTag;

    /**
     * 选项描述
     */
    @ExcelProperty(value = "选项描述")
    private String itemDescription;

    private String itemContentSelect;
    /**
     * 跳转步骤
     */
    @ExcelProperty(value = "跳转步骤")
    private Long hscsoId;

    /**
     * 是否是正确选项1=是，0=否
     */
    @ExcelProperty(value = "是否是正确选项1=是，0=否")
    private String isAnswer;

    /**
     * 交互模型id
     */
    @ExcelProperty(value = "交互模型id")
    @Translation(type = TransConstant.ID_TO_ELEMENT)
    private Long elementId;


}
