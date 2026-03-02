import { request } from '@/service/request';

/** 获取文件资源 */
export function getSopCardStepResult (id:any) {
    return request({
        url: '/business/sopCardStepResult/'+id,
        method: 'get'
    });
}
