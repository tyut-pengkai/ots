package org.dromara.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.domain.HlvrSopCardStepGroup;
import org.dromara.domain.HlvrSopCardStepResult;
import org.dromara.domain.bo.HlvrSopCardStepResultBo;
import org.dromara.domain.vo.HlvrSopCardStepGroupVo;
import org.dromara.domain.vo.HlvrSopCardStepResultVo;
import org.dromara.mapper.HlvrSopCardStepGroupMapper;
import org.dromara.mapper.HlvrSopCardStepResultMapper;
import org.springframework.stereotype.Service;
import org.dromara.domain.bo.HlvrSopCardStepOptBo;
import org.dromara.domain.vo.HlvrSopCardStepOptVo;
import org.dromara.domain.HlvrSopCardStepOpt;
import org.dromara.mapper.HlvrSopCardStepOptMapper;
import org.dromara.service.IHlvrSopCardStepOptService;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 操作卡步骤Service业务层处理
 *
 * @author Lion Li
 * @date 2025-11-07
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class HlvrSopCardStepOptServiceImpl implements IHlvrSopCardStepOptService {

    private final HlvrSopCardStepOptMapper baseMapper;

    private final HlvrSopCardStepGroupMapper groupMapper;

    private final HlvrSopCardStepResultMapper resultMapper;

    /**
     * 查询操作卡步骤
     *
     * @param id 主键
     * @return 操作卡步骤
     */
    @Override
    public HlvrSopCardStepOptVo queryById(Long id){
        HlvrSopCardStepOptVo optVo = baseMapper.selectVoById(id);
        if(optVo != null){
            LambdaQueryWrapper<HlvrSopCardStepResult> lqw = new LambdaQueryWrapper<>();
            lqw.eq(HlvrSopCardStepResult::getHscsoId, optVo.getId());
            List<HlvrSopCardStepResultVo> results = resultMapper.selectVoList(lqw);
            Map<String, HlvrSopCardStepResultVo> map = new HashMap<>();
            for(HlvrSopCardStepResultVo vo: results) {
                vo.setChoose(true);
                map.put(vo.getResultType(), vo);
            }
            optVo.setEffect(map);
        }
        return optVo;
    }

    /**
     * 分页查询操作卡步骤列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 操作卡步骤分页列表
     */
    @Override
    public TableDataInfo<HlvrSopCardStepOptVo> queryPageList(HlvrSopCardStepOptBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<HlvrSopCardStepOpt> lqw = buildQueryWrapper(bo);
        Page<HlvrSopCardStepOptVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的操作卡步骤列表
     *
     * @param bo 查询条件
     * @return 操作卡步骤列表
     */
    @Override
    public List<HlvrSopCardStepOptVo> queryList(HlvrSopCardStepOptBo bo) {
        LambdaQueryWrapper<HlvrSopCardStepOpt> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<HlvrSopCardStepOpt> buildQueryWrapper(HlvrSopCardStepOptBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<HlvrSopCardStepOpt> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getOptContent()), HlvrSopCardStepOpt::getOptContent, bo.getOptContent());
        lqw.eq(bo.getVoicePlayTiming() != null, HlvrSopCardStepOpt::getVoicePlayTiming, bo.getVoicePlayTiming());
        lqw.eq(bo.getTargetObject() != null, HlvrSopCardStepOpt::getTargetObject, bo.getTargetObject());
        lqw.eq(bo.getMainObject() != null, HlvrSopCardStepOpt::getMainObject, bo.getMainObject());
        lqw.eq(StringUtils.isNotBlank(bo.getTargetObjectInitalStatus()), HlvrSopCardStepOpt::getTargetObjectInitalStatus, bo.getTargetObjectInitalStatus());
        lqw.eq(StringUtils.isNotBlank(bo.getTargetObjectEndingStatus()), HlvrSopCardStepOpt::getTargetObjectEndingStatus, bo.getTargetObjectEndingStatus());
        lqw.eq(bo.getOptType() != null, HlvrSopCardStepOpt::getOptType, bo.getOptType());
        lqw.eq(bo.getEventType() != null, HlvrSopCardStepOpt::getEventType, bo.getEventType());
        lqw.eq(StringUtils.isNotBlank(bo.getOptCommentary()), HlvrSopCardStepOpt::getOptCommentary, bo.getOptCommentary());
        lqw.eq(StringUtils.isNotBlank(bo.getErrorMsg()), HlvrSopCardStepOpt::getErrorMsg, bo.getErrorMsg());
        lqw.eq(bo.getErrorVoiceStatus() != null, HlvrSopCardStepOpt::getErrorVoiceStatus, bo.getErrorVoiceStatus());
        lqw.eq(StringUtils.isNotBlank(bo.getMainObjectInitalStatus()), HlvrSopCardStepOpt::getMainObjectInitalStatus, bo.getMainObjectInitalStatus());
        lqw.eq(StringUtils.isNotBlank(bo.getMainObjectEndingStatus()), HlvrSopCardStepOpt::getMainObjectEndingStatus, bo.getMainObjectEndingStatus());
        return lqw;
    }

    /**
     * 新增操作卡步骤
     *
     * @param bo 操作卡步骤
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(HlvrSopCardStepOptBo bo) {
        HlvrSopCardStepOpt add = MapstructUtils.convert(bo, HlvrSopCardStepOpt.class);
        LambdaQueryWrapper<HlvrSopCardStepOpt> optQuery = new LambdaQueryWrapper<>();
        optQuery.eq(HlvrSopCardStepOpt::getHscsgId, bo.getHscsgId());
        optQuery.orderByAsc(HlvrSopCardStepOpt::getIndexing);
        List<HlvrSopCardStepOptVo> opts = baseMapper.selectVoList(optQuery);
        Map<Long, HlvrSopCardStepOptVo> optMap
            = opts.stream().collect(Collectors.toMap(HlvrSopCardStepOptVo:: getId, Function.identity()));
        if(bo.getInsertLocal() != null){
            int indexing = optMap.get(bo.getId()).getIndexing();
            baseMapper.flushOptIndex(bo.getHscsgId(), indexing, bo.getInsertLocal());
            if(bo.getInsertLocal().equals("0") ) {
                add.setIndexing(indexing);
            } else {
                add.setIndexing(indexing + 10);
            }

            add.setId(null);// 置空id,否则会主键重复
        } else {
            if(CollectionUtil.isNotEmpty(opts)) {
                add.setIndexing(opts.get(opts.size() - 1).getIndexing() + 10);
            } else {
                add.setIndexing(0);
            }
        }

        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
//        List<HlvrSopCardStepResult> results = MapstructUtils.convert(bo.getEffect(), HlvrSopCardStepResult.class);
//        results.forEach(item -> {
//            item.setHscsoId(add.getId());
//        });
        Map<String, HlvrSopCardStepResultBo> effects =  bo.getEffect();
        for(Map.Entry<String, HlvrSopCardStepResultBo> effect: effects.entrySet()) {
            if(!effect.getValue().isChoose()) continue;
            HlvrSopCardStepResult result =
                MapstructUtils.convert(effect.getValue(), HlvrSopCardStepResult.class);
            result.setResultType(effect.getKey());
            result.setHscsoId(add.getId());
//            String attr = effect.getValue().getResourceAttr();
//            if(effect.getKey().equals("animation") && StringUtils.isNotBlank(attr)) {
//                JSONObject json = JSONUtil.parseObj(attr);
//                if( json.get("id") != null && StringUtils.isNotBlank(json.get("id").toString())){
//                    result.setResourceId(Long.valueOf(json.get("id").toString()));
//                }
//            }

            resultMapper.insert(result);
        }
//        resultMapper.insertBatch(results);
        return flag;
    }

    /**
     * 修改操作卡步骤
     *
     * @param bo 操作卡步骤
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(HlvrSopCardStepOptBo bo) {
        HlvrSopCardStepOpt update = MapstructUtils.convert(bo, HlvrSopCardStepOpt.class);
        validEntityBeforeSave(update);
        int index = baseMapper.updateById(update);
        QueryWrapper<HlvrSopCardStepResult>delete = new QueryWrapper<>();
        delete.eq("hscso_id", bo.getId());
        resultMapper.delete(delete);
        Map<String, HlvrSopCardStepResultBo> effects = bo.getEffect();
        for(Map.Entry<String, HlvrSopCardStepResultBo> effect: effects.entrySet()) {
            if(!effect.getValue().isChoose()) continue;
            HlvrSopCardStepResult result =
                MapstructUtils.convert(effect.getValue(), HlvrSopCardStepResult.class);
            result.setId(null);
            result.setResultType(effect.getKey());
            result.setHscsoId(bo.getId());
//            String attr = effect.getValue().getResourceAttr();
//            if(effect.getKey().equals("animation") && StringUtils.isNotBlank(attr)) {
//                JSONObject json = JSONUtil.parseObj(attr);
//                if( json.get("id") != null && StringUtils.isNotBlank(json.get("id").toString())){
//                    result.setResourceId(Long.valueOf(json.get("id").toString()));
//                }
//            }
            resultMapper.insert(result);
        }
        return index > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(HlvrSopCardStepOpt entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除操作卡步骤信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        QueryWrapper<HlvrSopCardStepResult>delete = new QueryWrapper<>();
        delete.in("hscso_id", ids);
        resultMapper.delete(delete);
        return baseMapper.deleteByIds(ids)> 0;
    }

    @Override
    public TableDataInfo<HlvrSopCardStepOptVo> getStepsByGroupId(Long id, PageQuery pageQuery) {
        LambdaQueryWrapper<HlvrSopCardStepOpt> lqw = Wrappers.lambdaQuery();
        lqw.orderByAsc(HlvrSopCardStepOpt::getIndexing);
        lqw.eq(HlvrSopCardStepOpt::getHscsgId, id);
        Page<HlvrSopCardStepOptVo> steps = baseMapper.selectVoPage(pageQuery.build(), lqw);
        for(HlvrSopCardStepOptVo stepVo: steps.getRecords()){
            LambdaQueryWrapper<HlvrSopCardStepResult> query = new LambdaQueryWrapper<>();
            query.eq(HlvrSopCardStepResult::getHscsoId, stepVo.getId());
            List<HlvrSopCardStepResultVo> results = resultMapper.selectVoList(query);
            Map<String, HlvrSopCardStepResultVo> map = new HashMap<>();
            for(HlvrSopCardStepResultVo vo: results) {
                vo.setChoose(true);
                map.put(vo.getResultType(), vo);
            }
            stepVo.setEffect(map);
        }
        return TableDataInfo.build(steps);
    }

    @Override
    public List<HlvrSopCardStepOptVo> getStepsByCardId(Long id) {
        return baseMapper.getStepVosOrderBy(id, "ASC");
    }
}
