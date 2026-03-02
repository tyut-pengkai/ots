<script setup lang="tsx">
    import { NDivider } from 'naive-ui';
    import { ref, onMounted } from 'vue';
    import { fetchBatchDeleteTrainingExam, fetchGetTrainingExamList, getInfo } from '@/service/api/business/training';
    import { useAppStore } from '@/store/modules/app';
    import { useAuth } from '@/hooks/business/auth';
    import { useDownload } from '@/hooks/business/download';
    import { useTable, useTableOperate } from '@/hooks/common/table';
    import { $t } from '@/locales';
    import ButtonIcon from '@/components/custom/button-icon.vue';
    import TrainingExamOperateDrawer from './modules/training-exam-operate-drawer.vue';
    import TrainingExamSearch from './modules/training-exam-search.vue';
    import { fetchGetDeptList } from '@/service/api/system/dept';
    import {jsonClone} from "~/packages/utils/src/klona";

    defineOptions({
        name: 'TrainingExamList'
    });

    const appStore = useAppStore();
    const { download } = useDownload();
    const { hasAuth } = useAuth();

    const deptList:any = ref([]);
    onMounted(async () => {
        const res = await fetchGetDeptList();
        deptList.value = res.response.data.data;
    })
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
        apiFn: fetchGetTrainingExamList,
        apiParams: {
            pageNum: 1,
            pageSize: 10,
            // if you want to use the searchParams in Form, you need to define the following properties, and the value is null
            // the value can not be undefined, otherwise the property in Form will not be reactive
            name: null,
            deptId: null,
            type: "exam",
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
                        title: '考核名称',
                        align: 'center',
                        minWidth: 120,
                    },
                    {
                        key: 'deptName',
                        title: '参与组织',
                        align: 'center',
                        minWidth: 120,
                    },
                    {
                        key: 'createBy',
                        title: '创建人',
                        align: 'center',
                        minWidth: 120,
                    },
                    {
                        key: 'updateTime',
                        title: '修改时间',
                        align: 'center',
                        minWidth: 120,
                    },
                    // {
                    //     key: 'batchTimes',
                    //     title: '允许参与次数',
                    //     align: 'center',
                    //     minWidth: 120,
                    // },
                    // {
                    //     key: 'passingScore',
                    //     title: '通过的分数线',
                    //     align: 'center',
                    //     minWidth: 120,
                    // },
            {
                key: 'operate',
                title: $t('common.operate'),
                align: 'center',
                width: 130,
                render: row => {
                    const divider = () => {
                        if (!hasAuth('business:trainingExam:edit') || !hasAuth('business:trainingExam:remove')) {
                            return null;
                        }
                        return <NDivider vertical />;
                    };

                    const editBtn = () => {
                        if (!hasAuth('business:trainingExam:edit')) {
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
                        if (!hasAuth('business:trainingExam:remove')) {
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

    const { drawerVisible, operateType, editingData, handleAdd, handleEdit, checkedRowKeys, onBatchDeleted, onDeleted, openDrawer } =
        useTableOperate(data, getData);

    async function handleBatchDelete() {
        // request
        const { error } = await fetchBatchDeleteTrainingExam(checkedRowKeys.value);
        if (error) return;
        onBatchDeleted();
    }

    async function handleDelete(id: CommonType.IdType) {
        // request
        const { error } = await fetchBatchDeleteTrainingExam([id]);
        if (error) return;
        onDeleted();
    }

    function edit(id: CommonType.IdType) {
        getInfo(id).then(res=>{
            operateType.value = 'edit';
            editingData.value = jsonClone(res.data);
            openDrawer();
        })
    }

    // function handleExport() {
    //     download('/business/trainingExam/export', searchParams, `培训配置_${new Date().getTime()}.xlsx`);
    // }
</script>

<template>
    <div class="min-h-500px flex-col-stretch gap-16px overflow-hidden lt-sm:overflow-auto">
        <TrainingExamSearch :dept-data="deptList" v-model:model="searchParams" @reset="resetSearchParams" @search="getDataByPage" />
        <NCard title="考核配置列表" :bordered="false" size="small" class="sm:flex-1-hidden card-wrapper">
            <template #header-extra>
                <TableHeaderOperation
                    v-model:columns="columnChecks"
                    :disabled-delete="checkedRowKeys.length === 0"
                    :loading="loading"
                    :show-add="hasAuth('business:trainingExam:add')"
                    :show-delete="hasAuth('business:trainingExam:remove')"
                    @add="handleAdd"
                    @delete="handleBatchDelete"
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
            <TrainingExamOperateDrawer
                v-model:visible="drawerVisible"
                :operate-type="operateType"
                :row-data="editingData"
                :dept-data="deptList"
                @submitted="getDataByPage"
            />
        </NCard>
    </div>
</template>

<style scoped></style>
