import { request } from '@/service/request';

/** 获取培训配置列表 */
export function fetchGetTrainingExamList (params?: Api.Business.TrainingExamSearchParams) {
    return request<Api.Business.TrainingExamList>({
        url: '/business/training/list',
        method: 'get',
        params
    });
}
/** 新增培训配置 */
export function fetchCreateTrainingExam (data: Api.Business.TrainingExamOperateParams) {
    return request<boolean>({
        url: '/business/training',
        method: 'post',
        data
    });
}

/** 修改培训配置 */
export function fetchUpdateTrainingExam (data: Api.Business.TrainingExamOperateParams) {
    return request<boolean>({
        url: '/business/training',
        method: 'put',
        data
    });
}

/** 批量删除培训配置 */
export function fetchBatchDeleteTrainingExam (ids: CommonType.IdType[]) {
    return request<boolean>({
        url: `/business/training/${ids.join(',')}`,
        method: 'delete'
    });
}

export function getSopCardList (params?: any) {
    return request({
        url: `/business/sopCard/getList`,
        method: 'get',
        params
    });
}

/** 根据id查详情 */
export function getInfo (id:any) {
    return request<boolean>({
        url: `/business/training/${id}`,
        method: 'get'
    });
}
