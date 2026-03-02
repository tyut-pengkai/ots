package org.dromara.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.*;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.dromara.domain.vo.HlvrSopCardStepGroupVo;
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
import org.dromara.domain.vo.HlvrSopCardVo;
import org.dromara.domain.bo.HlvrSopCardBo;
import org.dromara.service.IHlvrSopCardService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 操作卡
 *
 * @author Lion Li
 * @date 2025-11-13
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/sopCard")
public class HlvrSopCardController extends BaseController {

    private final IHlvrSopCardService hlvrSopCardService;

    /**
     * 查询操作卡列表
     */
    @SaCheckPermission("business:sopCard:list")
    @GetMapping("/list")
    public TableDataInfo<HlvrSopCardVo> list(HlvrSopCardBo bo, PageQuery pageQuery) {
        return hlvrSopCardService.queryPageList(bo, pageQuery);
    }

    @SaCheckPermission("business:sopCard:list")
    @GetMapping("/getList")
    public R<List<HlvrSopCardVo>> listV2(HlvrSopCardBo bo) {
        return R.ok(hlvrSopCardService.queryList(bo));
    }

    /**
     * 导出操作卡列表
     */
    @SaCheckPermission("business:sopCard:export")
    @Log(title = "操作卡", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HlvrSopCardBo bo, HttpServletResponse response) {
        List<HlvrSopCardVo> list = hlvrSopCardService.queryList(bo);
        ExcelUtil.exportExcel(list, "操作卡", HlvrSopCardVo.class, response);
    }

    /**
     * 获取操作卡详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("business:sopCard:query")
    @GetMapping("/{id}")
    public R<HlvrSopCardVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(hlvrSopCardService.queryById(id));
    }

    /**
     * 新增操作卡
     */
    @SaCheckPermission("business:sopCard:add")
    @Log(title = "操作卡", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody HlvrSopCardBo bo) {
        return toAjax(hlvrSopCardService.insertByBo(bo));
    }

    /**
     * 修改操作卡
     */
    @SaCheckPermission("business:sopCard:edit")
    @Log(title = "操作卡", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody HlvrSopCardBo bo) {
        return toAjax(hlvrSopCardService.updateByBo(bo));
    }

    /**
     * 删除操作卡
     *
     * @param ids 主键串
     */
    @SaCheckPermission("business:sopCard:remove")
    @Log(title = "操作卡", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(hlvrSopCardService.deleteWithValidByIds(List.of(ids), true));
    }

    /**
     * 获取操作卡详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("business:sopCard:query")
    @GetMapping("/getFirstOpt/{id}")
    public R<HlvrSopCardStepGroupVo> getFirstOpt(@NotNull(message = "主键不能为空")
                                    @PathVariable Long id) {
        return R.ok(hlvrSopCardService.getFirstOpt(id));
    }

    /**
     * 复制操作卡
     * @param bo 复制参数
     */
    @SaCheckPermission("business:sopCard:add")
    @PostMapping("/copy")
    @RepeatSubmit()
    public R<Void> copy(@Validated(AddGroup.class) @RequestBody HlvrSopCardBo bo) {
        return toAjax(hlvrSopCardService.copyCard(bo));
    }
}
