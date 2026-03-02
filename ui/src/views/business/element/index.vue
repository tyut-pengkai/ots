<script setup lang="tsx">
    import { NDivider } from 'naive-ui';
    import { fetchBatchDeleteElement, fetchGetElementList } from '@/service/api/business/element';
    import { useAppStore } from '@/store/modules/app';
    import { useAuth } from '@/hooks/business/auth';
    import { useDownload } from '@/hooks/business/download';
    import { useTable, useTableOperate } from '@/hooks/common/table';
    import { $t } from '@/locales';
    import ButtonIcon from '@/components/custom/button-icon.vue';
    import ElementOperateDrawer from './modules/element-operate-drawer.vue';
    import ElementSearch from './modules/element-search.vue';
    import { useDict } from '@/hooks/business/dict';
    import DictTag from '@/components/custom/dict-tag.vue';

    defineOptions({
        name: 'ElementList'
    });

    const appStore = useAppStore();
    const { download } = useDownload();
    const { hasAuth } = useAuth();

    useDict('ue_model_type');

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
        apiFn: fetchGetElementList,
        apiParams: {
            pageNum: 1,
            pageSize: 10,
            // if you want to use the searchParams in Form, you need to define the following properties, and the value is null
            // the value can not be undefined, otherwise the property in Form will not be reactive
                        id: null,
                        ueId: null,
                        elementType: null,
                        name: null,
                        description: null,
            params: {}
        },
        columns: () => [
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
                        key: 'ueId',
                        title: '模型标识',
                        align: 'center',
                        minWidth: 120,
                    },
                    {
                        key: 'elementType',
                        title: '模型类别',
                        align: 'center',
                        minWidth: 120,
                        render: row => {
                          return <DictTag value={row.elementType} dictCode="ue_model_type" />;
                        },
                    },
                    {
                        key: 'name',
                        title: '模型名称',
                        align: 'center',
                        minWidth: 120,
                    },
                    {
                        key: 'description',
                        title: '描述',
                        align: 'center',
                        minWidth: 120,
                    },
            {
                key: 'operate',
                title: $t('common.operate'),
                align: 'center',
                width: 130,
                render: row => {
                    const divider = () => {
                        if (!hasAuth('business:element:edit') || !hasAuth('business:element:remove')) {
                            return null;
                        }
                        return <NDivider vertical />;
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
                            {editBtn()}
                            {divider()}
                            {deleteBtn()}
                        </div>
                    );
                }
            }
        ]
    });

    const { drawerVisible, operateType, editingData, handleAdd, handleEdit, checkedRowKeys, onBatchDeleted, onDeleted } =
        useTableOperate(data, getData);

    async function handleBatchDelete() {
        // request
        const { error } = await fetchBatchDeleteElement(checkedRowKeys.value);
        if (error) return;
        onBatchDeleted();
    }

    async function handleDelete(id: CommonType.IdType) {
        // request
        const { error } = await fetchBatchDeleteElement([id]);
        if (error) return;
        onDeleted();
    }

    function edit(id: CommonType.IdType) {
        handleEdit('id', id);
    }

    function handleExport() {
        download('/business/element/export', searchParams, `资产管理_${new Date().getTime()}.xlsx`);
    }
</script>

<template>
    <div class="min-h-500px flex-col-stretch gap-16px overflow-hidden lt-sm:overflow-auto">
        <ElementSearch v-model:model="searchParams" @reset="resetSearchParams" @search="getDataByPage" />
        <NCard title="资产管理列表" :bordered="false" size="small" class="sm:flex-1-hidden card-wrapper">
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
                size="small"
                :flex-height="!appStore.isMobile"
                :scroll-x="962"
                :loading="loading"
                remote
                :row-key="row => row.id"
                :pagination="mobilePagination"
                class="sm:h-full"
            />
            <ElementOperateDrawer
                v-model:visible="drawerVisible"
                :operate-type="operateType"
                :row-data="editingData"
                @submitted="getDataByPage"
            />
        </NCard>
    </div>
</template>

<style scoped></style>
