<script setup lang="ts">
import { computed, watch, h } from 'vue';
import { useTable } from '@/hooks/common/table';
import { getTrainingStepInfo } from '@/service/api/business/training-history';
defineOptions({
  name: 'TrainingExamHistoryDialog'
});
interface Props {
  /** the edit row data */
  rowData:  any;
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
  return "详情";
});

const {
    columns,
    data,
    getData,
    loading,
    mobilePagination,
    updateSearchParams,
} = useTable({
  apiFn: getTrainingStepInfo,
  apiParams: {
    pageNum: 1,
    pageSize: 10,
    batchId: '',
    hscId: ''
},
  columns: () => [
    { title: '步骤名称', key: 'groupName', width: 210 },
    { title: '操作内容', key: 'optContent', width: 240 },
    { title: '操作时间', key: 'createTime',width: 120},
    { title: '得分', key: 'score',width: 60},
    { title: '耗时(秒)', key: 'costTime',width: 80}
  ],
  immediate: false
})
watch(visible, async () => {
  if (visible.value) {
    updateSearchParams({
      batchId: props.rowData.batchId,
      hscId: props.rowData.hscId
    });
    getData();
  }
});
</script>

<template>
  <NModal 
    preset="dialog"
    negative-text=""
    style="width: 800px;"
    v-model:show="visible" 
    :title="title" 
    :width="800" 
    class="max-w-90%">
    <span style="margin-right: 20px;">姓名:{{props.rowData.userName}}</span>
    <span style="margin-right: 20px;">部门:{{props.rowData.deptName}}</span>
    <span style="margin-right: 20px;">操作卡:{{props.rowData.cardName}} </span>
    <NDataTable
          size="large"
          :max-height= "600"
          :min-height="600"
          :pagination="mobilePagination"
          :scroll-x="600"
          :loading="loading"
          :columns="columns"
          :data="data"
          remote
      />
  </NModal>
</template>
<style scoped></style>