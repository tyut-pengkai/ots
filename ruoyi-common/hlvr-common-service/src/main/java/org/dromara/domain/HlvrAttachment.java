package org.dromara.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.translation.annotation.Translation;
import org.dromara.common.translation.constant.TransConstant;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("hlvr_attachment")
public class HlvrAttachment extends BaseEntity {
    private Long id;
    private String name;
    private String path;
    private String updateName;
    private String suffix;
    private Long businessId;
}
