package org.dromara.domain.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import org.dromara.common.translation.annotation.Translation;
import org.dromara.common.translation.constant.TransConstant;
import org.dromara.domain.HlvrAttachment;
import org.dromara.domain.HlvrFile;
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
 * 文件资源管理视图对象 hlvr_file
 *
 * @author admin
 * @date 2025-11-13
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = HlvrFile.class)
public class HlvrFileVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * $column.columnComment
     */
    @ExcelProperty(value = "${comment}", converter = ExcelDictConvert.class)
    private Long id;

    /**
     * 资源类别
     */
    @ExcelProperty(value = "文件类别")
    private String assetType;

    @Translation(type = TransConstant.ID_TO_ATTACHMENT, mapper = "id")
    private List<HlvrAttachment> file;

    @Translation(type = TransConstant.USER_ID_TO_NICKNAME)
    private Long createBy;
    private Date createTime;

    private String name;
    @Translation(type = TransConstant.FULL_PATH)
    private String path;
    private String updateName;
    private String suffix;
    private Long businessId;
}
