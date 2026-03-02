<template>
    <div class="min-h-500px flex-col-stretch gap-16px overflow-hidden lt-sm:overflow-auto">
        <NCard :bordered="false" class="card-wrapper" size="small">
            <NCollapse>
                <NCollapseItem :title="$t('common.search')" name="user-search">
                    <NForm ref="formRef" :label-width="80" :model="searchParams" label-placement="left">
                        <NGrid item-responsive responsive="screen">
                            <NFormItemGi class="pr-24px" label="文件原名" path="name" span="24 s:12 m:6">
                                <NInput v-model:value="searchParams.name" placeholder="请输入文件原名"/>
                            </NFormItemGi>
                            <NFormItemGi class="pr-24px" span="24">
                                <NSpace class="w-full" justify="end">
                                    <NButton @click="reset">
                                        <template #icon>
                                            <icon-ic-round-refresh class="text-icon"/>
                                        </template>
                                        {{ $t('common.reset') }}
                                    </NButton>
                                    <NButton ghost type="primary" @click="search">
                                        <template #icon>
                                            <icon-ic-round-search class="text-icon"/>
                                        </template>
                                        {{ $t('common.search') }}
                                    </NButton>
                                </NSpace>
                            </NFormItemGi>
                        </NGrid>
                    </NForm>
                </NCollapseItem>
            </NCollapse>
        </NCard>
        <NCard :bordered="false" class="sm:flex-1-hidden card-wrapper" size="small" title="文件资产管理列表">
            <template #header-extra>
                <TableHeaderOperation
                    v-model:columns="columnChecks"
                    :disabled-delete="checkedRowKeys.length === 0"
                    :loading="loading"
                    :show-add="hasAuth('business:element:add')"
                    :show-delete="hasAuth('business:element:remove')"
                    :show-export="hasAuth('business:element:export')"
                    @add="handleAdd"
                    @delete="handleBatchDelete"
                    @export="handleExport"
                    @refresh="getData"
                />
            </template>
            <NDataTable
                v-model:checked-row-keys="checkedRowKeys"
                :columns="columns"
                :data="data"
                :flex-height="!appStore.isMobile"
                :loading="loading"
                :pagination="mobilePagination"
                :row-key="row => row.id"
                :scroll-x="962"
                class="sm:h-full"
                remote
                size="small"
            />
            <OperateDrawer
                v-model:visible="drawerVisible"
                :operate-type="operateType"
                :row-data="editingData"
                @submitted="getDataByPage"
            />
        </NCard>
    </div>
</template>

<script lang="tsx" setup>
    import {$t} from "@/locales";
    import {ref, h} from "vue";
    import {NButton, NDivider} from "naive-ui";
    import { getFileList, fetchBatchDeleteFile } from '@/service/api/resourceManagement/file';
    import {useTable, useTableOperate} from "@/hooks/common/table";
    import ButtonIcon from "@/components/custom/button-icon.vue";
    import {useAuth} from "@/hooks/business/auth";
    import {useAppStore} from "@/store/modules/app";
    import OperateDrawer from "./components/operate-drawer.vue";
    import {useDict} from "@/hooks/business/dict";
    import DictTag from "@/components/custom/dict-tag.vue";
    import { useRouter } from "vue-router";

    const router = useRouter();
    useDict('file_asset_type');
    const appStore = useAppStore();
    const {hasAuth} = useAuth();
    const reset = ()=>{
        resetSearchParams();
    }
    const search = ()=>{
        getDataByPage();
    }

    const {
        columns,
        columnChecks,
        data,
        getData,
        getDataByPage,
        loading,
        mobilePagination,
        searchParams,
        resetSearchParams
    } = useTable({
        apiFn: getFileList,
        apiParams: {
            pageNum: 1,
            pageSize: 10,
            // if you want to use the searchParams in Form, you need to define the following properties, and the value is null
            // the value can not be undefined, otherwise the property in Form will not be reactive
            id: null,
            name: null
        },
        columns: ():any => [
            {
                type: 'selection',
                align: 'center',
                width: 48
            },
            {
                key: 'index',
                title: $t('common.index'),
                align: 'center',
                width: 64
            },
            {
                key: 'updateName',
                title: '文件名',
                align: 'center',
                minWidth: 120,
            },
            {
                key: 'assetType',
                title: '文件类别',
                align: 'center',
                minWidth: 120,
                render: (row:any):any => {
                    return <DictTag value={row.assetType} dictCode="file_asset_type"/>;
                },
            },
            {
                key: 'name',
                title: '原名',
                align: 'center',
                minWidth: 120,
            },
            {
                key: 'suffix',
                title: '文件后缀名',
                align: 'center',
                minWidth: 120,
            },
            {
                key: 'path',
                title: 'URL地址',
                align: 'center',
                minWidth: 120,
                render: (row:any):any => {
                    return h('a',{
                        href: row.path,
                        target: '_blank'
                    },row.path);
                },
            },
            {
                key: 'createBy',
                title: '创建人',
                align: 'center',
                minWidth: 120,
            },
            {
                key: 'createTime',
                title: '创建时间',
                align: 'center',
                minWidth: 120,
            },
            {
                key: 'operate',
                title: $t('common.operate'),
                align: 'center',
                width: 130,
                render: (row:any) => {
                    const divider = () => {
                        if (!hasAuth('business:element:edit') || !hasAuth('business:element:remove')) {
                            return null;
                        }
                        return <NDivider vertical/>;
                    };

                    const editBtn = () => {
                        if (!hasAuth('business:element:edit')) {
                            return null;
                        }
                        return (
                            <ButtonIcon
                                text
                                type="primary"
                                icon="material-symbols:drive-file-rename-outline-outline"
                                tooltipContent={$t('common.edit')}
                                onClick={() => edit(row.id!)}
                            />
                        );
                    };

                    const deleteBtn = () => {
                        if (!hasAuth('business:element:remove')) {
                            return null;
                        }
                        return (
                            <ButtonIcon
                                text
                                type="error"
                                icon="material-symbols:delete-outline"
                                tooltipContent={$t('common.delete')}
                                popconfirmContent={$t('common.confirmDelete')}
                                onPositiveClick={() => handleDelete(row.id!)}
                            />
                        );
                    };
                    return (
                        <div class="flex-center gap-8px">
                            {divider()}
                            {editBtn()}
                            {divider()}
                            {deleteBtn()}
                        </div>
                    );
                }
            }
        ]
    });

    console.log('data==============>>>',data);
    const {drawerVisible, operateType, editingData, handleAdd, handleEdit, checkedRowKeys, onBatchDeleted, onDeleted} =
        useTableOperate(data, getData);
    async function handleBatchDelete() {
        // request
        const {error} = await fetchBatchDeleteFile(checkedRowKeys.value);
        if (error) return;
        onBatchDeleted();
    }

    const edit = (id:any)=>{
        handleEdit('id', id);
    }
    const handleDelete = async (id:any)=>{
        const {error} = await fetchBatchDeleteFile([id]);
        if (error) return;
        onDeleted();
    }

    function handleExport() {
        // download('/business/element/export', searchParams, `资产管理_${new Date().getTime()}.xlsx`);
    }
</script>
