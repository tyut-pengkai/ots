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
import org.dromara.domain.vo.HlvrQuestionItemVo;
import org.dromara.domain.bo.HlvrQuestionItemBo;
import org.dromara.service.IHlvrQuestionItemService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 问题选项
 *
 * @author admin
 * @date 2025-11-18
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/questionItem")
public class HlvrQuestionItemController extends BaseController {

    private final IHlvrQuestionItemService hlvrQuestionItemService;

    /**
     * 查询问题选项列表
     */
    @SaCheckPermission("business:questionItem:list")
    @GetMapping("/list")
    public TableDataInfo<HlvrQuestionItemVo> list(HlvrQuestionItemBo bo, PageQuery pageQuery) {
        return hlvrQuestionItemService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出问题选项列表
     */
    @SaCheckPermission("business:questionItem:export")
    @Log(title = "问题选项", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HlvrQuestionItemBo bo, HttpServletResponse response) {
        List<HlvrQuestionItemVo> list = hlvrQuestionItemService.queryList(bo);
        ExcelUtil.exportExcel(list, "问题选项", HlvrQuestionItemVo.class, response);
    }

    /**
     * 获取问题选项详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("business:questionItem:query")
    @GetMapping("/{id}")
    public R<HlvrQuestionItemVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(hlvrQuestionItemService.queryById(id));
    }

    /**
     * 新增问题选项
     */
    @SaCheckPermission("business:questionItem:add")
    @Log(title = "问题选项", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody HlvrQuestionItemBo bo) {
        return toAjax(hlvrQuestionItemService.insertByBo(bo));
    }

    /**
     * 修改问题选项
     */
    @SaCheckPermission("business:questionItem:edit")
    @Log(title = "问题选项", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody HlvrQuestionItemBo bo) {
        return toAjax(hlvrQuestionItemService.updateByBo(bo));
    }

    /**
     * 删除问题选项
     *
     * @param ids 主键串
     */
    @SaCheckPermission("business:questionItem:remove")
    @Log(title = "问题选项", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(hlvrQuestionItemService.deleteWithValidByIds(List.of(ids), true));
    }
}
