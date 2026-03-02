package org.dromara.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.*;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.dromara.domain.bo.HlvrTrainingExamBo;
import org.dromara.domain.vo.HlvrTrainingExamDetailVo;
import org.dromara.domain.vo.HlvrTrainingExamVo;
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
import org.dromara.service.IHlvrTrainingExamService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 培训配置
 *
 * @author admin
 * @date 2025-11-20
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/business")
public class HlvrTrainingExamController extends BaseController {

    private final IHlvrTrainingExamService hlvrTrainingService;

    /**
     * 查询培训配置列表
     */
    @SaCheckPermission("business:training:list")
    @GetMapping("/training/list")
    public TableDataInfo<HlvrTrainingExamVo> list(HlvrTrainingExamBo bo, PageQuery pageQuery) {
        return hlvrTrainingService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出培训配置列表
     */
    @SaCheckPermission("business:training:export")
    @Log(title = "培训配置", businessType = BusinessType.EXPORT)
    @PostMapping("/training/export")
    public void export(HlvrTrainingExamBo bo, HttpServletResponse response) {
        List<HlvrTrainingExamVo> list = hlvrTrainingService.queryList(bo);
        ExcelUtil.exportExcel(list, "培训配置", HlvrTrainingExamVo.class, response);
    }

    /**
     * 获取培训配置详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("business:training:query")
    @GetMapping("/training/{id}")
    public R<HlvrTrainingExamVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(hlvrTrainingService.queryById(id));
    }

    /**
     * 新增培训配置
     */
    @SaCheckPermission("business:training:add")
    @Log(title = "培训配置", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping("/training")
    public R<Void> add(@Validated(AddGroup.class) @RequestBody HlvrTrainingExamBo bo) {
        return toAjax(hlvrTrainingService.insertByBo(bo));
    }

    /**
     * 修改培训配置
     */
    @SaCheckPermission("business:training:edit")
    @Log(title = "培训配置", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping("/training")
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody HlvrTrainingExamBo bo) {
        return toAjax(hlvrTrainingService.updateByBo(bo));
    }

    /**
     * 删除培训配置
     *
     * @param ids 主键串
     */
    @SaCheckPermission("business:training:remove")
    @Log(title = "培训配置", businessType = BusinessType.DELETE)
    @DeleteMapping("/training/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(hlvrTrainingService.deleteWithValidByIds(List.of(ids), true));
    }

    @GetMapping("/training/page")
    public TableDataInfo<HlvrTrainingExamDetailVo> page(PageQuery pageQuery) {
        return hlvrTrainingService.queryPageList(pageQuery);
    }

    @GetMapping("/exam/page")
    public TableDataInfo<HlvrTrainingExamDetailVo> examPage(PageQuery pageQuery) {
        return hlvrTrainingService.queryExamList(pageQuery);
    }
}
