package org.dromara.mapper;

import org.dromara.domain.HlvrTrainingExam;
import org.dromara.domain.vo.HlvrTeBatchHistoryVo;
import org.dromara.domain.vo.HlvrTrainingExamDetailVo;
import org.dromara.domain.vo.HlvrTrainingExamVo;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;

import java.util.List;

/**
 * 培训配置Mapper接口
 *
 * @author admin
 * @date 2025-11-20
 */
public interface HlvrTrainingExamMapper extends BaseMapperPlus<HlvrTrainingExam, HlvrTrainingExamVo> {
    List<HlvrTrainingExamVo> queryPageList(long deptId, long current, long size);

    long queryTotal(long deptId, String type);
    List<HlvrTrainingExamDetailVo> queryDetail(long deptId, String type, long current, long size);

    long queryExamTotal(long deptId,long userId, String type);
    List<HlvrTrainingExamDetailVo> queryExamDetail(long deptId, long userId, String type,  long current, long size);
}
