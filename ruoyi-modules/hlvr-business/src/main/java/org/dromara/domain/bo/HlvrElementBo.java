package org.dromara.domain.bo;

import org.dromara.domain.HlvrElement;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 资产管理业务对象 hlvr_element
 *
 * @author admin
 * @date 2025-11-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = HlvrElement.class, reverseConvertGenerate = false)
public class HlvrElementBo extends BaseEntity {

    /**
     * $column.columnComment
     */
    @NotNull(message = "$column.columnComment不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 模型id
     */
    private String ueId;

    /**
     * 模型类别字典值
     */
    @NotBlank(message = "模型类别不能为空", groups = { AddGroup.class, EditGroup.class })
    private String elementType;

    /**
     * 模型名称
     */
    private String name;

    /**
     * 坐标
     */
    private String position;

    /**
     * 描述
     */
    private String description;

}
