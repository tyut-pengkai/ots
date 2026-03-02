package org.dromara.controller;

import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.*;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.dromara.annotation.UploadAttachment;
import org.dromara.common.excel.core.ExcelResult;
import org.springframework.http.MediaType;
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
import org.dromara.domain.vo.HlvrElementVo;
import org.dromara.domain.bo.HlvrElementBo;
import org.dromara.service.IHlvrElementService;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 资产管理
 *
 * @author admin
 * @date 2025-11-12
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/element")
public class HlvrElementController extends BaseController {

    private final IHlvrElementService hlvrElementService;

    /**
     * 查询资产管理列表
     */
    @SaCheckPermission("business:element:list")
    @GetMapping("/list")
    public TableDataInfo<HlvrElementVo> list(HlvrElementBo bo, PageQuery pageQuery) {
        return hlvrElementService.queryPageList(bo, pageQuery);
    }
    /**
     * 查询资产管理列表
     */
    @SaCheckPermission("business:element:list")
    @GetMapping("/getList")
    public R<List<HlvrElementVo>> list(HlvrElementBo bo) {
        return R.ok(hlvrElementService.queryList(bo));
    }

    /**
     * 获取设备简介列表
     */
    @SaCheckPermission("business:element:list")
    @GetMapping("/queryEquiOverviewList")
    public R<List<HlvrElementVo>> queryEquiOverviewList(HlvrElementBo bo) {
        return R.ok(hlvrElementService.queryEquiOverviewList(bo));
    }




    /**
     * 导出资产管理列表
     */
    @SaCheckPermission("business:element:export")
    @Log(title = "资产管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HlvrElementBo bo, HttpServletResponse response) {
        List<HlvrElementVo> list = hlvrElementService.queryList(bo);
        ExcelUtil.exportExcel(list, "资产管理", HlvrElementVo.class, response);
    }

    /**
     * 获取资产管理详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("business:element:query")
    @GetMapping("/{id}")
    public R<HlvrElementVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(hlvrElementService.queryById(id));
    }

    /**
     * 新增资产管理
     */
    @SaCheckPermission("business:element:add")
    @Log(title = "资产管理", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    @UploadAttachment
    public R<HlvrElementBo> add(@RequestPart(value = "file", required = false) List<MultipartFile> file, HlvrElementBo bo) {
        hlvrElementService.insertByBo(bo);
        return R.ok(bo);
    }

    /**
     * 修改资产管理
     */
    @SaCheckPermission("business:element:edit")
    @Log(title = "资产管理", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    @UploadAttachment
    public R<HlvrElementBo> edit(@RequestPart(value = "file", required = false) List<MultipartFile> file, HlvrElementBo bo) {
        hlvrElementService.updateByBo(bo);
        return R.ok(bo);
    }

    /**
     * 删除资产管理
     *
     * @param ids 主键串
     */
    @SaCheckPermission("business:element:remove")
    @Log(title = "资产管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(hlvrElementService.deleteWithValidByIds(List.of(ids), true));
    }

    @Log(title = "资产管理", businessType = BusinessType.IMPORT)
    @SaCheckPermission("business:element:import")
    @PostMapping(value = "/importData", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public R<Void> importData(@RequestPart("file") MultipartFile file) throws Exception {
        List<HlvrElementVo> result = ExcelUtil.importExcel(file.getInputStream(), HlvrElementVo.class);
        hlvrElementService.insertBatch(result);
        return R.ok();
    }

    /**
     * 获取导入模板
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        ExcelUtil.exportExcel(new ArrayList<>(), "资源数据", HlvrElementVo.class, response);
    }
}
