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
import org.dromara.domain.vo.HlvrTrainingExamDetailVo;
import org.dromara.domain.bo.HlvrTrainingExamDetailBo;
import org.dromara.service.IHlvrTrainingExamDetailService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 培训配置详情
 *
 * @author admin
 * @date 2025-11-20
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/trainingDetail")
public class HlvrTrainingExamDetailController extends BaseController {

    private final IHlvrTrainingExamDetailService hlvrTrainingDetailService;

    /**
     * 查询培训配置详情列表
     */
    @SaCheckPermission("business:trainingDetail:list")
    @GetMapping("/list")
    public TableDataInfo<HlvrTrainingExamDetailVo> list(HlvrTrainingExamDetailBo bo, PageQuery pageQuery) {
        return hlvrTrainingDetailService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出培训配置详情列表
     */
    @SaCheckPermission("business:trainingDetail:export")
    @Log(title = "培训配置详情", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HlvrTrainingExamDetailBo bo, HttpServletResponse response) {
        List<HlvrTrainingExamDetailVo> list = hlvrTrainingDetailService.queryList(bo);
        ExcelUtil.exportExcel(list, "培训配置详情", HlvrTrainingExamDetailVo.class, response);
    }

    /**
     * 获取培训配置详情详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("business:trainingDetail:query")
    @GetMapping("/{id}")
    public R<HlvrTrainingExamDetailVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(hlvrTrainingDetailService.queryById(id));
    }

    /**
     * 新增培训配置详情
     */
    @SaCheckPermission("business:trainingDetail:add")
    @Log(title = "培训配置详情", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody HlvrTrainingExamDetailBo bo) {
        return toAjax(hlvrTrainingDetailService.insertByBo(bo));
    }

    /**
     * 修改培训配置详情
     */
    @SaCheckPermission("business:trainingDetail:edit")
    @Log(title = "培训配置详情", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody HlvrTrainingExamDetailBo bo) {
        return toAjax(hlvrTrainingDetailService.updateByBo(bo));
    }

    /**
     * 删除培训配置详情
     *
     * @param ids 主键串
     */
    @SaCheckPermission("business:trainingDetail:remove")
    @Log(title = "培训配置详情", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(hlvrTrainingDetailService.deleteWithValidByIds(List.of(ids), true));
    }
}
