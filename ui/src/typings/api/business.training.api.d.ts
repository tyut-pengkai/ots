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
        type TrainingExam = Common.CommonRecord<{
            /** id */
                id: CommonType.IdType; 
            /** 培训名称 */
                name: string; 
            /** 参与组织 */
                deptId: CommonType.IdType; 
            /** 参与组织名称 */
                deptName: string; 
            /** $column.columnComment */
                delFlag: string; 
            /** 允许参与次数 */
                batchTimes: string; 
            /** 通过的分数线 */
                passingScore: string; 
            /** train=培训，exam=考核 */
                type: string; 
        }>;

        /** training exam search params */
        type TrainingExamSearchParams = CommonType.RecordNullable<
            Pick<
                Api.Business.TrainingExam,
                        | 'name'
                        | 'deptId'
                        | 'type'
            > &
            Api.Common.CommonSearchParams
        >;

        /** training exam operate params */
        type TrainingExamOperateParams = CommonType.RecordNullable<
            Pick<
                Api.Business.TrainingExam,
                        | 'id'
                        | 'name'
                        | 'deptId'
                        | 'deptName'
                        | 'createBy'
                        | 'batchTimes'
                        | 'passingScore'
                        | 'type'
                        | 'updateTime'
            >
        >;

        /** training exam list */
        type TrainingExamList = Api.Common.PaginatingQueryRecord<TrainingExam>;
    }
}
