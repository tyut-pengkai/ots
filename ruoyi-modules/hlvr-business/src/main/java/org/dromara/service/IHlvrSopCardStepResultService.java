package org.dromara.service;

import org.dromara.domain.vo.HlvrSopCardStepResultVo;
import org.dromara.domain.bo.HlvrSopCardStepResultBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 操作卡步骤影响结果Service接口
 *
 * @author Lion Li
 * @date 2025-11-07
 */
public interface IHlvrSopCardStepResultService {

    /**
     * 查询操作卡步骤影响结果
     *
     * @param id 主键
     * @return 操作卡步骤影响结果
     */
    HlvrSopCardStepResultVo queryById(Long id);

    /**
     * 分页查询操作卡步骤影响结果列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 操作卡步骤影响结果分页列表
     */
    TableDataInfo<HlvrSopCardStepResultVo> queryPageList(HlvrSopCardStepResultBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的操作卡步骤影响结果列表
     *
     * @param bo 查询条件
     * @return 操作卡步骤影响结果列表
     */
    List<HlvrSopCardStepResultVo> queryList(HlvrSopCardStepResultBo bo);

    /**
     * 新增操作卡步骤影响结果
     *
     * @param bo 操作卡步骤影响结果
     * @return 是否新增成功
     */
    Boolean insertByBo(HlvrSopCardStepResultBo bo);

    /**
     * 修改操作卡步骤影响结果
     *
     * @param bo 操作卡步骤影响结果
     * @return 是否修改成功
     */
    Boolean updateByBo(HlvrSopCardStepResultBo bo);

    /**
     * 校验并批量删除操作卡步骤影响结果信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 查询操作卡步骤影响结果
     *
     * @param hscsoId 步骤id
     * @return 操作卡步骤影响结果
     */
    HlvrSopCardStepResultVo selectPopwindowInfo(Long hscsoId);
}
