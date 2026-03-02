package org.dromara.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.*;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.dromara.domain.vo.HlvrSopCardStepGroupVo;
import org.dromara.domain.vo.HlvrSopCardStepOptVo;
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
import org.dromara.domain.vo.HlvrSopCardStepOptHistoryVo;
import org.dromara.domain.bo.HlvrSopCardStepOptHistoryBo;
import org.dromara.service.IHlvrSopCardStepOptHistoryService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 操作卡步骤记录
 *
 * @author admin
 * @date 2025-11-17
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/sopCardStepOptHistory")
public class HlvrSopCardStepOptHistoryController extends BaseController {

    private final IHlvrSopCardStepOptHistoryService hlvrSopCardStepOptHistoryService;

    /**
     * 查询操作卡步骤记录列表
     */
    @SaCheckPermission("business:sopCardStepOptHistory:list")
    @GetMapping("/list")
    public TableDataInfo<HlvrSopCardStepOptHistoryVo> list(HlvrSopCardStepOptHistoryBo bo, PageQuery pageQuery) {
        return hlvrSopCardStepOptHistoryService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出操作卡步骤记录列表
     */
    @SaCheckPermission("business:sopCardStepOptHistory:export")
    @Log(title = "操作卡步骤记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HlvrSopCardStepOptHistoryBo bo, HttpServletResponse response) {
        List<HlvrSopCardStepOptHistoryVo> list = hlvrSopCardStepOptHistoryService.queryList(bo);
        ExcelUtil.exportExcel(list, "操作卡步骤记录", HlvrSopCardStepOptHistoryVo.class, response);
    }

    /**
     * 获取操作卡步骤记录详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("business:sopCardStepOptHistory:query")
    @GetMapping("/{id}")
    public R<HlvrSopCardStepOptHistoryVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(hlvrSopCardStepOptHistoryService.queryById(id));
    }

    /**
     * 新增操作卡步骤记录
     */
    @SaCheckPermission("business:sopCardStepOptHistory:add")
    @Log(title = "操作卡步骤记录", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<HlvrSopCardStepGroupVo> add(@Validated(AddGroup.class) @RequestBody HlvrSopCardStepOptHistoryBo bo) {
        return R.ok(hlvrSopCardStepOptHistoryService.insertByBo(bo));
    }

    /**
     * 修改操作卡步骤记录
     */
    @SaCheckPermission("business:sopCardStepOptHistory:edit")
    @Log(title = "操作卡步骤记录", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody HlvrSopCardStepOptHistoryBo bo) {
        return toAjax(hlvrSopCardStepOptHistoryService.updateByBo(bo));
    }

    /**
     * 删除操作卡步骤记录
     *
     * @param ids 主键串
     */
    @SaCheckPermission("business:sopCardStepOptHistory:remove")
    @Log(title = "操作卡步骤记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(hlvrSopCardStepOptHistoryService.deleteWithValidByIds(List.of(ids), true));
    }
}
