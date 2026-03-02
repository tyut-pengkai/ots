package org.dromara.domain.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.mybatis.core.domain.BaseEntity;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("hlvr_attachment")
public class HlvrAttachmentVo extends BaseEntity {
    private Long id;
    private String name;
    private String path;
    private String updateName;
    private Long businessId;
    private String suffix;
}
