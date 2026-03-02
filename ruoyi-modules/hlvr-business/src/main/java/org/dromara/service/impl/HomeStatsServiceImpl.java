package org.dromara.service.impl;

import lombok.RequiredArgsConstructor;
import org.dromara.domain.vo.HomeStatsVo;
import org.dromara.service.IHomeStatsService;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class HomeStatsServiceImpl implements IHomeStatsService {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public HomeStatsVo getStats(LocalDate beginDate, LocalDate endDate) {
        HomeStatsVo vo = new HomeStatsVo();
        vo.setCore(buildCoreStats(beginDate, endDate));
        vo.setBusiness(buildBusinessStats());
        vo.setSystem(buildSystemStats());
        return vo;
    }

    private HomeStatsVo.CoreStats buildCoreStats(LocalDate beginDate, LocalDate endDate) {
        HomeStatsVo.CoreStats core = new HomeStatsVo.CoreStats();
        Timestamp begin = Timestamp.valueOf(beginDate.atStartOfDay());
        Timestamp end = Timestamp.valueOf(endDate.atTime(LocalTime.MAX));

        core.setTotalBatchCount(queryLong("select count(*) from hlvr_te_batch where del_flag = 0"));
        core.setTrainBatchCount(queryLong("select count(*) from hlvr_te_batch where del_flag = 0 and hte_id in (select id from hlvr_training_exam where del_flag=0 and type='train')"));
        core.setExamBatchCount(queryLong("select count(*) from hlvr_te_batch where del_flag = 0 and hte_id in (select id from hlvr_training_exam where del_flag=0 and type='exam')"));

        List<HomeStatsVo.TrendItem> trend7d = new ArrayList<>();
        List<Map<String, Object>> rows = jdbcTemplate.queryForList("""
            select date_format(create_time, '%m-%d') as d, count(*) as c
            from hlvr_te_batch
            where del_flag = 0 and create_time between ? and ?
            group by date_format(create_time, '%m-%d')
            order by min(create_time)
            """, begin, end);
        for (Map<String, Object> row : rows) {
            trend7d.add(new HomeStatsVo.TrendItem(String.valueOf(row.get("d")), toDouble(row.get("c"))));
        }
        core.setBatchTrend7d(trend7d);

        core.setTotalCardCount(queryLong("select count(*) from hlvr_sop_card where del_flag = 0"));
        core.setCardTypeDistribution(queryNameValue("select case when sop_card_type = 0 then 'VR' else 'PC' end as n, count(*) as v from hlvr_sop_card where del_flag=0 group by sop_card_type"));
        core.setCardModeDistribution(queryNameValue("select case when sop_card_model = 'train' then '培训' when sop_card_model = 'exam' then '考核' else '未知' end as n, count(*) as v from hlvr_sop_card where del_flag=0 group by sop_card_model"));

        core.setTotalQuestionCount(queryLong("select count(*) from hlvr_question where del_flag = 0"));
        core.setQuestionTypeDistribution(queryNameValue("select case when question_type='0' then '判断题' when question_type='1' then '选择题' when question_type='2' then '操作匹配题' else '步骤跳转题' end as n, count(*) as v from hlvr_question where del_flag=0 group by question_type"));

        core.setCoveredDeptCount(queryLong("select count(distinct dept_id) from hlvr_training_exam where del_flag=0 and dept_id is not null"));
        core.setDeptDistribution(queryNameValue("select sd.dept_name as n, count(*) as v from hlvr_training_exam hte join sys_dept sd on hte.dept_id=sd.dept_id where hte.del_flag=0 group by sd.dept_name order by v desc limit 8"));

        long attachmentCount = safeQueryLong("select count(*) from hlvr_attachment where del_flag = 0");
        long elementCount = queryLong("select count(*) from hlvr_element where del_flag = 0");
        long fileCount = queryLong("select count(*) from hlvr_file where del_flag = 0");
        core.setTotalAssetCount(attachmentCount + elementCount + fileCount);
        List<HomeStatsVo.NameValue> assets = new ArrayList<>();
        assets.add(new HomeStatsVo.NameValue("模型", elementCount));
        assets.add(new HomeStatsVo.NameValue("文件", fileCount));
        assets.add(new HomeStatsVo.NameValue("附件", attachmentCount));
        core.setAssetTypeDistribution(assets);
        return core;
    }

    private HomeStatsVo.BusinessStats buildBusinessStats() {
        HomeStatsVo.BusinessStats business = new HomeStatsVo.BusinessStats();
        business.setTrainConfig(periodCountByType("train"));
        business.setExamConfig(periodCountByType("exam"));

        business.setCompletedBatchToday(queryLong("select count(*) from hlvr_te_batch where del_flag=0 and date(end_time)=curdate() and end_time is not null"));
        business.setTotalBatchToday(queryLong("select count(*) from hlvr_te_batch where del_flag=0 and date(create_time)=curdate()"));
        business.setCompletedRateToday(calcRate(business.getCompletedBatchToday(), business.getTotalBatchToday()));

        business.setCompletedBatchWeek(queryLong("select count(*) from hlvr_te_batch where del_flag=0 and yearweek(end_time,1)=yearweek(curdate(),1) and end_time is not null"));
        business.setTotalBatchWeek(queryLong("select count(*) from hlvr_te_batch where del_flag=0 and yearweek(create_time,1)=yearweek(curdate(),1)"));
        business.setCompletedRateWeek(calcRate(business.getCompletedBatchWeek(), business.getTotalBatchWeek()));

        business.setCompletedBatchMonth(queryLong("select count(*) from hlvr_te_batch where del_flag=0 and date_format(end_time,'%Y-%m')=date_format(curdate(),'%Y-%m') and end_time is not null"));
        business.setTotalBatchMonth(queryLong("select count(*) from hlvr_te_batch where del_flag=0 and date_format(create_time,'%Y-%m')=date_format(curdate(),'%Y-%m')"));
        business.setCompletedRateMonth(calcRate(business.getCompletedBatchMonth(), business.getTotalBatchMonth()));

        double avgScore = queryDouble("select ifnull(avg(score),0) from hlvr_sop_card_step_opt_history where del_flag=0");
        business.setAvgPassRate(Math.min(100D, Math.max(0D, avgScore)));

        List<HomeStatsVo.TrendItem> passTrend = new ArrayList<>();
        List<Map<String, Object>> rows = jdbcTemplate.queryForList("""
            select date_format(create_time,'%m-%d') d, ifnull(avg(score),0) v
            from hlvr_sop_card_step_opt_history
            where del_flag=0 and create_time >= date_sub(curdate(), interval 6 day)
            group by date_format(create_time,'%m-%d')
            order by min(create_time)
            """);
        for (Map<String, Object> row : rows) {
            passTrend.add(new HomeStatsVo.TrendItem(String.valueOf(row.get("d")), toDouble(row.get("v"))));
        }
        business.setPassRateTrend7d(passTrend);
        business.setDeptTop5(queryNameValue("select sd.dept_name n, count(htb.id) v from hlvr_te_batch htb join hlvr_training_exam hte on htb.hte_id=hte.id join sys_dept sd on hte.dept_id=sd.dept_id where htb.del_flag=0 and hte.del_flag=0 group by sd.dept_name order by v desc limit 5"));

        return business;
    }

    private HomeStatsVo.SystemStats buildSystemStats() {
        HomeStatsVo.SystemStats system = new HomeStatsVo.SystemStats();
        system.setTotalUsers(queryLong("select count(*) from sys_user where del_flag='0'"));
        system.setNewUsersToday(queryLong("select count(*) from sys_user where del_flag='0' and date(create_time)=curdate()"));
        system.setNewUsersWeek(queryLong("select count(*) from sys_user where del_flag='0' and yearweek(create_time,1)=yearweek(curdate(),1)"));
        system.setUserStatusDistribution(queryNameValue("select case when status='0' then '启用' else '停用' end n, count(*) v from sys_user where del_flag='0' group by status"));
        system.setUserDeptTop5(queryNameValue("select sd.dept_name n, count(su.user_id) v from sys_user su left join sys_dept sd on su.dept_id=sd.dept_id where su.del_flag='0' group by sd.dept_name order by v desc limit 5"));

        system.setTotalRoleCount(queryLong("select count(*) from sys_role where del_flag='0'"));
        system.setTotalMenuCount(queryLong("select count(*) from sys_menu where del_flag='0'"));
        system.setTotalDeptCount(queryLong("select count(*) from sys_dept where del_flag='0'"));
        system.setTotalPostCount(queryLong("select count(*) from sys_post where status='0'"));

        long ok = queryLong("select count(*) from sys_logininfor where status='0' and date(login_time)=curdate()");
        long total = queryLong("select count(*) from sys_logininfor where date(login_time)=curdate()");
        system.setLoginCountToday(total);
        system.setLoginSuccessRateToday(calcRate(ok, total));

        List<HomeStatsVo.TrendItem> loginTrend = new ArrayList<>();
        DateTimeFormatter hourFmt = DateTimeFormatter.ofPattern("HH:00");
        for (int i = 23; i >= 0; i--) {
            LocalDateTime hour = LocalDateTime.now().minusHours(i);
            LocalDateTime next = hour.plusHours(1);
            long cnt = queryLong("select count(*) from sys_logininfor where login_time >= ? and login_time < ?",
                Timestamp.valueOf(hour), Timestamp.valueOf(next));
            loginTrend.add(new HomeStatsVo.TrendItem(hour.format(hourFmt), cnt));
        }
        system.setLoginTrend24h(loginTrend);
        return system;
    }

    private HomeStatsVo.PeriodCount periodCountByType(String type) {
        HomeStatsVo.PeriodCount count = new HomeStatsVo.PeriodCount();
        count.setToday(queryLong("select count(*) from hlvr_training_exam where del_flag=0 and type=? and date(create_time)=curdate()", type));
        count.setWeek(queryLong("select count(*) from hlvr_training_exam where del_flag=0 and type=? and yearweek(create_time,1)=yearweek(curdate(),1)", type));
        count.setMonth(queryLong("select count(*) from hlvr_training_exam where del_flag=0 and type=? and date_format(create_time,'%Y-%m')=date_format(curdate(),'%Y-%m')", type));
        return count;
    }

    private List<HomeStatsVo.NameValue> queryNameValue(String sql, Object... args) {
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, args);
        List<HomeStatsVo.NameValue> result = new ArrayList<>();
        for (Map<String, Object> item : list) {
            result.add(new HomeStatsVo.NameValue(String.valueOf(item.get("n")), toDouble(item.get("v"))));
        }
        return result;
    }

    private long queryLong(String sql, Object... args) {
        Long value = jdbcTemplate.queryForObject(sql, Long.class, args);
        return value == null ? 0L : value;
    }

    private long safeQueryLong(String sql) {
        try {
            return queryLong(sql);
        } catch (DataAccessException e) {
            return 0L;
        }
    }

    private double queryDouble(String sql, Object... args) {
        Double value = jdbcTemplate.queryForObject(sql, Double.class, args);
        return value == null ? 0D : value;
    }

    private double toDouble(Object value) {
        if (value == null) {
            return 0D;
        }
        if (value instanceof Number number) {
            return number.doubleValue();
        }
        return Double.parseDouble(String.valueOf(value));
    }

    private double calcRate(long num, long denominator) {
        if (denominator <= 0) {
            return 0D;
        }
        return Math.round((num * 10000D / denominator)) / 100D;
    }
}
