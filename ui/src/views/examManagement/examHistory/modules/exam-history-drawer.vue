<script setup lang="ts">
import { ref,computed, watch, h } from 'vue';
import { useTable } from '@/hooks/common/table';
import { getPeopleList } from '@/service/api/business/training-history';
import ExamHistoryDialog from './exam-history-dialog.vue';
import ButtonIcon from '@/components/custom/button-icon.vue';
defineOptions({
  name: 'TrainingExamOperateDrawer'
});
interface Props {
  /** the edit row data */
  rowData:  Api.Business.TrainingHistory;
}
const props = defineProps<Props>();
interface Emits {
  (e: 'submitted'): void;
}
const emit = defineEmits<Emits>();
const visible = defineModel<boolean>('visible', {
  default: false
});
const title = computed(() => {
  return "考核任务明细";
});
const {
    columns,
    data,
    getData,
    loading,
    mobilePagination,
    updateSearchParams,
} = useTable({
  apiFn: getPeopleList,
  apiParams: {
    pageNum: 1,
    pageSize: 10,
    hteId: ''
},
  columns: () => [
    { title: '姓名', key: 'createName', width: 120 },
    { title: '操作卡', key: 'name', width: 240 },
    { title: '考核时间', key: 'createTime', width: 160 },
    {
        key: 'operate',
        title: '操作',
        align: 'center',
        width: 80,
        render: (row)=>{
            return h(ButtonIcon, { icon: 'material-symbols:visibility-outline',tooltipContent: '详情', text: true,
            type: 'primary', onClick: () => openDialog(row) })
        }
    }
  ],
  immediate: false
})
const drawerVisible = ref<boolean>(false);
const batchData = ref<any>({
  batchId: '',
  hscId: '',
  deptName:'',
  userName: '',
  cardName: ''
});
function openDialog(row: Api.Business.TrainingHistory) {
  batchData.value.batchId = row.batchId;
  batchData.value.hscId = row.hscId;
  batchData.value.deptName = props.rowData.deptName;
  batchData.value.userName = row.createName;
  batchData.value.cardName = row.name;
  drawerVisible.value = true;
}
watch(visible, async () => {
  if (visible.value) {
    console.log(props.rowData)
    updateSearchParams({
      hteId: props.rowData.hteId
    });
    getData();
  }
});
</script>

<template>
  <NDrawer v-model:show="visible" :title="title" display-directive="show" :width="800" class="max-w-90%">
    <NDrawerContent :title="title" :native-scrollbar="false" closable>
      <NDataTable
            size="large"
            :max-height= "300"
            :min-height="300"
            :pagination="mobilePagination"
            :scroll-x="300"
            :loading="loading"
            :columns="columns"
            :data="data"
            remote
        />
      <template #footer>
      </template>
    </NDrawerContent>
  </NDrawer>
  <ExamHistoryDialog
                v-model:visible="drawerVisible"
                :row-data="batchData!"
            />
</template>
<style scoped></style>
