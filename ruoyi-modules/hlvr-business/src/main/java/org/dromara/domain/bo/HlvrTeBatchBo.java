package org.dromara.domain.bo;

import org.dromara.domain.HlvrTeBatch;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 培训/考核 批次业务对象 hlvr_te_batch
 *
 * @author admin
 * @date 2025-11-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = HlvrTeBatch.class, reverseConvertGenerate = false)
public class HlvrTeBatchBo extends BaseEntity {

    /**
     * id
     */
    @NotNull(message = "id不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 培训，考核id
     */
    private Long hteId;

    /**
     * 培训，考核详情id
     */
    private Long htedId;
}
