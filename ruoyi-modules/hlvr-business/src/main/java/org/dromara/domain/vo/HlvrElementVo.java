package org.dromara.domain.vo;

import org.dromara.common.translation.annotation.Translation;
import org.dromara.common.translation.constant.TransConstant;
import org.dromara.domain.HlvrAttachment;
import org.dromara.domain.HlvrElement;
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
 * 资产管理视图对象 hlvr_element
 *
 * @author admin
 * @date 2025-11-12
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = HlvrElement.class)
public class HlvrElementVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * $column.columnComment
     */
    private Long id;

    /**
     * 模型id
     */
    @ExcelProperty(value = "模型id")
    private String ueId;

    /**
     * 模型类别字典值
     */
    @ExcelProperty(value = "模型类别", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "ue_model_type")
    private String elementType;

    /**
     * 模型名称
     */
    @ExcelProperty(value = "模型名称")
    private String name;


    /**
     * 坐标
     */
    @ExcelProperty(value = "坐标")
    private String position;

    /**
     * 描述
     */
    @ExcelProperty(value = "描述")
    private String description;


    @Translation(type = TransConstant.OVERVIEW_TO_URL, mapper = "id")
    private String overviewUrl;

    @Translation(type = TransConstant.ID_TO_ATTACHMENT, mapper = "id")
    private List<HlvrAttachment> file;
}
