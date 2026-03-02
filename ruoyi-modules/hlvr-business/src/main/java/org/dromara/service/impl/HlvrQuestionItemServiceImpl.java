package org.dromara.service.impl;

import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.dromara.domain.bo.HlvrQuestionItemBo;
import org.dromara.domain.vo.HlvrQuestionItemVo;
import org.dromara.domain.HlvrQuestionItem;
import org.dromara.mapper.HlvrQuestionItemMapper;
import org.dromara.service.IHlvrQuestionItemService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 问题选项Service业务层处理
 *
 * @author admin
 * @date 2025-11-18
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class HlvrQuestionItemServiceImpl implements IHlvrQuestionItemService {

    private final HlvrQuestionItemMapper baseMapper;

    /**
     * 查询问题选项
     *
     * @param id 主键
     * @return 问题选项
     */
    @Override
    public HlvrQuestionItemVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询问题选项列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 问题选项分页列表
     */
    @Override
    public TableDataInfo<HlvrQuestionItemVo> queryPageList(HlvrQuestionItemBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<HlvrQuestionItem> lqw = buildQueryWrapper(bo);
        Page<HlvrQuestionItemVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的问题选项列表
     *
     * @param bo 查询条件
     * @return 问题选项列表
     */
    @Override
    public List<HlvrQuestionItemVo> queryList(HlvrQuestionItemBo bo) {
        LambdaQueryWrapper<HlvrQuestionItem> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<HlvrQuestionItem> buildQueryWrapper(HlvrQuestionItemBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<HlvrQuestionItem> lqw = Wrappers.lambdaQuery();
        lqw.orderByAsc(HlvrQuestionItem::getId);
        lqw.eq(bo.getHqId() != null, HlvrQuestionItem::getHqId, bo.getHqId());
        lqw.eq(StringUtils.isNotBlank(bo.getItemTag()), HlvrQuestionItem::getItemTag, bo.getItemTag());
        lqw.eq(StringUtils.isNotBlank(bo.getItemDescription()), HlvrQuestionItem::getItemDescription, bo.getItemDescription());
        lqw.eq(StringUtils.isNotBlank(bo.getIsAnswer()), HlvrQuestionItem::getIsAnswer, bo.getIsAnswer());
        return lqw;
    }

    /**
     * 新增问题选项
     *
     * @param bo 问题选项
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(HlvrQuestionItemBo bo) {
        HlvrQuestionItem add = MapstructUtils.convert(bo, HlvrQuestionItem.class);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改问题选项
     *
     * @param bo 问题选项
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(HlvrQuestionItemBo bo) {
        HlvrQuestionItem update = MapstructUtils.convert(bo, HlvrQuestionItem.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(HlvrQuestionItem entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除问题选项信息
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
