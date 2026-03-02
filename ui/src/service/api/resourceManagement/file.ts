import { request } from '@/service/request';

/** 获取文件资源列表 */
export function getFileList (params:any) {
    return request({
        url: '/business/file/list',
        method: 'get',
        params
    });
}

/** 批量删文件资源 */
export function fetchBatchDeleteFile (ids:any) {
    return request<boolean>({
        url: `/business/file/${ids.join(',')}`,
        method: 'delete'
    });
}

/** 新增文件资源 */
export function addFile (query:any) {
    return request<boolean>({
        url: `/business/file`,
        method: 'post',
        data: query
    });
}

/** 编辑文件资源 */
export function editFile (query:any) {
    return request<boolean>({
        url: `/business/file`,
        method: 'put',
        data: query
    });
}

/** 根据id查详情 */
export function getFileInfo (id:any) {
    return request<boolean>({
        url: `/business/file/${id}`,
        method: 'get'
    });
}
