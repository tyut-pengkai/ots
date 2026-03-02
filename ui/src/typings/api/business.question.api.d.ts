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
        /** question */
        type Question = Common.CommonRecord<{
            /** id */
                id: CommonType.IdType;
            /** 操作卡id */
                hscId: CommonType.IdType;
            /** 操作卡名称 */
                hscName: string;
            /** 问题名称 */
                questionName: string;
            /** 问题描述 */
                questionDescription: any;
            /** 问题类型 */
                questionType: any;
            /** $column.columnComment */
                delFlag: string;
            /** 选项 */
                questionItemBos: any;
        }>;

        /** question search params */
        type QuestionSearchParams = CommonType.RecordNullable<
            Pick<
                Api.Business.Question,
                        | 'hscId'
                        | 'questionName'
                        | 'questionDescription'
                        | 'questionType'
            > &
            Api.Common.CommonSearchParams
        >;

        /** question operate params */
        type QuestionOperateParams = CommonType.RecordNullable<
            Pick<
                Api.Business.Question,
                        | 'id'
                        | 'hscId'
                        | 'hscName'
                        | 'questionName'
                        | 'questionDescription'
                        | 'questionType'
                        | 'questionItemBos'
            >
        >;

        /** question list */
        type QuestionList = Api.Common.PaginatingQueryRecord<Question>;
    }
}
