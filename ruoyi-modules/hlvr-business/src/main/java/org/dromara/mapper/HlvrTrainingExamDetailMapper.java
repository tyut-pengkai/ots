package org.dromara.mapper;

import org.dromara.domain.HlvrTrainingExamDetail;
import org.dromara.domain.vo.HlvrTrainingExamDetailVo;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;

import java.util.List;

/**
 * 培训配置详情Mapper接口
 *
 * @author admin
 * @date 2025-11-20
 */
public interface HlvrTrainingExamDetailMapper extends BaseMapperPlus<HlvrTrainingExamDetail, HlvrTrainingExamDetailVo> {
    List<HlvrTrainingExamDetailVo> queryList(Long htId);
}
