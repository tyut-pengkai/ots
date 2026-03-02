package org.dromara.domain.vo;

import org.dromara.common.translation.annotation.Translation;
import org.dromara.domain.HlvrSopCardStepOptHistory;
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
 * 操作卡步骤记录视图对象 hlvr_sop_card_step_opt_history
 *
 * @author admin
 * @date 2025-11-17
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = HlvrSopCardStepOptHistory.class)
public class HlvrSopCardStepOptHistoryVo implements Serializable {

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
    @ExcelProperty(value = "id")
    private Long hscId;

    /**
     * 操作卡步骤id
     */
    @ExcelProperty(value = "操作卡步骤id")
    private Long hscsoId;



    /**
     * 操作卡分组id
     */
    private Long hscsgId;

    /**
     * 用户得分
     */
    @ExcelProperty(value = "用户得分")
    private Integer score;

    /**
     * 下一步操作步骤id
     */
    @ExcelProperty(value = "下一步操作步骤id")
    private Long nextStep;

    private Long batchId;
    /**
     * 步骤名称
     */
    private String groupName;
    /**
     * 操作内容
     */
    private String optContent;

    private Long costTime;
    private Date createTime;
}
