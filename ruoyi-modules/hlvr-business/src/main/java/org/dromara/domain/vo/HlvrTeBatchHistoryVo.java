package org.dromara.domain.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.common.translation.annotation.Translation;
import org.dromara.common.translation.constant.TransConstant;
import org.dromara.domain.HlvrTeBatch;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;


/**
 * 培训/考核 批次视图对象 hlvr_te_batch
 *
 * @author admin
 * @date 2025-11-21
 */
@Data
@ExcelIgnoreUnannotated
public class HlvrTeBatchHistoryVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;


    /**
     * 培训，考核id
     */
    private Long hteId;

    private String name;

    private long deptId;

    @Translation(type = TransConstant.DEPT_ID_TO_NAME, mapper = "deptId")
    private String deptName;

    private Integer optCardNum;

    private Integer num;

    // 实际与人数
    private Integer people;

    // 应参与人数
    private Integer peopleAcc;

    private long createBy;
    @Translation(type = TransConstant.USER_ID_TO_NICKNAME, mapper = "createBy")
    private long createName;

    private Integer totalScore;

    /**
     * 批次id
     */
    private Long batchId;

    /**
     * 操作卡id
     */
    private Long hscId;

    private Date createTime;

    private Integer costTime;
}
