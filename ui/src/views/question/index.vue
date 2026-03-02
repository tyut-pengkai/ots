<script setup lang="tsx">
    import { NDivider } from 'naive-ui';
    import { fetchBatchDeleteQuestion, fetchGetQuestionList, getQuestionInfo } from '@/service/api/business/question';
    import { useAppStore } from '@/store/modules/app';
    import { useAuth } from '@/hooks/business/auth';
    import { useDownload } from '@/hooks/business/download';
    import { useTable, useTableOperate } from '@/hooks/common/table';
    import { $t } from '@/locales';
    import ButtonIcon from '@/components/custom/button-icon.vue';
    import QuestionOperateDrawer from './modules/question-operate-drawer.vue';
    import QuestionSearch from './modules/question-search.vue';
    import {useDict} from "@/hooks/business/dict";
    import DictTag from '@/components/custom/dict-tag.vue';
    import {jsonClone} from "~/packages/utils/src/klona";
    import { useRouter } from 'vue-router'

    defineOptions({
        name: 'QuestionList'
    });
    useDict('question_type');
    const appStore = useAppStore();
    const { download } = useDownload();
    const { hasAuth } = useAuth();
    const router = useRouter();

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
        immediate: undefined,
        apiFn: fetchGetQuestionList,
        apiParams: {
            pageNum: 1,
            pageSize: 10,
            // if you want to use the searchParams in Form, you need to define the following properties, and the value is null
            // the value can not be undefined, otherwise the property in Form will not be reactive
            hscId: null,
            questionName: null,
            questionDescription: null,
            questionType: null,
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
                key: 'questionName',
                title: '题目名称',
                align: 'center',
                minWidth: 120,
            },
            {
                key: 'hscName',
                title: '操作卡',
                align: 'center',
                minWidth: 120,
            },
            {
                key: 'questionType',
                title: '题型',
                align: 'center',
                minWidth: 120,
                render: row => {
                    return <DictTag value={row.questionType} dictCode="question_type"/>;
                },
            },
            {
                key: 'questionDescription',
                title: '题干',
                align: 'center',
                minWidth: 120,
                render: row => {
        const tempDiv = document.createElement('div');
        tempDiv.innerHTML = row.questionDescription || '';
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
                        if (!hasAuth('business:question:edit') || !hasAuth('business:question:remove')) {
                            return null;
                        }
                        return <NDivider vertical />;
                    };

                    const editBtn = () => {
                        if (!hasAuth('business:question:edit')) {
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
                        if (!hasAuth('business:question:remove')) {
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

                    const viewBtn = () => {
                        return (
                            <ButtonIcon
                                type="primary"
                                text
                                icon="material-symbols:visibility-outline"
                                tooltipContent="预览"
                                onClick={() => view(row)}
                            />
                        );
                    };
                    return (
                        <div class="flex-center gap-8px">
                            {viewBtn()}
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

    const { drawerVisible, operateType, editingData, handleAdd, handleEdit, checkedRowKeys, onBatchDeleted, onDeleted, openDrawer } =
        useTableOperate(data, getData);

    async function handleBatchDelete() {
        // request
        const { error } = await fetchBatchDeleteQuestion(checkedRowKeys.value);
        if (error) return;
        onBatchDeleted();
    }

    async function handleDelete(id: CommonType.IdType) {
        // request
        const { error } = await fetchBatchDeleteQuestion([id]);
        if (error) return;
        onDeleted();
    }

    function edit(id: CommonType.IdType) {
        getQuestionInfo(id).then(res=>{
            operateType.value = 'edit';
            editingData.value = jsonClone(res.data);
            openDrawer();
        })
    }

    function handleExport() {
        download('/business/question/export', searchParams, `问题_${new Date().getTime()}.xlsx`);
    }

    const view = (row:any)=>{
        const route = router.resolve({
            path: '/viewTopic',
            query:{
                id: row.id,
                isView: '1'
            }
        })
        const fullUrl = window.location.origin + route.href;
        window.open(fullUrl, '_blank');
    }
</script>

<template>
    <div class="min-h-500px flex-col-stretch gap-16px overflow-hidden lt-sm:overflow-auto">
        <QuestionSearch v-model:model="searchParams" @reset="resetSearchParams" @search="getDataByPage" />
        <NCard title="考题配置列表" :bordered="false" size="small" class="sm:flex-1-hidden card-wrapper">
            <template #header-extra>
                <TableHeaderOperation
                    v-model:columns="columnChecks"
                    :disabled-delete="checkedRowKeys.length === 0"
                    :loading="loading"
                    :show-add="hasAuth('business:question:add')"
                    :show-delete="hasAuth('business:question:remove')"
                    :show-export="hasAuth('business:question:export')"
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
            <QuestionOperateDrawer
                v-model:visible="drawerVisible"
                :operate-type="operateType"
                :row-data="editingData"
                @submitted="getDataByPage"
            />
        </NCard>
    </div>
</template>

<style scoped></style>
