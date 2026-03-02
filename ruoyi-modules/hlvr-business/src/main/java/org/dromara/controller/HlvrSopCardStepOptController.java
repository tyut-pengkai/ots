package org.dromara.controller;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
import org.dromara.domain.vo.HlvrSopCardStepOptVo;
import org.dromara.domain.bo.HlvrSopCardStepOptBo;
import org.dromara.service.IHlvrSopCardStepOptService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 操作卡步骤
 *
 * @author Lion Li
 * @date 2025-11-07
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/sopCardStepOpt")
public class HlvrSopCardStepOptController extends BaseController {

    private final IHlvrSopCardStepOptService hlvrSopCardStepOptService;

    /**
     * 查询操作卡步骤列表
     */
    @SaCheckPermission("business:sopCardStepOpt:list")
    @GetMapping("/list")
    public TableDataInfo<HlvrSopCardStepOptVo> list(HlvrSopCardStepOptBo bo, PageQuery pageQuery) {
        return hlvrSopCardStepOptService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出操作卡步骤列表
     */
    @SaCheckPermission("business:sopCardStepOpt:export")
    @Log(title = "操作卡步骤", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HlvrSopCardStepOptBo bo, HttpServletResponse response) {
        List<HlvrSopCardStepOptVo> list = hlvrSopCardStepOptService.queryList(bo);
        ExcelUtil.exportExcel(list, "操作卡步骤", HlvrSopCardStepOptVo.class, response);
    }

    /**
     * 获取操作卡步骤详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("business:sopCardStepOpt:query")
    @GetMapping("/{id}")
    public R<HlvrSopCardStepOptVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(hlvrSopCardStepOptService.queryById(id));
    }

    /**
     * 新增操作卡步骤
     */
    @SaCheckPermission("business:sopCardStepOpt:add")
    @Log(title = "操作卡步骤", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody HlvrSopCardStepOptBo bo) {
        return toAjax(hlvrSopCardStepOptService.insertByBo(bo));
    }

    /**
     * 修改操作卡步骤
     */
    @SaCheckPermission("business:sopCardStepOpt:edit")
    @Log(title = "操作卡步骤", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody HlvrSopCardStepOptBo bo) {
        return toAjax(hlvrSopCardStepOptService.updateByBo(bo));
    }

    /**
     * 删除操作卡步骤
     *
     * @param ids 主键串
     */
    @SaCheckPermission("business:sopCardStepOpt:remove")
    @Log(title = "操作卡步骤", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(hlvrSopCardStepOptService.deleteWithValidByIds(List.of(ids), true));
    }

    /**
     * 获取操作卡步骤详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("business:sopCardStepOpt:query")
    @GetMapping("/getStepsByGroupId/{id}")
    public TableDataInfo<HlvrSopCardStepOptVo> getStepsByGroupId(@NotNull(message = "主键不能为空")
                                           @PathVariable Long id, PageQuery pageQuery) {
        return hlvrSopCardStepOptService.getStepsByGroupId(id, pageQuery);
    }

    /**
     * 获取操作卡步骤详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("business:sopCardStepOpt:query")
    @GetMapping("/getStepsByCardId/{id}")
    public R<List<HlvrSopCardStepOptVo>> getStepsByCardId(@NotNull(message = "主键不能为空")
                                                           @PathVariable Long id) {
        return R.ok(hlvrSopCardStepOptService.getStepsByCardId(id));
    }
}
