package org.dromara.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.*;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.dromara.annotation.UploadAttachment;
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
import org.dromara.domain.vo.HlvrFileVo;
import org.dromara.domain.bo.HlvrFileBo;
import org.dromara.service.IHlvrFileService;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件资源管理
 *
 * @author admin
 * @date 2025-11-13
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/file")
public class HlvrFileController extends BaseController {

    private final IHlvrFileService hlvrFileService;

    /**
     * 查询文件资源管理列表
     */
    @SaCheckPermission("business:file:list")
    @GetMapping("/list")
    public TableDataInfo<HlvrFileVo> list(HlvrFileBo bo, PageQuery pageQuery) {
        return hlvrFileService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出文件资源管理列表
     */
    @SaCheckPermission("business:file:export")
    @Log(title = "文件资源管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HlvrFileBo bo, HttpServletResponse response) {
        List<HlvrFileVo> list = hlvrFileService.queryList(bo);
        ExcelUtil.exportExcel(list, "文件资源管理", HlvrFileVo.class, response);
    }

    /**
     * 获取文件资源管理详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("business:file:query")
    @GetMapping("/{id}")
    public R<HlvrFileVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(hlvrFileService.queryById(id));
    }

    /**
     * 新增文件资源管理
     */
    @SaCheckPermission("business:file:add")
    @Log(title = "文件资源管理", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    @UploadAttachment
    public R<HlvrFileBo> add(@RequestPart(value = "file", required = false) List<MultipartFile> file,
                       HlvrFileBo bo) {
        hlvrFileService.insertByBo(bo);
        return R.ok(bo);
    }

    /**
     * 修改文件资源管理
     */
    @SaCheckPermission("business:file:edit")
    @Log(title = "文件资源管理", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    @UploadAttachment
    public R<HlvrFileBo> edit(@RequestPart(value = "file", required = false) List<MultipartFile> file,
                        HlvrFileBo bo) {
        hlvrFileService.updateByBo(bo);
        return R.ok(bo);
    }

    /**
     * 删除文件资源管理
     *
     * @param ids 主键串
     */
    @SaCheckPermission("business:file:remove")
    @Log(title = "文件资源管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(hlvrFileService.deleteWithValidByIds(List.of(ids), true));
    }
}
