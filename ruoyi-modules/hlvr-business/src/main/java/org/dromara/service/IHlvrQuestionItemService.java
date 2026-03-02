package org.dromara.service;

import org.dromara.domain.vo.HlvrQuestionItemVo;
import org.dromara.domain.bo.HlvrQuestionItemBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 问题选项Service接口
 *
 * @author admin
 * @date 2025-11-18
 */
public interface IHlvrQuestionItemService {

    /**
     * 查询问题选项
     *
     * @param id 主键
     * @return 问题选项
     */
    HlvrQuestionItemVo queryById(Long id);

    /**
     * 分页查询问题选项列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 问题选项分页列表
     */
    TableDataInfo<HlvrQuestionItemVo> queryPageList(HlvrQuestionItemBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的问题选项列表
     *
     * @param bo 查询条件
     * @return 问题选项列表
     */
    List<HlvrQuestionItemVo> queryList(HlvrQuestionItemBo bo);

    /**
     * 新增问题选项
     *
     * @param bo 问题选项
     * @return 是否新增成功
     */
    Boolean insertByBo(HlvrQuestionItemBo bo);

    /**
     * 修改问题选项
     *
     * @param bo 问题选项
     * @return 是否修改成功
     */
    Boolean updateByBo(HlvrQuestionItemBo bo);

    /**
     * 校验并批量删除问题选项信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
