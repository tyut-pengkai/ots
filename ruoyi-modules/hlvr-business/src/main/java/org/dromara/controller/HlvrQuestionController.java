package org.dromara.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.*;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.dromara.domain.bo.HlvrSopCardStepOptHistoryBo;
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
import org.dromara.domain.vo.HlvrQuestionVo;
import org.dromara.domain.bo.HlvrQuestionBo;
import org.dromara.service.IHlvrQuestionService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 问题
 *
 * @author admin
 * @date 2025-11-18
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/question")
public class HlvrQuestionController extends BaseController {

    private final IHlvrQuestionService hlvrQuestionService;

    /**
     * 查询问题列表
     */
    @SaCheckPermission("business:question:list")
    @GetMapping("/list")
    public TableDataInfo<HlvrQuestionVo> list(HlvrQuestionBo bo, PageQuery pageQuery) {
        return hlvrQuestionService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出问题列表
     */
    @SaCheckPermission("business:question:export")
    @Log(title = "问题", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HlvrQuestionBo bo, HttpServletResponse response) {
        List<HlvrQuestionVo> list = hlvrQuestionService.queryList(bo);
        ExcelUtil.exportExcel(list, "问题", HlvrQuestionVo.class, response);
    }

    /**
     * 获取问题详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("business:question:query")
    @GetMapping("/{id}")
    public R<HlvrQuestionVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(hlvrQuestionService.queryById(id));
    }

    /**
     * 新增问题
     */
    @SaCheckPermission("business:question:add")
    @Log(title = "问题", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody HlvrQuestionBo bo) {
        return toAjax(hlvrQuestionService.insertByBo(bo));
    }

    /**
     * 修改问题
     */
    @SaCheckPermission("business:question:edit")
    @Log(title = "问题", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody HlvrQuestionBo bo) {
        return toAjax(hlvrQuestionService.updateByBo(bo));
    }

    /**
     * 删除问题
     *
     * @param ids 主键串
     */
    @SaCheckPermission("business:question:remove")
    @Log(title = "问题", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(hlvrQuestionService.deleteWithValidByIds(List.of(ids), true));
    }

    /**
     * 提交问题
     */
    @SaCheckPermission("business:question:add")
    @Log(title = "问题", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping("/submit")
    public R<Void> submit(@Validated(AddGroup.class) @RequestBody HlvrSopCardStepOptHistoryBo bo) {
        return toAjax(hlvrQuestionService.submitAnswer(bo));
    }
}
