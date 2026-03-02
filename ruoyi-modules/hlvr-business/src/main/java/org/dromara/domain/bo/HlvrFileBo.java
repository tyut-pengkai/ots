package org.dromara.domain.bo;

import org.dromara.domain.HlvrFile;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 文件资源管理业务对象 hlvr_file
 *
 * @author admin
 * @date 2025-11-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = HlvrFile.class, reverseConvertGenerate = false)
public class HlvrFileBo extends BaseEntity {

    /**
     * $column.columnComment
     */
    @NotNull(message = "$column.columnComment不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 资源类别
     */
    private String assetType;


}
