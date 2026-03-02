declare namespace Api {
  namespace Business {
    interface NameValue {
      name: string;
      value: number;
    }

    interface TrendItem {
      label: string;
      value: number;
    }

    interface HomeStats {
      core: {
        totalBatchCount: number;
        trainBatchCount: number;
        examBatchCount: number;
        batchTrend7d: TrendItem[];
        totalCardCount: number;
        cardTypeDistribution: NameValue[];
        cardModeDistribution: NameValue[];
        totalQuestionCount: number;
        questionTypeDistribution: NameValue[];
        coveredDeptCount: number;
        deptDistribution: NameValue[];
        totalAssetCount: number;
        assetTypeDistribution: NameValue[];
      };
      business: {
        trainConfig: { today: number; week: number; month: number };
        examConfig: { today: number; week: number; month: number };
        completedBatchToday: number;
        totalBatchToday: number;
        completedRateToday: number;
        completedBatchWeek: number;
        totalBatchWeek: number;
        completedRateWeek: number;
        completedBatchMonth: number;
        totalBatchMonth: number;
        completedRateMonth: number;
        avgPassRate: number;
        passRateTrend7d: TrendItem[];
        deptTop5: NameValue[];
      };
      system: {
        totalUsers: number;
        newUsersToday: number;
        newUsersWeek: number;
        userStatusDistribution: NameValue[];
        userDeptTop5: NameValue[];
        totalRoleCount: number;
        totalMenuCount: number;
        totalDeptCount: number;
        totalPostCount: number;
        loginCountToday: number;
        loginSuccessRateToday: number;
        loginTrend24h: TrendItem[];
      };
    }
  }
}
