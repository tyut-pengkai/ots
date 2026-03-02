package org.dromara.domain.vo;

import org.dromara.common.translation.annotation.Translation;
import org.dromara.common.translation.constant.TransConstant;
import org.dromara.domain.HlvrSopCard;
import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import org.dromara.common.excel.annotation.ExcelDictFormat;
import org.dromara.common.excel.convert.ExcelDictConvert;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.domain.HlvrSopCardStepGroup;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * 操作卡视图对象 hlvr_sop_card
 *
 * @author Lion Li
 * @date 2025-11-13
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = HlvrSopCard.class)
public class HlvrSopCardVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * $column.columnComment
     */
    @ExcelProperty(value = "${comment}", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "$column.readConverterExp()")
    private Long id;

    /**
     * 操作卡名称
     */
    @ExcelProperty(value = "操作卡名称")
    private String name;

    @ExcelProperty(value = "初始坐标")
    private String initialZB;
    /**
     * 0=VR,1=PC
     */
    @ExcelProperty(value = "0=VR,1=PC")
    private Long sopCardType;

    /**
     * 序号
     */
    @ExcelProperty(value = "序号")
    private Long indexing;

    /**
     * 关卡地图
     */
    @ExcelProperty(value = "关卡地图")
    private String cardMap;

    private List<HlvrSopCardStepGroupVo> groups;

    @ExcelProperty(value = "操作卡模式，train=培训，exam=考核")
    private String sopCardModel;

    private Date updateTime;
    @Translation(type = TransConstant.USER_ID_TO_NICKNAME)
    private String createBy;
}
