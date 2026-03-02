<script setup lang="tsx">
    import { ref, onMounted, h } from 'vue';
    import { getTrainingHistoryList, getPeopleList, getPeopleHistory, getTrainingStepInfo } from '@/service/api/business/training-history';
    import { useAppStore } from '@/store/modules/app';
    import { useTable, useTableOperate } from '@/hooks/common/table';
    import { $t } from '@/locales';
    import ButtonIcon from '@/components/custom/button-icon.vue';
    import TrainingHistorySearch from './modules/training-history-search.vue';
    import TrainingHistoryDrawer from './modules/training-history-drawer.vue';
    import { fetchGetDeptList } from '@/service/api/system/dept';
    defineOptions({
        name: 'TrainingHistory'
    });
    const historyData = ref<Api.Business.TrainingHistory>();
    const appStore = useAppStore();
    const deptList:any = ref([]);
    const drawerVisible = ref<boolean>(false);
    onMounted(async () => {
        const res = await fetchGetDeptList();
        deptList.value = res.response.data.data;
    })
    const {
        columns,
        columnChecks,
        data,
        getData,
        loading,
        mobilePagination,
        searchParams,
        resetSearchParams,
        getDataByPage
    } = useTable({
        apiFn: getTrainingHistoryList,
        apiParams: {
            pageNum: 1,
            pageSize: 10,
            name: null,
            deptId: null,
            type: 'train',
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
                title: '培训名称',
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
                key: 'optCardNum',
                title: '操作卡数量',
                align: 'center',
                minWidth: 120
            },
            {
                key: 'peopleAcc',
                title: '应参与人数',
                align: 'center',
                minWidth: 120
            },
            {
                key: 'people',
                title: '实参与人数',
                align: 'center',
                minWidth: 120
            },
            {
                key: 'num',
                title: '培训总次数',
                align: 'center',
                minWidth: 120
            },
            {
                key: 'completRate',
                title: '完成率',
                align: 'center',
                minWidth: 120,
                render: row => {
                    const actual  = Number(row.people   ?? 0);
                    const should  = Number(row.peopleAcc ?? 0);
                    const rate    = should ? (actual / should * 100).toFixed(2) : '0.00';
                    return `${rate}%`;
                }
            },
            
            {
                key: 'operate',
                title: '操作',
                align: 'center',
                minWidth: 120,
                render: row => {
                    const detailBtn = () => {
                        return (
                            <ButtonIcon
                                text
                                type="primary"
                                icon="material-symbols:visibility-outline"
                                tooltipContent={'详情'}
                                onClick={() => openDrawer(row)}
                            />
                        );
                    };
                    return (
                        <div class="flex-center gap-8px">
                            {detailBtn()}
                        </div>
                    );
                }
            }
        ]
    });

    async function openDrawer(row: Api.Business.TrainingHistory) {
        historyData.value= row;
        drawerVisible.value = true;
    }
</script>

<template>
    <div class="min-h-500px flex-col-stretch gap-16px overflow-hidden lt-sm:overflow-auto">
        <TrainingHistorySearch :dept-data="deptList" v-model:model="searchParams" @reset="resetSearchParams" @search="getDataByPage" />
        <NCard title="培训配置列表" :bordered="false" size="small" class="sm:flex-1-hidden card-wrapper">
            <template #header-extra>
                <TableHeaderOperation
                    v-model:columns="columnChecks"
                    :loading="loading"
                    :showAdd="false"
                    :showDelete="false"
                    @refresh="getData"
                />
            </template>
            <NDataTable
                :columns="columns"
                :data="data"
                size="small"
                :flex-height="!appStore.isMobile"
                :scroll-x="962"
                :loading="loading"
                remote
                :row-key="row => row.hteId"
                :pagination="mobilePagination"
                class="sm:h-full"
            />
            <TrainingHistoryDrawer
                v-model:visible="drawerVisible"
                :row-data="historyData!"
            />
        </NCard>
    </div>
</template>
<style scoped></style>
