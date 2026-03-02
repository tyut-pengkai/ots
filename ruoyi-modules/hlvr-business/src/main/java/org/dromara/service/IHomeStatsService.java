package org.dromara.service;

import org.dromara.domain.vo.HomeStatsVo;

import java.time.LocalDate;

public interface IHomeStatsService {

    HomeStatsVo getStats(LocalDate beginDate, LocalDate endDate);
}
