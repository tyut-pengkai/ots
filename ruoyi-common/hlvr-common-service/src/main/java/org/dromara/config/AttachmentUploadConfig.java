package org.dromara.config;

import org.dromara.properties.AttachmentUploadProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@AutoConfiguration
@EnableConfigurationProperties(AttachmentUploadProperties.class)
public class AttachmentUploadConfig {
}
