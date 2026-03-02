package org.dromara.common.core.utils;

import org.apache.commons.io.FilenameUtils;
import org.dromara.common.core.domain.dto.UploadResultDTO;
import org.dromara.common.core.utils.file.MimeTypeUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

/**
 * 文件上传工具类
 *
 * @author ruoyi
 */
@Component
public class AttachmentUploadUtils {

    /**
     * 根据文件路径上传
     *
     * @param path 上传路径
     * @param file 上传的文件
     * @return 文件名称
     * @throws IOException
     */
    public static UploadResultDTO upload(String path, MultipartFile file) throws IOException {
        try {
            return upload(path, file, MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION);
        }
        catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        }
    }

    /**
     * 文件上传
     *
     * @param baseDir 相对应用的基目录
     * @param file 上传的文件
     * @param allowedExtension 上传文件类型
     * @return 返回上传成功的文件名
     * @throws IOException 比如读写文件出错时
     */
    public static UploadResultDTO upload(String baseDir, MultipartFile file, String[] allowedExtension)
            throws IOException {
        if(assertAllowed(file, allowedExtension)) {
            String fileName = extractFilename(file);
            String absPath = getAbsoluteFile(baseDir, fileName).getAbsolutePath();
            file.transferTo(Paths.get(absPath));
            return UploadResultDTO.builder()
                .path("/attachment/" + fileName)
                .name(file.getOriginalFilename())
                .updateName(getPathFileName(fileName))
                .build();
        } else {
            throw new RuntimeException("文件类型不支持");
        }
    }
    /**
     * 编码文件名
     */
    public static String extractFilename(MultipartFile file) {
        return StringUtils.format("{}/{}.{}", UUID.randomUUID(),
            UUID.randomUUID(), getExtension(file));
    }

    public static File getAbsoluteFile(String uploadDir, String fileName) {
        File desc = new File(uploadDir + File.separator + fileName);
        if (!desc.exists()) {
            if (!desc.getParentFile().exists()) {
                desc.getParentFile().mkdirs();
            }
        }
        return desc;
    }

    public static String getPathFileName(String fileName) throws IOException
    {
        return StringUtils.substring(fileName, fileName.lastIndexOf("/") + 1);
    }

    /**
     * 文件大小校验
     *
     * @param file 上传的文件
     */
    public static boolean assertAllowed(MultipartFile file, String[] allowedExtension){
        String extension = getExtension(file);
        return allowedExtension != null && isAllowedExtension(extension, allowedExtension);
    }

    /**
     * 判断MIME类型是否是允许的MIME类型
     *
     * @param extension
     * @param allowedExtension
     * @return
     */
    public static boolean isAllowedExtension(String extension, String[] allowedExtension) {
        for (String str : allowedExtension) {
            if (str.equalsIgnoreCase(extension)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取文件名的后缀
     *
     * @param file 表单文件
     * @return 后缀名
     */
    public static String getExtension(MultipartFile file) {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (StringUtils.isEmpty(extension)) {
            extension = MimeTypeUtils.getExtension(Objects.requireNonNull(file.getContentType()));
        }
        return extension;
    }
}
