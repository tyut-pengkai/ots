package org.dromara.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.*;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import org.dromara.common.idempotent.annotation.RepeatSubmit;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.web.core.BaseController;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.core.domain.R;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.excel.utils.ExcelUtil;
import org.dromara.domain.vo.HlvrSopCardStepGroupVo;
import org.dromara.domain.bo.HlvrSopCardStepGroupBo;
import org.dromara.service.IHlvrSopCardStepGroupService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 操作卡步骤分组
 *
 * @author Lion Li
 * @date 2025-11-13
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/sopCardStepGroup")
public class HlvrSopCardStepGroupController extends BaseController {

    private final IHlvrSopCardStepGroupService hlvrSopCardStepGroupService;

    /**
     * 查询操作卡步骤分组列表
     */
    @SaCheckPermission("business:sopCardStepGroup:list")
    @GetMapping("/list")
    public TableDataInfo<HlvrSopCardStepGroupVo> list(HlvrSopCardStepGroupBo bo, PageQuery pageQuery) {
        return hlvrSopCardStepGroupService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出操作卡步骤分组列表
     */
    @SaCheckPermission("business:sopCardStepGroup:export")
    @Log(title = "操作卡步骤分组", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HlvrSopCardStepGroupBo bo, HttpServletResponse response) {
        List<HlvrSopCardStepGroupVo> list = hlvrSopCardStepGroupService.queryList(bo);
        ExcelUtil.exportExcel(list, "操作卡步骤分组", HlvrSopCardStepGroupVo.class, response);
    }

    /**
     * 获取操作卡步骤分组详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("business:sopCardStepGroup:query")
    @GetMapping("/{id}")
    public R<HlvrSopCardStepGroupVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(hlvrSopCardStepGroupService.queryById(id));
    }

    /**
     * 新增操作卡步骤分组
     */
    @SaCheckPermission("business:sopCardStepGroup:add")
    @Log(title = "操作卡步骤分组", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody HlvrSopCardStepGroupBo bo) {
        return toAjax(hlvrSopCardStepGroupService.insertByBo(bo));
    }

    /**
     * 修改操作卡步骤分组
     */
    @SaCheckPermission("business:sopCardStepGroup:edit")
    @Log(title = "操作卡步骤分组", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody HlvrSopCardStepGroupBo bo) {
        return toAjax(hlvrSopCardStepGroupService.updateByBo(bo));
    }

    /**
     * 删除操作卡步骤分组
     *
     * @param ids 主键串
     */
    @SaCheckPermission("business:sopCardStepGroup:remove")
    @Log(title = "操作卡步骤分组", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(hlvrSopCardStepGroupService.deleteWithValidByIds(List.of(ids), true));
    }
}
