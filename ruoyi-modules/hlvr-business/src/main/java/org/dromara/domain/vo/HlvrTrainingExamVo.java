package org.dromara.domain.vo;

import org.dromara.common.translation.annotation.Translation;
import org.dromara.common.translation.constant.TransConstant;
import org.dromara.domain.HlvrTrainingExam;
import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * 培训配置视图对象 hlvr_training
 *
 * @author admin
 * @date 2025-11-20
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = HlvrTrainingExam.class)
public class HlvrTrainingExamVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ExcelProperty(value = "id")
    private Long id;

    /**
     * 培训名称
     */
    @ExcelProperty(value = "培训名称")
    private String name;

    /**
     * 参与组织
     */
    @ExcelProperty(value = "参与组织")
    private Long deptId;

    /**
     * 培训类型 train=培训，exam=考核
     */
    private String type;

    @Translation(type = TransConstant.DEPT_ID_TO_NAME, mapper = "deptId")
    private String deptName;

    private List<HlvrTrainingExamDetailVo> details;

    private Date updateTime;

    @Translation(type = TransConstant.USER_ID_TO_NICKNAME)
    private String createBy;
}
