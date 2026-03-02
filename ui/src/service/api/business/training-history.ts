import { request } from '@/service/request';
/** 获取培训历史列表 */
export function getTrainingHistoryList (params?: Api.Business.TrainingHistorySearchParams) {
    return request<Api.Business.TrainingHistoryPage>({
        url: '/business/teBatch/history',
        method: 'get',
        params
    });
}

/** 获取参与本次培训的人员 */
export function getPeopleList (params: any ) {
    const { pageNum, pageSize, hteId } = params;
    return request<Api.Business.TrainingHistoryPage>({
        url: `/business/teBatch/examPeople/${hteId}`,
        method: 'get',
        params: { pageNum, pageSize },
    });
}
/** 获取指定人员的培训记录 */
export function getPeopleHistory (id:any, people: any) {
    return request<Api.Business.TrainingHistoryList>({
        url: `/business/teBatch/peopleBatch/${id}/${people}`,
        method: 'get'
    });
}

/** 获取该人员的本次的培训明细 */
export function getTrainingStepInfo (params: any) {
    const { pageNum, pageSize, batchId } = params;
    return request<Api.Business.SopCardStepOptPage>({
        url: `/business/teBatch/optHistoryDetail/${batchId}?hscId=${params.hscId}`,
        method: 'get',
        params: { pageNum, pageSize },
    });
}
