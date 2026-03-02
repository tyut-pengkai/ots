package org.dromara.service.impl;

import cn.hutool.core.bean.BeanUtil;
import org.dromara.common.core.domain.dto.AttachmentDTO;
import org.dromara.common.core.domain.dto.ElementDTO;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.core.service.ElementService;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.MessageUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.domain.HlvrSopCardStepOpt;
import org.dromara.mapper.HlvrSopCardStepOptMapper;
import org.springframework.stereotype.Service;
import org.dromara.domain.bo.HlvrElementBo;
import org.dromara.domain.vo.HlvrElementVo;
import org.dromara.domain.HlvrElement;
import org.dromara.mapper.HlvrElementMapper;
import org.dromara.service.IHlvrElementService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 资产管理Service业务层处理
 *
 * @author admin
 * @date 2025-11-12
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class HlvrElementServiceImpl implements IHlvrElementService, ElementService {

    private final HlvrElementMapper baseMapper;
    private final HlvrSopCardStepOptMapper stepOptMapper;

    /**
     * 查询资产管理
     *
     * @param id 主键
     * @return 资产管理
     */
    @Override
    public HlvrElementVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询资产管理列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 资产管理分页列表
     */
    @Override
    public TableDataInfo<HlvrElementVo> queryPageList(HlvrElementBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<HlvrElement> lqw = buildQueryWrapper(bo);
        lqw.orderByDesc(HlvrElement:: getUpdateTime);
        Page<HlvrElementVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的资产管理列表
     *
     * @param bo 查询条件
     * @return 资产管理列表
     */
    @Override
    public List<HlvrElementVo> queryList(HlvrElementBo bo) {
        LambdaQueryWrapper<HlvrElement> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 查询符合条件的资产列表，用于漫游展示设备模型简介
     *
     * @param bo 获取元素列表
     * @return 元素列表
     */
    @Override
    public List<HlvrElementVo> queryEquiOverviewList(HlvrElementBo bo) {
        // 只查询设备模型简介
        bo.setElementType("equipment_info");
        LambdaQueryWrapper<HlvrElement> lqw = buildQueryWrapper(bo);
        lqw.isNotNull(HlvrElement::getDescription)         // 排除 NULL
            .ne(HlvrElement::getDescription, "");      // 排除空字符串 ""
        // 排除 position,减少响应数据压力
        lqw.select(HlvrElement.class, info -> !info.getColumn().equals("description"));
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<HlvrElement> buildQueryWrapper(HlvrElementBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<HlvrElement> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getUeId()), HlvrElement::getUeId, bo.getUeId());
        lqw.eq(bo.getElementType() != null, HlvrElement::getElementType, bo.getElementType());
        lqw.like(StringUtils.isNotBlank(bo.getName()), HlvrElement::getName, bo.getName());
        lqw.eq(StringUtils.isNotBlank(bo.getDescription()), HlvrElement::getDescription, bo.getDescription());
        return lqw;
    }

    /**
     * 新增资产管理
     *
     * @param bo 资产管理
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(HlvrElementBo bo) {
        HlvrElement add = MapstructUtils.convert(bo, HlvrElement.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改资产管理
     *
     * @param bo 资产管理
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(HlvrElementBo bo) {
        HlvrElement update = MapstructUtils.convert(bo, HlvrElement.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(HlvrElement entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除资产管理信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid && ids != null && !ids.isEmpty()){
            //TODO 做一些业务上的校验,判断是否需要校验
            LambdaQueryWrapper<HlvrSopCardStepOpt> queryWrapper = Wrappers.lambdaQuery();
            queryWrapper.in(HlvrSopCardStepOpt::getMainObject, ids)
                .or()
                .in(HlvrSopCardStepOpt::getTargetObject, ids);
            Long count =  stepOptMapper.selectCount(queryWrapper);
            if (count > 0) {
                //所选资源正在被使用，无法删除
                throw new ServiceException(MessageUtils.message("element.delete.exist.training"));
            }
        }
        return baseMapper.deleteByIds(ids) > 0;
    }

    @Override
    public Boolean insertBatch(List<HlvrElementVo> bos) {
        return baseMapper.insertBatch(MapstructUtils.convert(bos, HlvrElement.class));
    }

    @Override
    public ElementDTO selectById(Long id) {
        HlvrElement element = baseMapper.selectById(id);
        return element == null? null: BeanUtil.copyProperties(element, ElementDTO.class);
    }
}
