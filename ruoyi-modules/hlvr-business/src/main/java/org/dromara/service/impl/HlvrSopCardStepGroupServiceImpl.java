package org.dromara.service.impl;

import cn.hutool.core.collection.CollectionUtil;
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
import org.dromara.domain.HlvrSopCardStepOpt;
import org.dromara.domain.HlvrSopCardStepResult;
import org.dromara.mapper.HlvrSopCardStepOptMapper;
import org.dromara.mapper.HlvrSopCardStepResultMapper;
import org.springframework.stereotype.Service;
import org.dromara.domain.bo.HlvrSopCardStepGroupBo;
import org.dromara.domain.vo.HlvrSopCardStepGroupVo;
import org.dromara.mapper.HlvrSopCardStepGroupMapper;
import org.dromara.service.IHlvrSopCardStepGroupService;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 操作卡步骤分组Service业务层处理
 *
 * @author Lion Li
 * @date 2025-11-13
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class HlvrSopCardStepGroupServiceImpl implements IHlvrSopCardStepGroupService {

    private final HlvrSopCardStepGroupMapper baseMapper;
    private final HlvrSopCardStepOptMapper stepOptMapper;
    private final HlvrSopCardStepResultMapper resultMapper;

    /**
     * 查询操作卡步骤分组
     *
     * @param id 主键
     * @return 操作卡步骤分组
     */
    @Override
    public HlvrSopCardStepGroupVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询操作卡步骤分组列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 操作卡步骤分组分页列表
     */
    @Override
    public TableDataInfo<HlvrSopCardStepGroupVo> queryPageList(HlvrSopCardStepGroupBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<HlvrSopCardStepGroup> lqw = buildQueryWrapper(bo);
        lqw.orderByAsc(HlvrSopCardStepGroup::getIndexing);
        Page<HlvrSopCardStepGroupVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的操作卡步骤分组列表
     *
     * @param bo 查询条件
     * @return 操作卡步骤分组列表
     */
    @Override
    public List<HlvrSopCardStepGroupVo> queryList(HlvrSopCardStepGroupBo bo) {
        LambdaQueryWrapper<HlvrSopCardStepGroup> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<HlvrSopCardStepGroup> buildQueryWrapper(HlvrSopCardStepGroupBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<HlvrSopCardStepGroup> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getHscId() != null, HlvrSopCardStepGroup::getHscId, bo.getHscId());
        lqw.like(StringUtils.isNotBlank(bo.getGroupName()), HlvrSopCardStepGroup::getGroupName, bo.getGroupName());
        lqw.eq(StringUtils.isNotBlank(bo.getGroupContent()), HlvrSopCardStepGroup::getGroupContent, bo.getGroupContent());
        lqw.eq(StringUtils.isNotBlank(bo.getGroupContentDetail()), HlvrSopCardStepGroup::getGroupContentDetail, bo.getGroupContentDetail());
        return lqw;
    }

    /**
     * 新增操作卡步骤分组
     *
     * @param bo 操作卡步骤分组
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(HlvrSopCardStepGroupBo bo) {
        HlvrSopCardStepGroup add = MapstructUtils.convert(bo, HlvrSopCardStepGroup.class);
        LambdaQueryWrapper<HlvrSopCardStepGroup> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(HlvrSopCardStepGroup::getHscId, bo.getHscId());
        queryWrapper.orderByDesc(HlvrSopCardStepGroup::getIndexing);
        queryWrapper.last("limit 1");
        HlvrSopCardStepGroupVo last = baseMapper.selectVoOne(queryWrapper);
        if(last != null) {
            add.setIndexing(last.getIndexing() + 10);
        } else {
            add.setIndexing(0);
        }

        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改操作卡步骤分组
     *
     * @param bo 操作卡步骤分组
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(HlvrSopCardStepGroupBo bo) {
        HlvrSopCardStepGroup update = MapstructUtils.convert(bo, HlvrSopCardStepGroup.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(HlvrSopCardStepGroup entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除操作卡步骤分组信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        QueryWrapper<HlvrSopCardStepOpt> query = new QueryWrapper<>();
        query.in("hscsg_id", ids);
        List<HlvrSopCardStepOpt> opts = stepOptMapper.selectList(query);
        stepOptMapper.delete(query);
        if(CollectionUtil.isNotEmpty(opts)){
            Set<Long> optsId = opts.stream().map(HlvrSopCardStepOpt::getId).collect(Collectors.toSet());
            QueryWrapper<HlvrSopCardStepResult> delResult = new QueryWrapper<>();
            delResult.in("hscso_id", optsId);
            resultMapper.delete(delResult);
        }
        return baseMapper.deleteByIds(ids) > 0;
    }
}
