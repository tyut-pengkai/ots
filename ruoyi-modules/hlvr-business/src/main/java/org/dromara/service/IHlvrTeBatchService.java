package org.dromara.service;

import org.dromara.domain.bo.HlvrTrainingExamBo;
import org.dromara.domain.vo.HlvrSopCardStepOptHistoryVo;
import org.dromara.domain.vo.HlvrTeBatchHistoryVo;
import org.dromara.domain.vo.HlvrTeBatchVo;
import org.dromara.domain.bo.HlvrTeBatchBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 培训/考核 批次Service接口
 *
 * @author admin
 * @date 2025-11-21
 */
public interface IHlvrTeBatchService {

    /**
     * 查询培训/考核 批次
     *
     * @param id 主键
     * @return 培训/考核 批次
     */
    HlvrTeBatchVo queryById(Long id);

    /**
     * 分页查询培训/考核 批次列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 培训/考核 批次分页列表
     */
    TableDataInfo<HlvrTeBatchVo> queryPageList(HlvrTeBatchBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的培训/考核 批次列表
     *
     * @param bo 查询条件
     * @return 培训/考核 批次列表
     */
    List<HlvrTeBatchVo> queryList(HlvrTeBatchBo bo);

    /**
     * 新增培训/考核 批次
     *
     * @param bo 培训/考核 批次
     * @return 是否新增成功
     */
    Long insertByBo(HlvrTeBatchBo bo);

    /**
     * 修改培训/考核 批次
     *
     * @param bo 培训/考核 批次
     * @return 是否修改成功
     */
    Boolean updateByBo(HlvrTeBatchBo bo);

    /**
     * 校验并批量删除培训/考核 批次信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    TableDataInfo<HlvrTeBatchHistoryVo> history(HlvrTrainingExamBo bo, PageQuery pageQuery);

    TableDataInfo<HlvrTeBatchHistoryVo> getExamPeople(long id, PageQuery pageQuery);

    List<HlvrTeBatchHistoryVo> peopleBatch(long id, long peopleId);

    /**
     * 获取参与培训的人员的批次得分详情
     * @param hscId     操作卡id
     * @param pageQuery 分页参数
     * @return
     */
    TableDataInfo<HlvrSopCardStepOptHistoryVo> getOpts(long hscId, PageQuery pageQuery);

    TableDataInfo<HlvrSopCardStepOptHistoryVo> optHistoryDetail(long hscId, PageQuery pageQuery);

    TableDataInfo<HlvrTeBatchHistoryVo> getPersonal(PageQuery pageQuery);

    List<HlvrTeBatchHistoryVo> personalBatch(Long id);
}
