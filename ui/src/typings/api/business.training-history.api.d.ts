/**
 * Namespace Api
 *
 * All backend api type
 */
declare namespace Api {
    /**
     * namespace Business
     *
     * backend api module: "Business"
     */
    namespace Business {
        /** training exam */
        type TrainingHistory = Common.CommonRecord<{
            /** id */
                hteId: string;
            /** 操作卡id */
                hscId: string;
            /** 培训名称 */
                name: string; 
            /** 参与组织 */
                deptId: number; 
            /** 参与组织名称 */
                deptName: string; 
            /** 培训参与人数 */
                people: string; 
            /** 培训总次数 */
                num: string; 
                totalScore: number;
                batchId: string;
                costTime: number;
                optCardNum: number;
                peopleAcc: number;
                completRate:number;
                createName: string;
        }>;
        type SopCardStepOptHistory = Common.CommonRecord<{
            /** 批次id */
            batchId: number; 
            /** 操作卡步骤id */
            hsceoId: number; 
            /** 历史记录id */
            id: number; 
            /** 下一步骤id */
            nextStep: number; 
            /** 步骤名称 */
            groupName: string; 
            /** 操作内容 */
            optContent: string; 
            /** 得分 */
            score: number; 
            /** 耗时 */
            costTime: number;
            createTime: string;
        }>;

        /** training exam search params */
        type TrainingHistorySearchParams = CommonType.RecordNullable<
            Pick<
                Api.Business.TrainingExam,
                        | 'name'
                        | 'deptId'
                        | 'type'
            > &
            Api.Common.CommonSearchParams
        >;
        /** training exam list */
        type TrainingHistoryPage = Api.Common.PaginatingQueryRecord<TrainingHistory>;
        type TrainingHistoryList = TrainingHistory[];
        type SopCardStepOptPage = Api.Common.PaginatingQueryRecord<SopCardStepOptHistory>;
    }
}
