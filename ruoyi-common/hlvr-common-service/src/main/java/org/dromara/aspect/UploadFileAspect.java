package org.dromara.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.dromara.common.core.domain.R;
import org.dromara.service.IHlvrAttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;

@Aspect
@Component
public class UploadFileAspect {

    @Autowired
    IHlvrAttachmentService attachmentService;
    @Pointcut("@annotation(org.dromara.annotation.UploadAttachment)")
    public void fileUploadBeginPoint() {}

    @AfterReturning(
        pointcut = "fileUploadBeginPoint()",
        returning = "result"
    )
    public void beginFileUpload(JoinPoint joinPoint, Object result) {
        try {
            if (result instanceof R) {
                R<?> response = (R<?>) result;
                Class<?> clazz = response.getData().getClass();
                Field field = clazz.getDeclaredField("id");
                field.setAccessible(true);
                Object value = field.get(response.getData());
                attachmentService.setBusinessId(Long.parseLong(value.toString()));
                List<MultipartFile> files = extractRequestPartFiles(joinPoint);
                if(files != null && !files.isEmpty()) {
                    for(MultipartFile file: files) {
                        if(!file.isEmpty()){
                            attachmentService.uploadAttachment(file);
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<MultipartFile> extractRequestPartFiles(JoinPoint joinPoint) {
        try {
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            Method method = methodSignature.getMethod();
            Parameter[] parameters = method.getParameters();
            Object[] args = joinPoint.getArgs();
            for (int i = 0; i < parameters.length; i++) {
                Parameter parameter = parameters[i];
                RequestPart requestPart = parameter.getAnnotation(RequestPart.class);
                if (requestPart != null && "file".equals(requestPart.value())) {
                    Object arg = args[i];
                    if (arg instanceof List) {
                        List<?> list = (List<?>) arg;
                        if (!list.isEmpty() && list.get(0) instanceof MultipartFile) {
                            return (List<MultipartFile>) list;
                        }
                    }
                }
            }
        } catch (Exception e) {
        }
        return null;
    }
}
