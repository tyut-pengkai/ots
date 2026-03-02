package org.dromara.service;

import org.dromara.domain.vo.HlvrSopCardStepGroupVo;
import org.dromara.domain.vo.HlvrSopCardVo;
import org.dromara.domain.bo.HlvrSopCardBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 操作卡Service接口
 *
 * @author Lion Li
 * @date 2025-11-13
 */
public interface IHlvrSopCardService {

    /**
     * 查询操作卡
     *
     * @param id 主键
     * @return 操作卡
     */
    HlvrSopCardVo queryById(Long id);

    /**
     * 分页查询操作卡列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 操作卡分页列表
     */
    TableDataInfo<HlvrSopCardVo> queryPageList(HlvrSopCardBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的操作卡列表
     *
     * @param bo 查询条件
     * @return 操作卡列表
     */
    List<HlvrSopCardVo> queryList(HlvrSopCardBo bo);

    /**
     * 新增操作卡
     *
     * @param bo 操作卡
     * @return 是否新增成功
     */
    Boolean insertByBo(HlvrSopCardBo bo);

    /**
     * 修改操作卡
     *
     * @param bo 操作卡
     * @return 是否修改成功
     */
    Boolean updateByBo(HlvrSopCardBo bo);

    /**
     * 校验并批量删除操作卡信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 查询操作卡
     *
     * @param id 主键
     * @return 操作卡
     */
    HlvrSopCardStepGroupVo getFirstOpt(Long id);

    Boolean copyCard(HlvrSopCardBo bo);
}
