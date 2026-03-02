package org.dromara.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
import org.dromara.domain.HlvrTrainingExamDetail;
import org.dromara.domain.bo.HlvrTrainingExamBo;
import org.dromara.domain.bo.HlvrTrainingExamDetailBo;
import org.dromara.domain.vo.HlvrTeBatchHistoryVo;
import org.dromara.domain.vo.HlvrTrainingExamDetailVo;
import org.dromara.domain.vo.HlvrTrainingExamVo;
import org.dromara.mapper.HlvrTrainingExamDetailMapper;
import org.springframework.stereotype.Service;
import org.dromara.domain.HlvrTrainingExam;
import org.dromara.mapper.HlvrTrainingExamMapper;
import org.dromara.service.IHlvrTrainingExamService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 培训配置Service业务层处理
 *
 * @author admin
 * @date 2025-11-20
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class HlvrTrainingExamServiceImpl implements IHlvrTrainingExamService {

    private final HlvrTrainingExamMapper baseMapper;
    private final HlvrTrainingExamDetailMapper detailMapper;

    /**
     * 查询培训配置
     *
     * @param id 主键
     * @return 培训配置
     */
    @Override
    public HlvrTrainingExamVo queryById(Long id){
        HlvrTrainingExamVo result = baseMapper.selectVoById(id);
        result.setDetails(detailMapper.queryList(id));
        return result;
    }

    /**
     * 分页查询培训配置列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 培训配置分页列表
     */
    @Override
    public TableDataInfo<HlvrTrainingExamVo> queryPageList(HlvrTrainingExamBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<HlvrTrainingExam> lqw = buildQueryWrapper(bo);
        Page<HlvrTrainingExamVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    @Override
    public TableDataInfo<HlvrTrainingExamDetailVo> queryPageList(PageQuery pageQuery) {
        LambdaQueryWrapper<HlvrTrainingExam> lqw = new LambdaQueryWrapper<>();
        lqw.eq(HlvrTrainingExam::getDeptId, LoginHelper.getDeptId());
        lqw.orderByDesc(HlvrTrainingExam::getCreateTime);
        IPage<HlvrTrainingExamDetailVo> page = pageQuery.build();
        List<HlvrTrainingExamDetailVo> result = baseMapper.queryDetail(LoginHelper.getDeptId(), "train",page.getCurrent(), page.getSize());
        page.setRecords(result);
        long total = baseMapper.queryTotal(LoginHelper.getDeptId(), "train");
        page.setTotal(total);
        return TableDataInfo.build(result);
    }

    @Override
    public TableDataInfo<HlvrTrainingExamDetailVo> queryExamList(PageQuery pageQuery) {
        LambdaQueryWrapper<HlvrTrainingExam> lqw = new LambdaQueryWrapper<>();
        lqw.eq(HlvrTrainingExam::getDeptId, LoginHelper.getDeptId());
        lqw.orderByDesc(HlvrTrainingExam::getCreateTime);
        IPage<HlvrTrainingExamDetailVo> page = pageQuery.build();
        List<HlvrTrainingExamDetailVo> result = baseMapper.queryDetail(LoginHelper.getDeptId(),
            "exam",page.getCurrent(), page.getSize());
        page.setRecords(result);
        long total = baseMapper.queryTotal(LoginHelper.getDeptId(), "exam");
        page.setTotal(total);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的培训配置列表
     *
     * @param bo 查询条件
     * @return 培训配置列表
     */
    @Override
    public List<HlvrTrainingExamVo> queryList(HlvrTrainingExamBo bo) {
        LambdaQueryWrapper<HlvrTrainingExam> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<HlvrTrainingExam> buildQueryWrapper(HlvrTrainingExamBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<HlvrTrainingExam> lqw = Wrappers.lambdaQuery();
        lqw.orderByDesc(HlvrTrainingExam::getUpdateTime);
        lqw.like(StringUtils.isNotBlank(bo.getName()), HlvrTrainingExam::getName, bo.getName());
        lqw.eq(bo.getDeptId() != null, HlvrTrainingExam::getDeptId, bo.getDeptId());
        lqw.eq(StringUtils.isNotBlank(bo.getType()), HlvrTrainingExam::getType, bo.getType());
        return lqw;
    }

    /**
     * 新增培训配置
     *
     * @param bo 培训配置
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(HlvrTrainingExamBo bo) {
        HlvrTrainingExam add = MapstructUtils.convert(bo, HlvrTrainingExam.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
            List<HlvrTrainingExamDetailBo>details = bo.getDetails();
            details.forEach(item -> item.setHtId(add.getId()));
            detailMapper.insertBatch(MapstructUtils.convert(details, HlvrTrainingExamDetail.class));
        }
        return flag;
    }

    /**
     * 修改培训配置
     *
     * @param bo 培训配置
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(HlvrTrainingExamBo bo) {
        List<HlvrTrainingExamDetailBo>details = bo.getDetails();
        QueryWrapper<HlvrTrainingExamDetail> delete = new QueryWrapper<>();
        delete.eq("ht_id", bo.getId());
        detailMapper.delete(delete);
        details.forEach(item -> {
            item.setId(null);
            item.setHtId(bo.getId());
        });
        detailMapper.insertBatch(MapstructUtils.convert(details, HlvrTrainingExamDetail.class));
        HlvrTrainingExam update = MapstructUtils.convert(bo, HlvrTrainingExam.class);
        return baseMapper.updateById(update)> 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(HlvrTrainingExam entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除培训配置信息
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
        QueryWrapper<HlvrTrainingExamDetail> delete = new QueryWrapper<>();
        delete.in("ht_id", ids);
        detailMapper.delete(delete);
        return baseMapper.deleteByIds(ids) > 0;
    }
}
