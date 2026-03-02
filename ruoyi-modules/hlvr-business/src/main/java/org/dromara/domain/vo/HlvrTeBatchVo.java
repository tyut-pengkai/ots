package org.dromara.domain.vo;

import org.dromara.domain.HlvrTeBatch;
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
 * 培训/考核 批次视图对象 hlvr_te_batch
 *
 * @author admin
 * @date 2025-11-21
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = HlvrTeBatch.class)
public class HlvrTeBatchVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ExcelProperty(value = "id")
    private Long id;

    /**
     * 培训，考核id
     */
    @ExcelProperty(value = "培训，考核id")
    private Long hteId;

    private Long htedId;
}
