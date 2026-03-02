package org.dromara.service;

import org.dromara.domain.bo.HlvrTrainingExamBo;
import org.dromara.domain.vo.HlvrTrainingExamDetailVo;
import org.dromara.domain.vo.HlvrTrainingExamVo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 培训配置Service接口
 *
 * @author admin
 * @date 2025-11-20
 */
public interface IHlvrTrainingExamService {

    /**
     * 查询培训配置
     *
     * @param id 主键
     * @return 培训配置
     */
    HlvrTrainingExamVo queryById(Long id);

    /**
     * 分页查询培训配置列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 培训配置分页列表
     */
    TableDataInfo<HlvrTrainingExamVo> queryPageList(HlvrTrainingExamBo bo, PageQuery pageQuery);

    TableDataInfo<HlvrTrainingExamDetailVo> queryPageList(PageQuery pageQuery);

    TableDataInfo<HlvrTrainingExamDetailVo> queryExamList(PageQuery pageQuery);

    /**
     * 查询符合条件的培训配置列表
     *
     * @param bo 查询条件
     * @return 培训配置列表
     */
    List<HlvrTrainingExamVo> queryList(HlvrTrainingExamBo bo);

    /**
     * 新增培训配置
     *
     * @param bo 培训配置
     * @return 是否新增成功
     */
    Boolean insertByBo(HlvrTrainingExamBo bo);

    /**
     * 修改培训配置
     *
     * @param bo 培训配置
     * @return 是否修改成功
     */
    Boolean updateByBo(HlvrTrainingExamBo bo);

    /**
     * 校验并批量删除培训配置信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
