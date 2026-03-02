package org.dromara.service;

import org.dromara.domain.vo.HlvrSopCardStepGroupVo;
import org.dromara.domain.bo.HlvrSopCardStepGroupBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 操作卡步骤分组Service接口
 *
 * @author Lion Li
 * @date 2025-11-13
 */
public interface IHlvrSopCardStepGroupService {

    /**
     * 查询操作卡步骤分组
     *
     * @param id 主键
     * @return 操作卡步骤分组
     */
    HlvrSopCardStepGroupVo queryById(Long id);

    /**
     * 分页查询操作卡步骤分组列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 操作卡步骤分组分页列表
     */
    TableDataInfo<HlvrSopCardStepGroupVo> queryPageList(HlvrSopCardStepGroupBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的操作卡步骤分组列表
     *
     * @param bo 查询条件
     * @return 操作卡步骤分组列表
     */
    List<HlvrSopCardStepGroupVo> queryList(HlvrSopCardStepGroupBo bo);

    /**
     * 新增操作卡步骤分组
     *
     * @param bo 操作卡步骤分组
     * @return 是否新增成功
     */
    Boolean insertByBo(HlvrSopCardStepGroupBo bo);

    /**
     * 修改操作卡步骤分组
     *
     * @param bo 操作卡步骤分组
     * @return 是否修改成功
     */
    Boolean updateByBo(HlvrSopCardStepGroupBo bo);

    /**
     * 校验并批量删除操作卡步骤分组信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
