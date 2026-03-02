package org.dromara.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "attachment")
public class AttachmentUploadProperties {
    private String path;

    private String ip;

    private String port;

}
