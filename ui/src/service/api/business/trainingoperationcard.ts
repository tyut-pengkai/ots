import { request } from '@/service/request';

/** 新增操作卡 */
export function addStepOne (data:any) {
    return request<boolean>({
        url: '/business/sopCard',
        method: 'post',
        data
    });
}

/** 查询操作卡列表 */
export function getSopCardList (data:any) {
    return request<boolean>({
        url: '/business/sopCard/list',
        method: 'get',
        params: data
    });
}

/** 修改操作卡列表 */
export function editStepOne (data:any) {
    return request<boolean>({
        url: '/business/sopCard',
        method: 'put',
        data
    });
}

/** 根据id查询详情 */
export function getSopCardInfo(id:any) {
    return request<boolean>({
        url: `/business/sopCard/${id}`,
        method: 'get'
    });
}

/** 删除操作卡列表 */
export function delSopCard(ids:CommonType.IdType[]) {
    return request<boolean>({
        url: `/business/sopCard/${ids.join(',')}`,
        method: 'delete'
    });
}


/** 查询操作卡分组列表 */
export function getSopCardStepGroupList (data:any) {
    return request<boolean>({
        url: '/business/sopCardStepGroup/list',
        method: 'get',
        params: data
    });
}
/** 新增操作卡分组列表 */
export function addSopCardStepGroup (data:any) {
    return request<boolean>({
        url: '/business/sopCardStepGroup',
        method: 'post',
        data
    });
}
/** 修改操作卡分组列表 */
export function editSopCardStepGroup (data:any) {
    return request<boolean>({
        url: '/business/sopCardStepGroup',
        method: 'put',
        data
    });
}
/** 删除操作卡分组列表 */
export function delSopCardStepGroup (ids:CommonType.IdType[]) {
    return request<boolean>({
        url: `/business/sopCardStepGroup/${ids.join(',')}`,
        method: 'delete'
    });
}
/** 根据id查询操作卡分组详情 */
export function getSopCardStepGroupInfo (id:any) {
    return request<boolean>({
        url: '/business/sopCardStepGroup/'+id,
        method: 'get'
    });
}

/** 新增操作卡步骤 */
export function addSopCardStepOpt (data:any) {
    return request<boolean>({
        url: '/business/sopCardStepOpt',
        method: 'post',
        data
    });
}

/** 获取操作卡分组下的步骤 */
export function getStepsByGroupId (data:any) {
    return request<boolean>({
        url: '/business/sopCardStepOpt/getStepsByGroupId/'+data.hscId,
        method: 'get',
        params: data
    });
}

/** 删除操作卡步骤 */
export function delSopCardStepOpt (id:any) {
    return request<boolean>({
        url: '/business/sopCardStepOpt/'+id,
        method: 'delete'
    });
}

/** 根据id获取操作卡步骤详情 */
export function getSopCardStepOptInfo (id:any) {
    return request<boolean>({
        url: '/business/sopCardStepOpt/'+id,
        method: 'get'
    });
}

/** 编辑操作卡步骤 */
export function editSopCardStepOptInfo (data:any) {
    return request<boolean>({
        url: '/business/sopCardStepOpt',
        method: 'put',
        data: data
    });
}


/** 复制操作卡 */
export function sopCardCopy (data:any) {
    return request<boolean>({
        url: '/business/sopCard/copy',
        method: 'post',
        data: data
    });
}

/** 查询操作卡所有数据 */
export function getSopCardData (data:any) {
    return request<boolean>({
        url: '/business/sopCard/getList',
        method: 'get',
        params: data
    });
}
