package org.dromara.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.domain.HlvrFile;
import org.dromara.domain.bo.HlvrFileBo;
import org.dromara.domain.vo.HlvrFileVo;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;

import java.util.List;

/**
 * 文件资源管理Mapper接口
 *
 * @author admin
 * @date 2025-11-13
 */
public interface HlvrFileMapper extends BaseMapperPlus<HlvrFile, HlvrFileVo> {

    List<HlvrFileVo> selectList(@Param("param")HlvrFileBo bo,
                                @Param("current") long current,
                                @Param("size")long size);
    Long queryTotal(@Param("param") HlvrFileBo bo);

    HlvrFileVo queryById(Long id);
}
