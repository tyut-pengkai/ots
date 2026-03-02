import { request } from '@/service/request';

/** 获取问题列表 */
export function fetchGetQuestionList (params?: Api.Business.QuestionSearchParams) {
    return request<Api.Business.QuestionList>({
        url: '/business/question/list',
        method: 'get',
        params
    });
}
/** 新增问题 */
export function fetchCreateQuestion (data: Api.Business.QuestionOperateParams) {
    return request<boolean>({
        url: '/business/question',
        method: 'post',
        data
    });
}

/** 修改问题 */
export function fetchUpdateQuestion (data: Api.Business.QuestionOperateParams) {
    return request<boolean>({
        url: '/business/question',
        method: 'put',
        data
    });
}

/** 批量删除问题 */
export function fetchBatchDeleteQuestion (ids: CommonType.IdType[]) {
    return request<boolean>({
        url: `/business/question/${ids.join(',')}`,
        method: 'delete'
    });
}

/** 查询操作卡列表 */
export function getSopCardList () {
    return request<boolean>({
        url: `/business/sopCard/getList`,
        method: 'get'
    });
}

/** 查询模型列表 */
export function getElementList (query:any) {
    return request<boolean>({
        url: `/business/element/getList`,
        method: 'get',
        params: query
    });
}

/** 获取步骤列表 */
export function getSopCardStepOpt (id:any) {
    return request<boolean>({
        url: `/business/sopCardStepOpt/getStepsByCardId/${id}`,
        method: 'get'
    });
}

/** 根据id查详情 */
export function getQuestionInfo (id:any) {
    return request<boolean>({
        url: `/business/question/${id}`,
        method: 'get'
    });
}

/** 提交答案 */
export function submitAnswer (query:any) {
    return request<boolean>({
        url: `/business/question/submit`,
        method: 'post',
        data: query
    });
}


