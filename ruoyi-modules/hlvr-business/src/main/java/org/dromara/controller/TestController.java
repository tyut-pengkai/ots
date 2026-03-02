package org.dromara.controller;

import cn.hutool.core.util.ObjectUtil;
import lombok.RequiredArgsConstructor;
import org.dromara.annotation.UploadAttachment;
import org.dromara.common.core.domain.R;
import org.dromara.service.IHlvrAttachmentService;
import org.dromara.domain.TestObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/resource/oss")
public class TestController {
    @Autowired
    private IHlvrAttachmentService attachmentService;
    @PostMapping(value = "/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @UploadAttachment
    public R<TestObject> file(@RequestPart("file") List<MultipartFile> file) throws IOException {
        if (ObjectUtil.isNull(file)) {
            return R.fail("上传文件不能为空");
        }
        Long businessId = 123L;
        TestObject testObject = new TestObject();
        testObject.setId(businessId);
//        SysOssVo oss = ossService.upload(file);
//        SysOssUploadVo uploadVo = new SysOssUploadVo();
//        uploadVo.setUrl(oss.getUrl());
//        uploadVo.setFileName(oss.getOriginalName());
//        uploadVo.setOssId(oss.getOssId().toString());
        return R.ok(testObject);
    }


}
