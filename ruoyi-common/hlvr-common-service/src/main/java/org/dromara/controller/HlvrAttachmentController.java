package org.dromara.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.util.ObjectUtil;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.domain.HlvrAttachment;
import org.dromara.domain.vo.HlvrAttachmentVo;
import org.dromara.service.IHlvrAttachmentService;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/common/attachment")
public class HlvrAttachmentController {
    private final IHlvrAttachmentService attachmentService;
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public R<HlvrAttachment> upload(@RequestPart("file") MultipartFile file) {
        if (ObjectUtil.isNull(file)) {
            return R.fail("上传文件不能为空");
        }
        return R.ok(attachmentService.uploadAttachment(file));
    }
}
