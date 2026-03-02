package org.dromara.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.dromara.common.core.constant.SystemConstants;
import org.dromara.common.core.domain.model.LoginUser;
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
import org.dromara.domain.*;
import org.dromara.domain.vo.HlvrQuestionItemVo;
import org.dromara.domain.vo.HlvrQuestionVo;
import org.dromara.domain.vo.HlvrSopCardStepGroupVo;
import org.dromara.domain.vo.HlvrSopCardStepOptVo;
import org.dromara.domain.vo.HlvrSopCardStepResultVo;
import org.dromara.mapper.*;
import org.springframework.stereotype.Service;
import org.dromara.domain.bo.HlvrSopCardStepOptHistoryBo;
import org.dromara.domain.vo.HlvrSopCardStepOptHistoryVo;
import org.dromara.service.IHlvrSopCardStepOptHistoryService;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 操作卡步骤记录Service业务层处理
 *
 * @author admin
 * @date 2025-11-17
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class HlvrSopCardStepOptHistoryServiceImpl implements IHlvrSopCardStepOptHistoryService {

    private final HlvrSopCardStepOptHistoryMapper baseMapper;
    private final HlvrSopCardStepOptMapper stepOptMapper;
    private final HlvrTeBatchMapper batchMapper;
    //操作卡分组mapper
    private final HlvrSopCardStepGroupMapper groupMapper;
    //操作卡响应结果mapper
    private final HlvrSopCardStepResultMapper resultMapper;
    /**
     * 查询操作卡步骤记录
     *
     * @param id 主键
     * @return 操作卡步骤记录
     */
    @Override
    public HlvrSopCardStepOptHistoryVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询操作卡步骤记录列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 操作卡步骤记录分页列表
     */
    @Override
    public TableDataInfo<HlvrSopCardStepOptHistoryVo> queryPageList(HlvrSopCardStepOptHistoryBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<HlvrSopCardStepOptHistory> lqw = buildQueryWrapper(bo);
        Page<HlvrSopCardStepOptHistoryVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的操作卡步骤记录列表
     *
     * @param bo 查询条件
     * @return 操作卡步骤记录列表
     */
    @Override
    public List<HlvrSopCardStepOptHistoryVo> queryList(HlvrSopCardStepOptHistoryBo bo) {
        LambdaQueryWrapper<HlvrSopCardStepOptHistory> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<HlvrSopCardStepOptHistory> buildQueryWrapper(HlvrSopCardStepOptHistoryBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<HlvrSopCardStepOptHistory> lqw = Wrappers.lambdaQuery();
        lqw.orderByAsc(HlvrSopCardStepOptHistory::getId);
        lqw.eq(bo.getHscsoId() != null, HlvrSopCardStepOptHistory::getHscsoId, bo.getHscsoId());
        lqw.eq(bo.getNextStep() != null, HlvrSopCardStepOptHistory::getNextStep, bo.getNextStep());
        return lqw;
    }

    /**
     * 新增操作卡步骤记录
     *
     * @param bo 操作卡步骤记录
     * @return 是否新增成功
     */
    @Override
    @Transactional
    public HlvrSopCardStepGroupVo insertByBo(HlvrSopCardStepOptHistoryBo bo) {
        HlvrSopCardStepOptHistoryVo current = baseMapper.selectVoOne(
            new QueryWrapper<HlvrSopCardStepOptHistory>()
                .eq("hscso_id", bo.getHscsoId())
                .eq("batch_id", bo.getBatchId())
        );
        HlvrSopCardStepOptVo nextStepVo = current.getNextStep() == null
            ? stepOptMapper.getNextStepVo(bo.getHscsoId())
            : stepOptMapper.selectVoById(current.getNextStep());
        baseMapper.update(null, new LambdaUpdateWrapper<HlvrSopCardStepOptHistory>()
            .set(HlvrSopCardStepOptHistory::getEndTime, new Date())
            .set(bo.getScore() != null, HlvrSopCardStepOptHistory::getScore, bo.getScore())
            .set(nextStepVo != null, HlvrSopCardStepOptHistory::getNextStep, nextStepVo != null ? nextStepVo.getId() : null)
            .eq(HlvrSopCardStepOptHistory::getBatchId, bo.getBatchId())
            .eq(HlvrSopCardStepOptHistory::getHscsoId, bo.getHscsoId()));
        batchMapper.update(null, new LambdaUpdateWrapper<HlvrTeBatch>()
            .set(HlvrTeBatch::getEndTime, new Date())
            .eq(HlvrTeBatch::getId, bo.getBatchId()));
        if (nextStepVo == null) return new HlvrSopCardStepGroupVo();

        HlvrSopCardStepGroupVo groupVo = groupMapper.selectVoById(nextStepVo.getHscsgId());
        groupVo.setOpts(Collections.singletonList(nextStepVo));
        // 获取操作卡响应结果
        LambdaQueryWrapper<HlvrSopCardStepResult> lqw = new LambdaQueryWrapper<>();
        lqw.eq(HlvrSopCardStepResult::getHscsoId, nextStepVo.getId());
        List<HlvrSopCardStepResultVo> effects = resultMapper.selectVoList(lqw);
        Map<String, HlvrSopCardStepResultVo> map = new HashMap<>();
        for(HlvrSopCardStepResultVo vo: effects) {
            vo.setChoose(true);
            map.put(vo.getResultType(), vo);
        }
        nextStepVo.setEffect(map);
//        query.eq("create_by", LoginHelper.getLoginUser().getUserId());
        // 检查并初始化下一步历史记录
        if (baseMapper.selectVoOne(new QueryWrapper<HlvrSopCardStepOptHistory>()
            .eq("hscso_id", nextStepVo.getId())
            .eq("batch_id", bo.getBatchId())) == null) {
            HlvrSopCardStepOptHistory history = new HlvrSopCardStepOptHistory();
            history.setHscsoId(nextStepVo.getId());
            history.setBatchId(bo.getBatchId());
            history.setOptContent(nextStepVo.getOptContent());
            history.setStepName(groupVo.getGroupName());
            baseMapper.insert(history);
        }
        return groupVo;
    }

    /**
     * 修改操作卡步骤记录
     *
     * @param bo 操作卡步骤记录
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(HlvrSopCardStepOptHistoryBo bo) {
        HlvrSopCardStepOptHistory update = MapstructUtils.convert(bo, HlvrSopCardStepOptHistory.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(HlvrSopCardStepOptHistory entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除操作卡步骤记录信息
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
}
