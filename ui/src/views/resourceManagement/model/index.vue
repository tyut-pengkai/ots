<template>
    <div class="min-h-500px flex-col-stretch gap-16px overflow-hidden lt-sm:overflow-auto">
        <Search v-model:model="searchParams" @reset="resetSearchParams" @search="getDataByPage"/>
        <NCard :bordered="false" class="sm:flex-1-hidden card-wrapper" size="small" title="模型资产管理列表">
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
                >
                    <template #after>
                        <NButton v-if="hasAuth('business:element:import')" size="small" ghost @click="handleImport">
                            <template #icon>
                                <icon-material-symbols:upload-rounded class="text-icon" />
                            </template>
                            {{ $t('common.import') }}
                        </NButton>
                    </template>
                </TableHeaderOperation>
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
            <ImportModal v-model:visible="importVisible" @submitted="getData" />
        </NCard>
    </div>
</template>

<script lang="tsx" setup>
import {NButton, NDivider,NEllipsis } from 'naive-ui';
import {fetchBatchDeleteElement, fetchGetElementList} from '@/service/api/business/element';
import {useAppStore} from '@/store/modules/app';
import {useAuth} from '@/hooks/business/auth';
import {useDownload} from '@/hooks/business/download';
import {useTable, useTableOperate} from '@/hooks/common/table';
import {$t} from '@/locales';
import ButtonIcon from '@/components/custom/button-icon.vue';
import OperateDrawer from './components/operate-drawer.vue';
import Search from './components/search.vue';
import {useDict} from '@/hooks/business/dict';
import DictTag from '@/components/custom/dict-tag.vue';
import {useBoolean} from "~/packages/hooks";
import ImportModal from "./components/import-model.vue";

defineOptions({
    name: 'ElementList'
});

const appStore = useAppStore();
const {download} = useDownload();
const {hasAuth} = useAuth();

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
            key: 'name',
            title: '模型名称',
            align: 'center',
            minWidth: 120,
        },
        {
            key: 'elementType',
            title: '模型类别',
            align: 'center',
            minWidth: 120,
            render: row => {
                return <DictTag value={row.elementType} dictCode="ue_model_type"/>;
            },
        },
        {
            key: 'ueId',
            title: '模型标识',
            align: 'center',
            minWidth: 120,
        },
        {
            key: 'position',
            title: '坐标',
            align: 'center',
            minWidth: 120,
        },
        {
            key: 'description',
            title: '描述',
            align: 'center',
            minWidth: 120,
            render: row => {
        const tempDiv = document.createElement('div');
        tempDiv.innerHTML = row.description || '';
        const plainText = tempDiv.textContent || tempDiv.innerText || '';
        return (
            <NEllipsis
                style={{ maxWidth: '100%' }}
                tooltip={true}
                lineClamp={1}
            >
                {plainText}
            </NEllipsis>
        );
    }
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
                        {editBtn()}
                        {divider()}
                        {deleteBtn()}
                    </div>
                );
            }
        }
    ]
});

const {drawerVisible, operateType, editingData, handleAdd, handleEdit, checkedRowKeys, onBatchDeleted, onDeleted} =
    useTableOperate(data, getData);

async function handleBatchDelete() {
    // request
    const {error} = await fetchBatchDeleteElement(checkedRowKeys.value);
    if (error) return;
    onBatchDeleted();
}

async function handleDelete(id: CommonType.IdType) {
    // request
    const {error} = await fetchBatchDeleteElement([id]);
    if (error) return;
    onDeleted();
}

function edit(id: CommonType.IdType) {
    handleEdit('id', id);
}

function handleExport() {
    download('/business/element/export', searchParams, `资产管理_${new Date().getTime()}.xlsx`);
}

const { bool: importVisible, setTrue: openImportModal } = useBoolean();
const handleImport = ()=>{
    openImportModal();
}
</script>

<style scoped>

</style>
