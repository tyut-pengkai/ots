package org.dromara.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class HomeStatsVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private CoreStats core;
    private BusinessStats business;
    private SystemStats system;

    @Data
    public static class CoreStats implements Serializable {
        private long totalBatchCount;
        private long trainBatchCount;
        private long examBatchCount;
        private List<TrendItem> batchTrend7d;

        private long totalCardCount;
        private List<NameValue> cardTypeDistribution;
        private List<NameValue> cardModeDistribution;

        private long totalQuestionCount;
        private List<NameValue> questionTypeDistribution;

        private long coveredDeptCount;
        private List<NameValue> deptDistribution;

        private long totalAssetCount;
        private List<NameValue> assetTypeDistribution;
    }

    @Data
    public static class BusinessStats implements Serializable {
        private PeriodCount trainConfig;
        private PeriodCount examConfig;

        private long completedBatchToday;
        private long totalBatchToday;
        private double completedRateToday;

        private long completedBatchWeek;
        private long totalBatchWeek;
        private double completedRateWeek;

        private long completedBatchMonth;
        private long totalBatchMonth;
        private double completedRateMonth;

        private double avgPassRate;
        private List<TrendItem> passRateTrend7d;
        private List<NameValue> deptTop5;
    }

    @Data
    public static class SystemStats implements Serializable {
        private long totalUsers;
        private long newUsersToday;
        private long newUsersWeek;
        private List<NameValue> userStatusDistribution;
        private List<NameValue> userDeptTop5;

        private long totalRoleCount;
        private long totalMenuCount;

        private long totalDeptCount;
        private long totalPostCount;

        private long loginCountToday;
        private double loginSuccessRateToday;
        private List<TrendItem> loginTrend24h;
    }

    @Data
    public static class PeriodCount implements Serializable {
        private long today;
        private long week;
        private long month;
    }

    @Data
    public static class TrendItem implements Serializable {
        private String label;
        private double value;

        public TrendItem(String label, double value) {
            this.label = label;
            this.value = value;
        }
    }

    @Data
    public static class NameValue implements Serializable {
        private String name;
        private double value;

        public NameValue(String name, double value) {
            this.name = name;
            this.value = value;
        }
    }
}
