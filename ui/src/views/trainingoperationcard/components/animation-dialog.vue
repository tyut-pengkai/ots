<template>
    <div class="form-item">
        <NInput  v-model:value="iptVal"
                 placeholder="请输入"
        />
        <n-button type="success" style="margin-left: 16px" @click="openDialog">
            请选择动画资源
        </n-button>
        <n-modal v-model:show="showModal"
                 preset="card"
                 title="动画资源"
                 isplay-directive="show"
                 class="w-1000px"
        >
            <div class="search-wrap">
                <NForm ref="formRef" :model="searchParams" label-placement="left" inline>
                    <n-row gutter="12">
                        <n-col :span="6">
                            <NFormItem label="模型标识" path="searchParams.ueId">
                                <NInput v-model:value="searchParams.ueId" placeholder="请输入"/>
                            </NFormItem>
                        </n-col>
                        <n-col :span="6">
                            <NFormItem label="模型名称" path="searchParams.name">
                                <NInput v-model:value="searchParams.name" placeholder="请输入"/>
                            </NFormItem>
                        </n-col>
                        <n-col :span="6">
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
    </div>
</template>

<script setup lang="ts">
import {reactive, ref, watch} from "vue";
import {fetchGetElementList, getElementInfo} from "@/service/api/business/element";
import {$t} from "@/locales";

const animationId:any = defineModel('animationId',{required: true});
const iptVal:any = ref('');
const showModal = ref(false);

const formRef = ref();
const createSearchParams = ()=>{
    return{
        elementType: 'animation',
        ueId: null,
        name: null
    }
}
const searchParams:any = reactive(createSearchParams());
const isSpin = ref(false);
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
        pagination.page = pageSize
        pagination.pageNum = 1
        getTableData();
    }
})
const columns:any = [
    {
        type: 'selection',
        multiple: false,
        width: 50
    },{
        key: 'name',
        title: '模型名称',
        align: 'center',
        minWidth: 120,
    },{
        key: 'ueId',
        title: '模型标识',
        align: 'center',
        minWidth: 120,
    }, {
        key: 'description',
        title: '描述',
        align: 'center',
        minWidth: 120,
    }
]
const getTableData = async ()=>{
    isSpin.value = true;
    const response:any = await fetchGetElementList({pageNum: pagination.page,pageSize: pagination.pageSize,...searchParams});
    if(response.data.code == 200){
        tableData.value = response.data.rows;
        pagination.itemCount = response.data.total;
    }
    isSpin.value = false;
}
const reset = ()=>{
    Object.assign(searchParams, createSearchParams());
    getTableData();
}
const search = ()=>{
    getTableData();
}
const handleSubmit = ()=>{
    animationId.value = checkedRowKeys.value[0];
    showModal.value = false;
}
const handleCancel = ()=>{
    showModal.value = false;
}
const openDialog = ()=>{
    Object.assign(searchParams, createSearchParams());
    tableData.value = [];
    pagination.itemCount = 0;
    pagination.pageNum = 1;
    pagination.pageSize = 10;
    getTableData();
    checkedRowKeys.value = [animationId.value];
    showModal.value = true;
}
watch(animationId,(val)=>{
    iptVal.value = '';
    if(val){
        getElementInfo(val).then((res:any)=>{
            iptVal.value = res.data.name;
        })
    }
},{
    immediate:true
})
</script>

<style scoped>
.form-item{
    width: 100%;
    display: flex;
}
</style>
