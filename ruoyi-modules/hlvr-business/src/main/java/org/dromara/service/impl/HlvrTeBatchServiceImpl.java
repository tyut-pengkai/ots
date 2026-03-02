package org.dromara.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.satoken.utils.LoginHelper;
import org.dromara.domain.HlvrSopCardStepOptHistory;
import org.dromara.domain.bo.HlvrTrainingExamBo;
import org.dromara.domain.vo.*;
import org.dromara.mapper.*;
import org.springframework.stereotype.Service;
import org.dromara.domain.bo.HlvrTeBatchBo;
import org.dromara.domain.HlvrTeBatch;
import org.dromara.service.IHlvrTeBatchService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 培训/考核 批次Service业务层处理
 *
 * @author admin
 * @date 2025-11-21
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class HlvrTeBatchServiceImpl implements IHlvrTeBatchService {

    private final HlvrTeBatchMapper baseMapper;

    private final HlvrTeBatchHistoryMapper historyMapper;

    private final HlvrSopCardStepOptMapper optMapper;

    private final HlvrSopCardStepOptHistoryMapper optHistoryMapper;

    private final HlvrTrainingExamDetailMapper detailMapper;

    /**
     * 查询培训/考核 批次
     *
     * @param id 主键
     * @return 培训/考核 批次
     */
    @Override
    public HlvrTeBatchVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询培训/考核 批次列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 培训/考核 批次分页列表
     */
    @Override
    public TableDataInfo<HlvrTeBatchVo> queryPageList(HlvrTeBatchBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<HlvrTeBatch> lqw = buildQueryWrapper(bo);
        Page<HlvrTeBatchVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的培训/考核 批次列表
     *
     * @param bo 查询条件
     * @return 培训/考核 批次列表
     */
    @Override
    public List<HlvrTeBatchVo> queryList(HlvrTeBatchBo bo) {
        LambdaQueryWrapper<HlvrTeBatch> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<HlvrTeBatch> buildQueryWrapper(HlvrTeBatchBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<HlvrTeBatch> lqw = Wrappers.lambdaQuery();
        lqw.orderByAsc(HlvrTeBatch::getId);
        lqw.eq(bo.getHteId() != null, HlvrTeBatch::getHteId, bo.getHteId());
        return lqw;
    }

    /**
     * 新增培训/考核 批次
     *
     * @param bo 培训/考核 批次
     * @return 是否新增成功
     */
    @Override
    public Long insertByBo(HlvrTeBatchBo bo) {
        HlvrTeBatch add = MapstructUtils.convert(bo, HlvrTeBatch.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
            HlvrTrainingExamDetailVo detailVo = detailMapper.selectVoById(bo.getHtedId());
            HlvrSopCardStepOptVo first = optMapper.getFirstStepVo(detailVo.getHscId());
            if(first != null) {
                HlvrSopCardStepOptHistory optHistory = new HlvrSopCardStepOptHistory();
                optHistory.setHscsoId(first.getId());
                optHistory.setBatchId(bo.getId());
                optHistory.setOptContent(first.getOptContent());
                optHistory.setStepName(first.getGroupName());
                optHistoryMapper.insert(optHistory);
            }
        }
        return add.getId();
    }

    /**
     * 修改培训/考核 批次
     *
     * @param bo 培训/考核 批次
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(HlvrTeBatchBo bo) {
        HlvrTeBatch update = MapstructUtils.convert(bo, HlvrTeBatch.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(HlvrTeBatch entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除培训/考核 批次信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteByIds(ids) > 0;
    }

    @Override
    public TableDataInfo<HlvrTeBatchHistoryVo> history(HlvrTrainingExamBo bo, PageQuery pageQuery) {
        Page<HlvrTeBatchHistoryVo> page = pageQuery.build();
        IPage<HlvrTeBatchHistoryVo> result = historyMapper.queryHistory(page, bo);
        return TableDataInfo.build(result);
    }

    @Override
    public TableDataInfo<HlvrTeBatchHistoryVo> getExamPeople(long id, PageQuery pageQuery) {
        Page<HlvrTeBatchHistoryVo> page = pageQuery.build();
        IPage<HlvrTeBatchHistoryVo> result = historyMapper.getExamPeople(page, id);
        return TableDataInfo.build(result);
    }

    @Override
    public List<HlvrTeBatchHistoryVo> peopleBatch(long id, long peopleId) {
        return historyMapper.peopleBatch(id, peopleId);
    }

    @Override
    public TableDataInfo<HlvrSopCardStepOptHistoryVo> getOpts(long hscId, PageQuery pageQuery) {
        Page<HlvrSopCardStepOptHistoryVo> page = pageQuery.build();
        IPage<HlvrSopCardStepOptHistoryVo> result = historyMapper.getOpts(page, hscId);
        return TableDataInfo.build(result);
    }

    @Override
    public TableDataInfo<HlvrSopCardStepOptHistoryVo> optHistoryDetail(long batchId, PageQuery pageQuery) {
        return TableDataInfo.build(historyMapper.optHistoryDetail(pageQuery.build(), batchId));
    }

    @Override
    public TableDataInfo<HlvrTeBatchHistoryVo> getPersonal(PageQuery pageQuery) {
        IPage<HlvrTeBatchHistoryVo> page = pageQuery.build();
        List<HlvrTeBatchHistoryVo> result = historyMapper.queryPersonal(LoginHelper.getUserId(), page.getCurrent(), page.getSize());
        page.setRecords(result);
        long total = historyMapper.queryPersonalTotal(LoginHelper.getUserId());
        page.setTotal(total);
        return TableDataInfo.build(page);
    }

    @Override
    public List<HlvrTeBatchHistoryVo> personalBatch(Long id) {
        return historyMapper.peopleBatch(id, LoginHelper.getUserId());
    }
}
