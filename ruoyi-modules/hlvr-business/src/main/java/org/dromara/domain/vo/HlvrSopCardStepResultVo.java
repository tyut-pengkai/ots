package org.dromara.domain.vo;

import org.dromara.common.translation.annotation.Translation;
import org.dromara.common.translation.constant.TransConstant;
import org.dromara.domain.HlvrSopCardStepResult;
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
 * 操作卡步骤影响结果视图对象 hlvr_sop_card_step_result
 *
 * @author Lion Li
 * @date 2025-11-07
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = HlvrSopCardStepResult.class)
public class HlvrSopCardStepResultVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * $column.columnComment
     */
    @ExcelProperty(value = "${comment}", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "$column.readConverterExp()")
    private Long id;

    /**
     * 步骤id
     */
    @ExcelProperty(value = "步骤id")
    private Long hscsoId;

    /**
     * 结果类别 animation= 动画，popwindow=弹窗，voice=语音
     */
    @ExcelProperty(value = "结果类别")
    private String resultType;

    /**
     * 资产信息
     */
    @ExcelProperty(value = "资产信息")
    @Translation(type = TransConstant.RESOURCE_ID_TO_ELEMENT, field = true, suffix = "resource", other = "resultType", mapper = "resourceAttr")
    private Long resourceId;
    private String resourceUeId;
    private String resourcePath;
    private String resourceWebPath;
    /**
     * 资产属性
     */
    @ExcelProperty(value = "资产属性")
    @Translation(type = TransConstant.STRING_TO_JSON)
    private String resourceAttr;

    /**
     * 延时时间
     */
    @ExcelProperty(value = "延时时间")
    private Integer resourceDelayTime;



    /**
     * 是否选择
     */
    private boolean choose;
}
