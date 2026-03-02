package org.dromara.domain.vo;

import org.dromara.common.translation.annotation.Translation;
import org.dromara.common.translation.constant.TransConstant;
import org.dromara.domain.HlvrTrainingExamDetail;
import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;


/**
 * 培训配置详情视图对象 hlvr_training_detail
 *
 * @author admin
 * @date 2025-11-20
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = HlvrTrainingExamDetail.class)
public class HlvrTrainingExamDetailVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ExcelProperty(value = "id")
    private Long id;

    /**
     * 培训配置表id
     */
    @ExcelProperty(value = "培训配置表id")
    private Long htId;
    private String htName;
    /**
     * 操作卡id
     */
    @ExcelProperty(value = "操作卡id")
    private Long hscId;

    //@Translation(type = TransConstant.ID_TO_CARD, mapper = "hscId")
    private String name;

    private String initialZB;

    @ExcelProperty(value = "参与组织")
    private Long deptId;

    @Translation(type = TransConstant.DEPT_ID_TO_NAME, mapper = "deptId")
    private String deptName;

}
