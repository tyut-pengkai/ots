package org.dromara.controller;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.domain.vo.HlvrSopCardStepResultVo;
import org.dromara.service.IHlvrSopCardStepResultService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/sopCardStepResult")
public class HlvrSopCardstepResultController {

    private final IHlvrSopCardStepResultService hlvrSopCardStepResultService;
    /**
     * 获取弹窗类步骤内容，用于网页渲染
     * @param id 步骤id
     */
    @GetMapping("/{id}")
    public R<HlvrSopCardStepResultVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {

        return  R.ok(hlvrSopCardStepResultService.selectPopwindowInfo(id));
    }

}
