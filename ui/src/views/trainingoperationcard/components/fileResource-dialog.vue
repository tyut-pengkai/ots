<template>
    <div class="form-item">
        <NInput  v-model:value="iptVal"
                 placeholder="请输入"
        />
        <n-button type="success" style="margin-left: 16px" @click="openDialog">
            选择
        </n-button>
        <n-modal v-model:show="showModal"
                 preset="card"
                 :title="props.description"
                 isplay-directive="show"
                 class="w-1000px"
        >
            <div class="search-wrap">
                <NForm ref="formRef" :model="searchParams" label-placement="left" inline>
                    <n-row gutter="12">
                        <n-col :span="6">
                            <NFormItem label="文件原名" path="name">
                                <NInput v-model:value="searchParams.name" placeholder="请输入"/>
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
        <OperateDrawer
            v-model:visible="drawerVisible"
            :operate-type="'add'"
            :assetType="searchParams.assetType"
            @submitted="getTableData"
        />
    </div>
</template>

<script setup lang="tsx">
import { ref,reactive,watch } from 'vue';
import {$t} from "@/locales";
import {getFileList,getFileInfo} from "@/service/api/resourceManagement/file";
import ButtonIcon from "@/components/custom/button-icon.vue";
import OperateDrawer from "@/views/resourceManagement/file/components/operate-drawer.vue";

const fileResourceId:any = defineModel('fileResourceId',{required: true});
const props = defineProps(["description"]);
const iptVal:any = ref('');
const showModal = ref(false);
const formRef:any = ref();
const createSearchParams = ()=>{
    return {
        assetType: '',
        name: ''
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
    },{
        key: 'name',
        title: '原名',
        align: 'center'
    },{
        key: 'path',
        title: 'URL地址',
        align: 'center'
    }, {
        key: 'operate',
        title: $t('common.operate'),
        align: 'center',
        width: 130,
        render: (row:any) => {
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
                </div>
            );
        }
    }
]
const getTableData = async ()=>{
    isSpin.value = true;
    const response:any = await getFileList({pageNum: pagination.page,pageSize: pagination.pageSize,...searchParams});
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
const openDialog = ()=>{
    showModal.value = true;
    Object.assign(searchParams, createSearchParams());
    tableData.value = [];
    pagination.itemCount = 0;
    pagination.page = 1;
    pagination.pageSize = 10;
    if(props.description === '选择文档'){
        searchParams.assetType = 'document';
    }else if(props.description === '选择视频'){
        searchParams.assetType = 'video';
    }else if(props.description === '选择图片'){
        searchParams.assetType = 'image';
    }
    getTableData();
    checkedRowKeys.value = [fileResourceId.value];
}
const view = (row:any)=>{
    const encoder = new TextEncoder();
    const data = encoder.encode(row.path);
    // 将 Uint8Array 转换为二进制字符串
    let binary = '';
    for (let i = 0; i < data.length; i++) {
        binary += String.fromCharCode(data[i]);
    }
    // 使用 btoa 进行 Base64 编码
    const base64Encoded = btoa(binary);
    const finalEncoded = encodeURIComponent(base64Encoded);
    window.open('http://127.0.0.1:8012/onlinePreview?url='+finalEncoded);
}
const handleSubmit = ()=>{
    fileResourceId.value = checkedRowKeys.value[0];
    showModal.value = false;
}
const handleCancel = ()=>{
    showModal.value = false;
}
watch(fileResourceId,(val)=>{
    iptVal.value = '';
    if(val){
        getFileInfo(val).then((res:any)=>{
            iptVal.value = res.data.name;
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
