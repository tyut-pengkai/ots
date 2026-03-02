package org.dromara.service;

import org.dromara.domain.vo.HlvrFileVo;
import org.dromara.domain.bo.HlvrFileBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 文件资源管理Service接口
 *
 * @author admin
 * @date 2025-11-13
 */
public interface IHlvrFileService {

    /**
     * 查询文件资源管理
     *
     * @param id 主键
     * @return 文件资源管理
     */
    HlvrFileVo queryById(Long id);

    /**
     * 分页查询文件资源管理列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 文件资源管理分页列表
     */
    TableDataInfo<HlvrFileVo> queryPageList(HlvrFileBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的文件资源管理列表
     *
     * @param bo 查询条件
     * @return 文件资源管理列表
     */
    List<HlvrFileVo> queryList(HlvrFileBo bo);

    /**
     * 新增文件资源管理
     *
     * @param bo 文件资源管理
     * @return 是否新增成功
     */
    Boolean insertByBo(HlvrFileBo bo);

    /**
     * 修改文件资源管理
     *
     * @param bo 文件资源管理
     * @return 是否修改成功
     */
    Boolean updateByBo(HlvrFileBo bo);

    /**
     * 校验并批量删除文件资源管理信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
