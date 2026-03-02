package org.dromara.service;

import org.dromara.domain.bo.HlvrSopCardStepOptHistoryBo;
import org.dromara.domain.vo.HlvrQuestionVo;
import org.dromara.domain.bo.HlvrQuestionBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 问题Service接口
 *
 * @author admin
 * @date 2025-11-18
 */
public interface IHlvrQuestionService {

    /**
     * 查询问题
     *
     * @param id 主键
     * @return 问题
     */
    HlvrQuestionVo queryById(Long id);

    /**
     * 分页查询问题列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 问题分页列表
     */
    TableDataInfo<HlvrQuestionVo> queryPageList(HlvrQuestionBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的问题列表
     *
     * @param bo 查询条件
     * @return 问题列表
     */
    List<HlvrQuestionVo> queryList(HlvrQuestionBo bo);

    /**
     * 新增问题
     *
     * @param bo 问题
     * @return 是否新增成功
     */
    Boolean insertByBo(HlvrQuestionBo bo);

    /**
     * 修改问题
     *
     * @param bo 问题
     * @return 是否修改成功
     */
    Boolean updateByBo(HlvrQuestionBo bo);

    /**
     * 校验并批量删除问题信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    Boolean submitAnswer(HlvrSopCardStepOptHistoryBo bo);
}
