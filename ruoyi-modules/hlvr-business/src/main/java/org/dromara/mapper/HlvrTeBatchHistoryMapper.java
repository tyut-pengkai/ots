package org.dromara.mapper;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.dromara.domain.bo.HlvrTeBatchBo;
import org.dromara.domain.bo.HlvrTrainingExamBo;
import org.dromara.domain.vo.HlvrSopCardStepOptHistoryVo;
import org.dromara.domain.vo.HlvrTeBatchHistoryVo;

import java.util.List;

/**
 * 培训/考核 批次Mapper接口
 *
 * @author admin
 * @date 2025-11-21
 */
public interface HlvrTeBatchHistoryMapper {
    IPage<HlvrTeBatchHistoryVo> queryHistory(Page<HlvrTeBatchHistoryVo> page,
                                            @Param(value = "param") HlvrTrainingExamBo bo);

    IPage<HlvrTeBatchHistoryVo> getExamPeople(Page<HlvrTeBatchHistoryVo> page, long hteId);

    List<HlvrTeBatchHistoryVo> peopleBatch(long id,
                                             long createBy);


    IPage<HlvrSopCardStepOptHistoryVo> getOpts(Page<HlvrSopCardStepOptHistoryVo> page,
                                                          @Param(value = "hscId") long hscId);

    IPage<HlvrSopCardStepOptHistoryVo> optHistoryDetail(Page<HlvrSopCardStepOptHistoryVo> page,
                                               @Param(value = "batchId") long batchId);

    List<HlvrTeBatchHistoryVo> queryPersonal(long createBy,
                                            long current,
                                            long size);

    long queryPersonalTotal(long createBy);
}
