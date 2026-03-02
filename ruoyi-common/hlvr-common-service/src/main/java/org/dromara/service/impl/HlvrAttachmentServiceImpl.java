package org.dromara.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.domain.dto.AttachmentDTO;
import org.dromara.common.core.domain.dto.UploadResultDTO;
import org.dromara.common.core.service.AttachmentService;
import org.dromara.common.core.service.AttachmentUrlService;
import org.dromara.common.core.service.ConfigService;
import org.dromara.common.core.utils.AttachmentUploadUtils;
import org.dromara.domain.HlvrAttachment;
import org.dromara.mapper.HlvrAttachmentMapper;
import org.dromara.properties.AttachmentUploadProperties;
import org.dromara.service.IHlvrAttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class HlvrAttachmentServiceImpl implements IHlvrAttachmentService, AttachmentService, AttachmentUrlService {
    private static final ThreadLocal<Long> BUSINESS_ID = new ThreadLocal<>();
    private final HlvrAttachmentMapper baseMapper;
    private final ConfigService configService;
    @Autowired
    AttachmentUploadProperties properties;
    @Override
    public HlvrAttachment uploadAttachment(MultipartFile file) {
        HlvrAttachment attachment = new HlvrAttachment();
        try {
            UploadResultDTO result = AttachmentUploadUtils.upload(properties.getPath(), file);
            attachment.setBusinessId(BUSINESS_ID.get());
            attachment.setName(result.getName());
            attachment.setPath(result.getPath());
            attachment.setUpdateName(result.getUpdateName());
            attachment.setSuffix(result.getName().substring(result.getName().lastIndexOf(".")));
            if(BUSINESS_ID.get() != null){
                QueryWrapper<HlvrAttachment> qw = new QueryWrapper<>();
                qw.eq("business_id",BUSINESS_ID.get());
                baseMapper.delete(qw);
            }
            baseMapper.insert(attachment);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return attachment;
    }

    public void setBusinessId(Long businessId){
        BUSINESS_ID.set(businessId);
    }

    public Long getBusinessId(){
        return BUSINESS_ID.get();
    }

    @Override
    public List<AttachmentDTO> selectAttachmentById(Long id) {
        QueryWrapper<HlvrAttachment> qw = new QueryWrapper<>();
        qw.eq("business_id", id);
        List<HlvrAttachment> attachments = baseMapper.selectList(qw);
        attachments.forEach(attachment -> {
            attachment.setPath(configService.getConfigValue("sys.oss.preview.prefix")
                + attachment.getPath());
        });
        return BeanUtil.copyToList(attachments, AttachmentDTO.class);
    }

    @Override
    public String selectAttachmentUrlById(Long id) {
        QueryWrapper<HlvrAttachment> qw = new QueryWrapper<>();
        qw.eq("id", id);
        AttachmentDTO dto = BeanUtil.copyProperties(baseMapper.selectOne(qw), AttachmentDTO.class);
        return dto == null ? null: configService.getConfigValue("sys.oss.preview.prefix")
            + dto.getPath();
    }

    @Override
    public String selectAttachmentUrlByBusinessId(Long id) {
        List<AttachmentDTO> fileList = selectAttachmentById(id);
        return fileList.get(0).getPath();
    }
}
