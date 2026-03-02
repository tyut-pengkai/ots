<template>
    <div class="form-item">
        <NInput  v-model:value="questionName"
                 placeholder="请输入"
        />
        <n-button type="success" style="margin-left: 16px" @click="openDialog">
            选择
        </n-button>
        <n-modal v-model:show="showModal"
                 preset="card"
                 title="考题"
                 isplay-directive="show"
                 class="w-1000px"
        >
            <div class="search-wrap">
                <NForm ref="formRef" :model="searchParams" label-placement="left" inline>
                    <n-row gutter="12">
                        <n-col :span="6">
                            <NFormItem label="题目名称" path="questionName">
                                <NInput v-model:value="searchParams.questionName" placeholder="请输入"/>
                            </NFormItem>
                        </n-col>
                        <n-col :span="6">
                            <NFormItem label="题型" path="questionType">
                                <NSelect
                                    v-model:value="searchParams.questionType"
                                    :options="questionTypeOptions"
                                    clearable
                                    placeholder="请选择题型"
                                />
                            </NFormItem>
                        </n-col>
                        <n-col :span="12">
                            <NFormItem>
                                <NSpace>
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
                                    <NButton ghost type="primary" @click="handleAdd">
                                        <template #icon>
                                            <icon-material-symbols:add class="text-icon" />
                                        </template>
                                        {{ $t('common.add') }}
                                    </NButton>
                                </NSpace>
                            </NFormItem>
                        </n-col>
                    </n-row>
                </NForm>
            </div>
            <div class="table-wrap">
                <n-spin :show="isSpin">
                    <NDataTable
                        remote
                        v-model:checked-row-keys="checkedRowKeys"
                        :columns="columns"
                        :data="tableData"
                        :pagination="pagination"
                        :row-key="row => row.id"
                        flex-height
                        style="height: 500px"
                    />
                </n-spin>
            </div>
            <template #footer>
                <NSpace justify="end" :size="16">
                    <NButton @click="handleCancel">{{ $t('common.cancel') }}</NButton>
                    <NButton type="primary" @click="handleSubmit">{{ $t('common.confirm') }}</NButton>
                </NSpace>
            </template>
        </n-modal>
        <QuestionOperateDrawer
            v-model:visible="drawerVisible"
            :operate-type="'add'"
            :hsc-id="props.hscId"
            @submitted="getTableData"
        />
    </div>
</template>

<script setup lang="tsx">
import { ref,reactive,watch } from 'vue';
import {$t} from "@/locales";
import { fetchGetQuestionList, getQuestionInfo } from "@/service/api/business/question";
import {useDict} from "@/hooks/business/dict";
import DictTag from '@/components/custom/dict-tag.vue';
import QuestionOperateDrawer from "@/views/question/modules/question-operate-drawer.vue";

interface Props {
    hscId: any;
}
const props = defineProps<Props>();
const questionId:any = defineModel('questionId',{required: true});
const questionName:any = ref('');
const showModal = ref(false);

const {options: questionTypeOptions} = useDict('question_type');
const formRef:any = ref();
const createSearchParams = ()=>{
    return {
        questionName: null,
        questionType: null,
        hscId: props.hscId
    }
}
const searchParams:any = reactive(createSearchParams());
const tableData:any = ref([]);
const checkedRowKeys:any = ref([]);
const pagination:any = reactive({
    page: 1,
    pageSize: 10,
    itemCount: 0,
    pageSizes: [10, 15, 20, 25, 30],
    showSizePicker: true,
    prefix: ()=>$t('datatable.itemCount', { total: pagination.itemCount }),
    onChange:(page: number)=>{
        pagination.page = page
        getTableData();
    },
    onUpdatePageSize:(pageSize: number)=>{
        pagination.pageSize = pageSize
        pagination.page = 1
        getTableData();
    }
})
const columns:any = [
    {
        type: 'selection',
        multiple: false,
        width: 50
    },
    {
        key: 'questionName',
        title: '题目名称',
        align: 'center',
        Width: 200,
    },{
        key: 'questionType',
        title: '题型',
        align: 'center',
        Width: 200,
        render: (row:any) => {
            return <DictTag value={row.questionType} dictCode="question_type"/>;
        },
    },{
        key: 'questionDescription',
        title: '题干',
        align: 'center'
    },
]
const isSpin = ref(false);
const getTableData = async ()=>{
    isSpin.value = true;
    const response:any = await fetchGetQuestionList({pageNum:pagination.page,pageSize:pagination.pageSize,...searchParams});
    if(response.data.code == 200){
        tableData.value = response.data.rows;
        pagination.itemCount = response.data.total;
    }
    isSpin.value = false;
}
const search = ()=>{
    getTableData();
}
const reset = ()=>{
    Object.assign(searchParams, createSearchParams());
    getTableData();
}
const handleSubmit = ()=>{
    questionId.value = checkedRowKeys.value[0];
    showModal.value = false;
}
const handleCancel = ()=>{
    showModal.value = false;
}
const openDialog = ()=>{
    showModal.value = true;
    Object.assign(searchParams, createSearchParams());
    tableData.value = [];
    pagination.pageNum = 1;
    pagination.pageSize = 10;
    pagination.itemCount = 0;
        getTableData();
    checkedRowKeys.value = [questionId.value];
}
watch(questionId,(val)=>{
    questionName.value = '';
    if(val){
        getQuestionInfo(val).then((res:any)=>{
            questionName.value = res.data.questionName;
        })
    }
},{
    immediate:true
})

const drawerVisible = ref(false);
const handleAdd = ()=>{
    drawerVisible.value = true;
}
</script>

<style lang="scss" scoped>
    .form-item{
        width: 100%;
        display: flex;
    }
</style>
