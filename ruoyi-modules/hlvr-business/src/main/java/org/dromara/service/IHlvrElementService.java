package org.dromara.service;

import org.dromara.domain.vo.HlvrElementVo;
import org.dromara.domain.bo.HlvrElementBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 资产管理Service接口
 *
 * @author admin
 * @date 2025-11-12
 */
public interface IHlvrElementService {

    /**
     * 查询资产管理
     *
     * @param id 主键
     * @return 资产管理
     */
    HlvrElementVo queryById(Long id);

    /**
     * 分页查询资产管理列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 资产管理分页列表
     */
    TableDataInfo<HlvrElementVo> queryPageList(HlvrElementBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的资产管理列表
     *
     * @param bo 查询条件
     * @return 资产管理列表
     */
    List<HlvrElementVo> queryList(HlvrElementBo bo);


    /**
     * 查询设备简介列表
     *
     * @param bo 查询条件
     * @return 设备简介列表
     */
    List<HlvrElementVo> queryEquiOverviewList(HlvrElementBo bo);

    /**
     * 新增资产管理
     *
     * @param bo 资产管理
     * @return 是否新增成功
     */
    Boolean insertByBo(HlvrElementBo bo);

    /**
     * 修改资产管理
     *
     * @param bo 资产管理
     * @return 是否修改成功
     */
    Boolean updateByBo(HlvrElementBo bo);

    /**
     * 校验并批量删除资产管理信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    Boolean insertBatch(List<HlvrElementVo> bo);
}
