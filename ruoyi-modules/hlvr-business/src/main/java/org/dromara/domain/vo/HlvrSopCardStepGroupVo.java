package org.dromara.domain.vo;

import org.dromara.domain.HlvrSopCardStepGroup;
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
 * 操作卡步骤分组视图对象 hlvr_sop_card_step_group
 *
 * @author Lion Li
 * @date 2025-11-13
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = HlvrSopCardStepGroup.class)
public class HlvrSopCardStepGroupVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * $column.columnComment
     */
    @ExcelProperty(value = "${comment}", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "$column.readConverterExp()")
    private Long id;

    /**
     * 操作卡id
     */
    @ExcelProperty(value = "操作卡id")
    private Long hscId;

    /**
     * 步骤分组名称
     */
    @ExcelProperty(value = "步骤分组名称")
    private String groupName;

    /**
     * 步骤分组描述
     */
    @ExcelProperty(value = "步骤分组描述")
    private String groupContent;

    /**
     * 步骤分组详细描述
     */
    @ExcelProperty(value = "步骤分组详细描述")
    private String groupContentDetail;

    /**
     * 步骤顺序
     */
    @ExcelProperty(value = "步骤顺序")
    private Integer indexing;

    private List<HlvrSopCardStepOptVo> opts;
}
