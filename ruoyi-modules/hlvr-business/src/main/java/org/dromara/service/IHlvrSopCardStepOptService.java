package org.dromara.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.dromara.domain.vo.HlvrSopCardStepOptVo;
import org.dromara.domain.bo.HlvrSopCardStepOptBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 操作卡步骤Service接口
 *
 * @author Lion Li
 * @date 2025-11-07
 */
public interface IHlvrSopCardStepOptService {

    /**
     * 查询操作卡步骤
     *
     * @param id 主键
     * @return 操作卡步骤
     */
    HlvrSopCardStepOptVo queryById(Long id);

    /**
     * 分页查询操作卡步骤列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 操作卡步骤分页列表
     */
    TableDataInfo<HlvrSopCardStepOptVo> queryPageList(HlvrSopCardStepOptBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的操作卡步骤列表
     *
     * @param bo 查询条件
     * @return 操作卡步骤列表
     */
    List<HlvrSopCardStepOptVo> queryList(HlvrSopCardStepOptBo bo);

    /**
     * 新增操作卡步骤
     *
     * @param bo 操作卡步骤
     * @return 是否新增成功
     */
    Boolean insertByBo(HlvrSopCardStepOptBo bo);

    /**
     * 修改操作卡步骤
     *
     * @param bo 操作卡步骤
     * @return 是否修改成功
     */
    Boolean updateByBo(HlvrSopCardStepOptBo bo);

    /**
     * 校验并批量删除操作卡步骤信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    TableDataInfo<HlvrSopCardStepOptVo> getStepsByGroupId(Long id, PageQuery pageQuery);

    List<HlvrSopCardStepOptVo> getStepsByCardId(Long id);
}
