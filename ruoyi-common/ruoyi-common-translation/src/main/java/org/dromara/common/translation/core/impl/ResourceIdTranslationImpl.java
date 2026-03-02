package org.dromara.common.translation.core.impl;

import cn.hutool.core.util.ObjectUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.service.AttachmentUrlService;
import org.dromara.common.core.service.ConfigService;
import org.dromara.common.core.service.ElementService;
import org.dromara.common.core.utils.reflect.ReflectUtils;
import org.dromara.common.json.utils.JsonUtils;
import org.dromara.common.translation.annotation.TranslationType;
import org.dromara.common.translation.constant.TransConstant;
import org.dromara.common.translation.core.TranslationInterface;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;


@AllArgsConstructor
@TranslationType(type = TransConstant.RESOURCE_ID_TO_ELEMENT)
@Slf4j
public class ResourceIdTranslationImpl implements TranslationInterface<Object> {
    private static List<String> FILE_RESOURCE_TYPE = List.of("document", "video", "image");
    private static List<String> QUESTION_RESOURCE_TYPE = List.of("questions");
    private final ElementService elementService;
    private final AttachmentUrlService fileService;
    private final ConfigService configService;
    @Override
    public Object translation(Object key, String other) {
       return null;
    }

    @Override
    public Object translation(Object key, String other, JsonGenerator gen, boolean bool, String suffix) {
        if(ObjectUtil.isEmpty(key)){
            return null;
        }
        try {
            Object object = gen.currentValue();
            Long hscsoId = (Long) ReflectUtils.invokeGetter(object, "hscsoId");
            String resultType = ReflectUtils.invokeGetter(object, other);
            Map<String, Object> resourceAttr = JsonUtils.parseMap((String) key);

            String path = "";
            String webpath = "";
            String ueId = "";
            if(resultType.equals("popwindow")){
                if(FILE_RESOURCE_TYPE.contains(resourceAttr.get("popwindowType"))) {
                    path = fileService.selectAttachmentUrlByBusinessId(Long.valueOf(resourceAttr.get("id").toString()));
                    //if(!"image".equals(resourceAttr.get("popwindowType"))){
                    webpath=configService.getConfigValue("sys.oss.preview.prefix") + "/viewResource?id=" + hscsoId;
                    //}
                } else if(QUESTION_RESOURCE_TYPE.contains(resourceAttr.get("popwindowType"))){
                    path = configService.getConfigValue("sys.oss.preview.prefix") + "/viewTopic?id=" + resourceAttr.get("id") + "&isView=0";
                    webpath = configService.getConfigValue("sys.oss.preview.prefix") + "/viewTopic?id=" + resourceAttr.get("id") + "&isView=0";
                }
                //如果富文本
                if("textarea".equals(resourceAttr.get("popwindowType"))){
                    webpath=configService.getConfigValue("sys.oss.preview.prefix") + "/viewResource?id=" + hscsoId;
                }
            } else if(resultType.equals("animation")) {
                ueId = elementService.selectById(Long.valueOf(resourceAttr.get("id").toString())).getUeId();
            }
            Class<?> clazz = object.getClass();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                if(field.getName().equals("resourceUeId")) {
                    field.set(object, ueId);
                } else if(field.getName().equals("resourcePath")){
                    field.set(object, path);
                }else if(field.getName().equals("resourceWebPath")){
                    field.set(object, webpath);
                }
            }
        } catch (Exception e) {
            log.error("ResourceIdTranslation exception", e);
            throw new RuntimeException(e);
        }
        return null;
    }
}
