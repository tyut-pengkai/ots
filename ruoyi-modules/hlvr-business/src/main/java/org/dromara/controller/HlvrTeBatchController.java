package org.dromara.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.*;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.dromara.domain.bo.HlvrTrainingExamBo;
import org.dromara.domain.vo.HlvrSopCardStepOptHistoryVo;
import org.dromara.domain.vo.HlvrTeBatchHistoryVo;
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
import org.dromara.domain.vo.HlvrTeBatchVo;
import org.dromara.domain.bo.HlvrTeBatchBo;
import org.dromara.service.IHlvrTeBatchService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 培训/考核 批次
 *
 * @author admin
 * @date 2025-11-21
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/teBatch")
public class HlvrTeBatchController extends BaseController {

    private final IHlvrTeBatchService hlvrTeBatchService;

    /**
     * 查询培训/考核 批次列表
     */
    @SaCheckPermission("business:teBatch:list")
    @GetMapping("/list")
    public TableDataInfo<HlvrTeBatchVo> list(HlvrTeBatchBo bo, PageQuery pageQuery) {
        return hlvrTeBatchService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出培训/考核 批次列表
     */
    @Log(title = "培训/考核 批次", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HlvrTeBatchBo bo, HttpServletResponse response) {
        List<HlvrTeBatchVo> list = hlvrTeBatchService.queryList(bo);
        ExcelUtil.exportExcel(list, "培训/考核 批次", HlvrTeBatchVo.class, response);
    }

    /**
     * 获取培训/考核 批次详细信息
     *
     * @param id 主键
     */
    @GetMapping("/{id}")
    public R<HlvrTeBatchVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(hlvrTeBatchService.queryById(id));
    }

    /**
     * 新增培训/考核 批次
     */
    @SaCheckPermission("business:teBatch:add")
    @Log(title = "培训/考核 批次", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Long> add(@Validated(AddGroup.class) @RequestBody HlvrTeBatchBo bo) {
        return R.ok(hlvrTeBatchService.insertByBo(bo));
    }

    /**
     * 修改培训/考核 批次
     */
    @SaCheckPermission("business:teBatch:edit")
    @Log(title = "培训/考核 批次", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody HlvrTeBatchBo bo) {
        return toAjax(hlvrTeBatchService.updateByBo(bo));
    }

    /**
     * 删除培训/考核 批次
     *
     * @param ids 主键串
     */
    @SaCheckPermission("business:teBatch:remove")
    @Log(title = "培训/考核 批次", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(hlvrTeBatchService.deleteWithValidByIds(List.of(ids), true));
    }

    @Log(title = "查询培训记录列表", businessType = BusinessType.DELETE)
    @GetMapping("/history")
    public TableDataInfo<HlvrTeBatchHistoryVo> history(HlvrTrainingExamBo bo, PageQuery pageQuery) {
        return hlvrTeBatchService.history(bo, pageQuery);
    }

    @Log(title = "获取参与培训的人员列表", businessType = BusinessType.DELETE)
    @GetMapping("/examPeople/{id}")
    public TableDataInfo<HlvrTeBatchHistoryVo> getExamPeople(@PathVariable long id, PageQuery pageQuery) {
        return hlvrTeBatchService.getExamPeople(id, pageQuery);
    }

    @Log(title = "获取参与培训的人员的参与批次", businessType = BusinessType.DELETE)
    @GetMapping("/peopleBatch/{id}/{peopleId}")
    public R<List<HlvrTeBatchHistoryVo>> peopleBatch(@PathVariable long id, @PathVariable long peopleId) {
        return R.ok(hlvrTeBatchService.peopleBatch(id, peopleId));
    }

    @Log(title = "获取参与操作卡的步骤列表", businessType = BusinessType.DELETE)
    @GetMapping("/batchHistoryDetail/{batchId}")
    public TableDataInfo<HlvrSopCardStepOptHistoryVo> batchHistoryDetail(@RequestParam Long hscId, PageQuery pageQuery) {
        return hlvrTeBatchService.getOpts(hscId, pageQuery);
    }

    @Log(title = "获取参与培训的人员的批次得分详情", businessType = BusinessType.DELETE)
    @GetMapping("/optHistoryDetail/{batchId}")
    public TableDataInfo<HlvrSopCardStepOptHistoryVo> optHistoryDetail(@PathVariable Long batchId, PageQuery pageQuery) {
        return hlvrTeBatchService.optHistoryDetail(batchId, pageQuery);
    }

    @Log(title = "获取当前登录人参与培训的记录", businessType = BusinessType.DELETE)
    @GetMapping("/personal")
    public TableDataInfo<HlvrTeBatchHistoryVo> getPersonal(PageQuery pageQuery) {
        return hlvrTeBatchService.getPersonal(pageQuery);
    }
    @Log(title = "获取当前登录人参与培训的记录", businessType = BusinessType.DELETE)
    @GetMapping("/personalBatch/{id}")
    public R<List<HlvrTeBatchHistoryVo>> personalBatch(@PathVariable long id) {
        return R.ok(hlvrTeBatchService.personalBatch(id));
    }
}
