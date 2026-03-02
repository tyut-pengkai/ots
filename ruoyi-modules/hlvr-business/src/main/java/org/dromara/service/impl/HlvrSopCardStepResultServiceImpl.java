package org.dromara.service.impl;

import cn.hutool.core.lang.Dict;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.json.utils.JsonUtils;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.dromara.domain.bo.HlvrSopCardStepResultBo;
import org.dromara.domain.vo.HlvrSopCardStepResultVo;
import org.dromara.domain.HlvrSopCardStepResult;
import org.dromara.mapper.HlvrSopCardStepResultMapper;
import org.dromara.service.IHlvrSopCardStepResultService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 操作卡步骤影响结果Service业务层处理
 *
 * @author Lion Li
 * @date 2025-11-07
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class HlvrSopCardStepResultServiceImpl implements IHlvrSopCardStepResultService {

    private final HlvrSopCardStepResultMapper baseMapper;

    /**
     * 查询操作卡步骤影响结果
     *
     * @param id 主键
     * @return 操作卡步骤影响结果
     */
    @Override
    public HlvrSopCardStepResultVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询操作卡步骤影响结果列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 操作卡步骤影响结果分页列表
     */
    @Override
    public TableDataInfo<HlvrSopCardStepResultVo> queryPageList(HlvrSopCardStepResultBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<HlvrSopCardStepResult> lqw = buildQueryWrapper(bo);
        Page<HlvrSopCardStepResultVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的操作卡步骤影响结果列表
     *
     * @param bo 查询条件
     * @return 操作卡步骤影响结果列表
     */
    @Override
    public List<HlvrSopCardStepResultVo> queryList(HlvrSopCardStepResultBo bo) {
        LambdaQueryWrapper<HlvrSopCardStepResult> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<HlvrSopCardStepResult> buildQueryWrapper(HlvrSopCardStepResultBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<HlvrSopCardStepResult> lqw = Wrappers.lambdaQuery();
        return lqw;
    }

    /**
     * 新增操作卡步骤影响结果
     *
     * @param bo 操作卡步骤影响结果
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(HlvrSopCardStepResultBo bo) {
        HlvrSopCardStepResult add = MapstructUtils.convert(bo, HlvrSopCardStepResult.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改操作卡步骤影响结果
     *
     * @param bo 操作卡步骤影响结果
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(HlvrSopCardStepResultBo bo) {
        HlvrSopCardStepResult update = MapstructUtils.convert(bo, HlvrSopCardStepResult.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(HlvrSopCardStepResult entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除操作卡步骤影响结果信息
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


   public HlvrSopCardStepResultVo selectPopwindowInfo(Long hscsoId) {
       LambdaQueryWrapper<HlvrSopCardStepResult> lqw = new LambdaQueryWrapper<>();
       lqw.eq(HlvrSopCardStepResult::getResultType, "popwindow");
       lqw.eq(HlvrSopCardStepResult::getHscsoId, hscsoId);
       return baseMapper.selectVoOne(lqw);


   }
}
