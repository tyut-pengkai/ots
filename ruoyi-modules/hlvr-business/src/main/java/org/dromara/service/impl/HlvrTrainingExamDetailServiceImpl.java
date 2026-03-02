package org.dromara.service.impl;

import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.domain.HlvrTrainingExamDetail;
import org.dromara.domain.bo.HlvrTrainingExamDetailBo;
import org.dromara.domain.vo.HlvrTrainingExamDetailVo;
import org.springframework.stereotype.Service;
import org.dromara.mapper.HlvrTrainingExamDetailMapper;
import org.dromara.service.IHlvrTrainingExamDetailService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 培训配置详情Service业务层处理
 *
 * @author admin
 * @date 2025-11-20
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class HlvrTrainingExamDetailServiceImpl implements IHlvrTrainingExamDetailService {

    private final HlvrTrainingExamDetailMapper baseMapper;

    /**
     * 查询培训配置详情
     *
     * @param id 主键
     * @return 培训配置详情
     */
    @Override
    public HlvrTrainingExamDetailVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询培训配置详情列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 培训配置详情分页列表
     */
    @Override
    public TableDataInfo<HlvrTrainingExamDetailVo> queryPageList(HlvrTrainingExamDetailBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<HlvrTrainingExamDetail> lqw = buildQueryWrapper(bo);
        Page<HlvrTrainingExamDetailVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的培训配置详情列表
     *
     * @param bo 查询条件
     * @return 培训配置详情列表
     */
    @Override
    public List<HlvrTrainingExamDetailVo> queryList(HlvrTrainingExamDetailBo bo) {
        LambdaQueryWrapper<HlvrTrainingExamDetail> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<HlvrTrainingExamDetail> buildQueryWrapper(HlvrTrainingExamDetailBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<HlvrTrainingExamDetail> lqw = Wrappers.lambdaQuery();
        lqw.orderByAsc(HlvrTrainingExamDetail::getId);
        lqw.eq(bo.getHtId() != null, HlvrTrainingExamDetail::getHtId, bo.getHtId());
        lqw.eq(bo.getHscId() != null, HlvrTrainingExamDetail::getHscId, bo.getHscId());
        return lqw;
    }

    /**
     * 新增培训配置详情
     *
     * @param bo 培训配置详情
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(HlvrTrainingExamDetailBo bo) {
        HlvrTrainingExamDetail add = MapstructUtils.convert(bo, HlvrTrainingExamDetail.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改培训配置详情
     *
     * @param bo 培训配置详情
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(HlvrTrainingExamDetailBo bo) {
        HlvrTrainingExamDetail update = MapstructUtils.convert(bo, HlvrTrainingExamDetail.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(HlvrTrainingExamDetail entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除培训配置详情信息
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
