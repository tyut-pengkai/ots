package org.dromara.controller;

import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.domain.vo.HomeStatsVo;
import org.dromara.service.IHomeStatsService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RequiredArgsConstructor
@RestController
@RequestMapping("/business/home")
public class HomeStatsController {

    private final IHomeStatsService homeStatsService;

    @GetMapping("/stats")
    public R<HomeStatsVo> stats(@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate beginDate,
                                @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        LocalDate end = endDate == null ? LocalDate.now() : endDate;
        LocalDate begin = beginDate == null ? end.minusDays(6) : beginDate;
        if (begin.isAfter(end)) {
            begin = end.minusDays(6);
        }
        return R.ok(homeStatsService.getStats(begin, end));
    }
}
