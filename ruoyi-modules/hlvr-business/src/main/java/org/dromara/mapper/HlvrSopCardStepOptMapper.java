package org.dromara.mapper;

import org.dromara.domain.HlvrSopCardStepOpt;
import org.dromara.domain.vo.HlvrSopCardStepOptVo;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;

import java.util.List;

/**
 * 操作卡步骤Mapper接口
 *
 * @author Lion Li
 * @date 2025-11-07
 */
public interface HlvrSopCardStepOptMapper extends BaseMapperPlus<HlvrSopCardStepOpt, HlvrSopCardStepOptVo> {

    HlvrSopCardStepOptVo getFirstStepVo(Long cardId);

    HlvrSopCardStepOptVo getNextStepVo(Long currentStepId);

    int flushOptIndex(Long groupId, Integer indexing, String insertLocal);

    List<HlvrSopCardStepOptVo> getStepVosOrderBy(Long cardId, String order);
}
