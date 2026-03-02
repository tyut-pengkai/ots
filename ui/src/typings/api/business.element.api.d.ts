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
        /** element */
        type Element = Common.CommonRecord<{
            /** $column.columnComment */
                id: string;
            /** 模型标识 */
                ueId: string;
            /** 模型类别 */
                elementType: string;
            /** 模型名称 */
                name: string;
            /** 坐标 */
                position: string;
            /** 描述 */
                description: string;
            /** $column.columnComment */
                delFlag: string;
                file: any;
                path: any;
        }>;

        /** element search params */
        type ElementSearchParams = CommonType.RecordNullable<
            Pick<
                Api.Business.Element,
                        | 'id'
                        | 'ueId'
                        | 'elementType'
                        | 'name'
                        | 'description'
            > &
            Api.Common.CommonSearchParams
        >;

        /** element operate params */
        type ElementOperateParams = CommonType.RecordNullable<
            Pick<
                Api.Business.Element,
                        | 'id'
                        | 'ueId'
                        | 'elementType'
                        | 'name'
                        | 'description'
                        | 'file'
            >
        >;

        /** element list */
        type ElementList = Api.Common.PaginatingQueryRecord<Element>;
    }
}
