package org.dromara.service.impl;

import cn.hutool.core.collection.CollUtil;
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
import org.springframework.stereotype.Service;
import org.dromara.domain.bo.HlvrFileBo;
import org.dromara.domain.vo.HlvrFileVo;
import org.dromara.domain.HlvrFile;
import org.dromara.mapper.HlvrFileMapper;
import org.dromara.service.IHlvrFileService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 文件资源管理Service业务层处理
 *
 * @author admin
 * @date 2025-11-13
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class HlvrFileServiceImpl implements IHlvrFileService {

    private final HlvrFileMapper baseMapper;

    /**
     * 查询文件资源管理
     *
     * @param id 主键
     * @return 文件资源管理
     */
    @Override
    public HlvrFileVo queryById(Long id){
        return baseMapper.queryById(id);
    }

    /**
     * 分页查询文件资源管理列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 文件资源管理分页列表
     */
    @Override
    public TableDataInfo<HlvrFileVo> queryPageList(HlvrFileBo bo, PageQuery pageQuery) {
        IPage<HlvrFileVo> page = pageQuery.build();
        List<HlvrFileVo> result = baseMapper.selectList(bo, page.getCurrent(), page.getSize());
        page.setRecords(result);
        long total = baseMapper.queryTotal(bo);
        page.setTotal(total);
        return TableDataInfo.build(page);
    }

    /**
     * 查询符合条件的文件资源管理列表
     *
     * @param bo 查询条件
     * @return 文件资源管理列表
     */
    @Override
    public List<HlvrFileVo> queryList(HlvrFileBo bo) {
        LambdaQueryWrapper<HlvrFile> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<HlvrFile> buildQueryWrapper(HlvrFileBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<HlvrFile> lqw = Wrappers.lambdaQuery();
        lqw.orderByAsc(HlvrFile::getId);
        lqw.eq(StringUtils.isNotBlank(bo.getAssetType()), HlvrFile::getAssetType, bo.getAssetType());
        return lqw;
    }

    /**
     * 新增文件资源管理
     *
     * @param bo 文件资源管理
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(HlvrFileBo bo) {
        HlvrFile add = MapstructUtils.convert(bo, HlvrFile.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改文件资源管理
     *
     * @param bo 文件资源管理
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(HlvrFileBo bo) {
        HlvrFile update = MapstructUtils.convert(bo, HlvrFile.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(HlvrFile entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除文件资源管理信息
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
