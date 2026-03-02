import { request } from '@/service/request';

/** 获取资产管理列表 */
export function fetchGetElementList (params?: Api.Business.ElementSearchParams) {
    return request<Api.Business.ElementList>({
        url: '/business/element/list',
        method: 'get',
        params
    });
}
/** 新增资产管理 */
export function fetchCreateElement (data: FormData) {
    return request<boolean>({
        url: '/business/element',
        method: 'post',
        data
    });
}

/** 修改资产管理 */
export function fetchUpdateElement (data: FormData) {
    return request<boolean>({
        url: '/business/element',
        method: 'put',
        data
    });
}

/** 批量删除资产管理 */
export function fetchBatchDeleteElement (ids: CommonType.IdType[]) {
    return request<boolean>({
        url: `/business/element/${ids.join(',')}`,
        method: 'delete'
    });
}

/** 根据id查询资源详情 */
export function getElementInfo (id:any) {
    return request<boolean>({
        url: '/business/element/'+id,
        method: 'get'
    });
}
