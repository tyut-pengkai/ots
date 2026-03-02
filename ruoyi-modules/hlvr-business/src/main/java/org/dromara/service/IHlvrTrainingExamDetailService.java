package org.dromara.service;

import org.dromara.domain.bo.HlvrTrainingExamDetailBo;
import org.dromara.domain.vo.HlvrTrainingExamDetailVo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 培训配置详情Service接口
 *
 * @author admin
 * @date 2025-11-20
 */
public interface IHlvrTrainingExamDetailService {

    /**
     * 查询培训配置详情
     *
     * @param id 主键
     * @return 培训配置详情
     */
    HlvrTrainingExamDetailVo queryById(Long id);

    /**
     * 分页查询培训配置详情列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 培训配置详情分页列表
     */
    TableDataInfo<HlvrTrainingExamDetailVo> queryPageList(HlvrTrainingExamDetailBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的培训配置详情列表
     *
     * @param bo 查询条件
     * @return 培训配置详情列表
     */
    List<HlvrTrainingExamDetailVo> queryList(HlvrTrainingExamDetailBo bo);

    /**
     * 新增培训配置详情
     *
     * @param bo 培训配置详情
     * @return 是否新增成功
     */
    Boolean insertByBo(HlvrTrainingExamDetailBo bo);

    /**
     * 修改培训配置详情
     *
     * @param bo 培训配置详情
     * @return 是否修改成功
     */
    Boolean updateByBo(HlvrTrainingExamDetailBo bo);

    /**
     * 校验并批量删除培训配置详情信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
