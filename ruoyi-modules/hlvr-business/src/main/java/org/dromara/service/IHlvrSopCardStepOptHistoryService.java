package org.dromara.service;

import org.dromara.domain.vo.HlvrSopCardStepGroupVo;
import org.dromara.domain.vo.HlvrSopCardStepOptHistoryVo;
import org.dromara.domain.bo.HlvrSopCardStepOptHistoryBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.domain.vo.HlvrSopCardStepOptVo;

import java.util.Collection;
import java.util.List;

/**
 * 操作卡步骤记录Service接口
 *
 * @author admin
 * @date 2025-11-17
 */
public interface IHlvrSopCardStepOptHistoryService {

    /**
     * 查询操作卡步骤记录
     *
     * @param id 主键
     * @return 操作卡步骤记录
     */
    HlvrSopCardStepOptHistoryVo queryById(Long id);

    /**
     * 分页查询操作卡步骤记录列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 操作卡步骤记录分页列表
     */
    TableDataInfo<HlvrSopCardStepOptHistoryVo> queryPageList(HlvrSopCardStepOptHistoryBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的操作卡步骤记录列表
     *
     * @param bo 查询条件
     * @return 操作卡步骤记录列表
     */
    List<HlvrSopCardStepOptHistoryVo> queryList(HlvrSopCardStepOptHistoryBo bo);

    /**
     * 新增操作卡步骤记录
     *
     * @param bo 操作卡步骤记录
     * @return 是否新增成功
     */
    HlvrSopCardStepGroupVo insertByBo(HlvrSopCardStepOptHistoryBo bo);

    /**
     * 修改操作卡步骤记录
     *
     * @param bo 操作卡步骤记录
     * @return 是否修改成功
     */
    Boolean updateByBo(HlvrSopCardStepOptHistoryBo bo);

    /**
     * 校验并批量删除操作卡步骤记录信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
