<script setup lang="ts">
import { computed, reactive, watch, ref, h } from 'vue';
import { DataTableInst, DataTableColumns, NButton } from 'naive-ui';
import { fetchCreateTrainingExam, fetchUpdateTrainingExam, getSopCardList, getInfo } from '@/service/api/business/training';
import { useFormRules, useNaiveForm } from '@/hooks/common/form';
import { $t } from '@/locales';
import ButtonIcon from '@/components/custom/button-icon.vue';

defineOptions({
  name: 'TrainingExamOperateDrawer'
});
interface Props {
  /** the type of operation */
  operateType: NaiveUI.TableOperateType;
  /** the edit row data */
  rowData?: Api.Business.TrainingExam | null;
  deptData: [];
}

const props = defineProps<Props>();

interface Emits {
  (e: 'submitted'): void;
}

const emit = defineEmits<Emits>();

const visible = defineModel<boolean>('visible', {
  default: false
});

const { formRef, validate, restoreValidation } = useNaiveForm();
const { createRequiredRule } = useFormRules();

const title = computed(() => {
  const titles: Record<NaiveUI.TableOperateType, string> = {
    add: '新增培训配置',
    edit: '编辑培训配置'
  };
  return titles[props.operateType];
});

type Model = Api.Business.TrainingExamOperateParams & {
  details?: TrainingExamDetail[];   
};

const model: Model = reactive(createDefaultModel());

function createDefaultModel(): Model {
  return {
    id: null,
    name: '',
    deptId: null,
    batchTimes: null,
    passingScore: null,
    type: 'train',
    createBy: null,
    deptName: null,
    updateTime: null,
    details: []
  };
}

interface TrainingExamDetail {
  id: number;
  hscId: number;
  name: string;
}

type RuleKey = Extract<
  keyof Model,
  'name' | 'deptId' | 'batchTimes' | 'passingScore' | 'type'
>;

const rules: Record<RuleKey, App.Global.FormRule> = {
  name: createRequiredRule('培训名称不能为空'),
  deptId: createRequiredRule('参与组织不能为空'),
  batchTimes: createRequiredRule('允许参与次数不能为空'),
  passingScore: createRequiredRule('通过的分数线不能为空'),
  type: createRequiredRule('类型不能为空')
};

function handleUpdateModelWhenEdit() {
  if (props.operateType === 'add') {
    Object.assign(model, createDefaultModel());
    return;
  }

  if (props.operateType === 'edit' && props.rowData) {
    Object.assign(model, { ...props.rowData, details: props.rowData.details || [] });
  }
}

function closeDrawer() {
  visible.value = false;
}

async function handleSubmit() {
  await validate();

  const { id, name, deptId, batchTimes, passingScore, type, details } = model;

  // 构建提交数据
  const submitData = {
    id,
    name,
    deptId,
    batchTimes,
    passingScore,
    type,
    details: details|| [] 
  };

  // request
  if (props.operateType === 'add') {
    const { error } = await fetchCreateTrainingExam(submitData);
    if (error) return;
  }

  if (props.operateType === 'edit') {
    const { error } = await fetchUpdateTrainingExam(submitData);
    if (error) return;
  }

  window.$message?.success($t('common.updateSuccess'));
  closeDrawer();
  emit('submitted');
}

watch(visible, () => {
  if (visible.value) {
    handleUpdateModelWhenEdit();
    restoreValidation();
  }
});

const pickModal = ref(false);
const pickTableRef = ref<DataTableInst | null>(null);
const pickData = ref([] as TrainingExamDetail[]); // 弹框里可被选中的原始数据
const checkedRowKeys = ref<number[]>([]);

async function openPickModal() {
  const res = await getSopCardList({sopCardModel: 'train'});
  if (res.response.data.code == "200") {
    pickData.value = res.response.data.data;
  }
  checkedRowKeys.value = [...alreadyIds.value];
  pickModal.value = true;
}

const alreadyIds = computed(() => model.details?.map(i => i.hscId) || []);

function handlePickSubmit() {
  const selectedRows = pickData.value.filter(item => 
    checkedRowKeys.value.includes(item.id)
  );
  model.details = selectedRows.map(({ id, name }) => ({ hscId: id, name }));
  pickModal.value = false;
  checkedRowKeys.value = [];
}

const detailColumns: DataTableColumns<TrainingExamDetail> = [
  { title: '操作卡名称', key: 'name' },
  {
      key: 'operate',
      title: '操作',
      align: 'center',
      minWidth: 120,
      render(row: any) {
          return h (
              ButtonIcon,
              { icon: 'material-symbols:delete-outline',tooltipContent: $t('common.delete'), text: true,  
              type: 'primary', onClick: () => removeDetail(row) },
              
          )
      }
  }
  
];

function removeDetail(row: TrainingExamDetail) {
  const idx = model.details!.findIndex(i => i.hscId === row.hscId);
  if (idx !== -1) {
    model.details!.splice(idx, 1);
  }
}

const pickColumns: DataTableColumns<TrainingExamDetail> = [
  { 
    type: 'selection',
    disabled: (row: TrainingExamDetail) => alreadyIds.value.includes(row.id)
  },
  { title: '操作卡名称', key: 'name' }
];

const handleCheckedRowKeysChange = (keys: number[]) => {
  checkedRowKeys.value = keys;
};

</script>

<template>
  <NDrawer v-model:show="visible" :title="title" display-directive="show" :width="800" class="max-w-90%">
    <NDrawerContent :title="title" :native-scrollbar="false" closable>
      <NForm ref="formRef" :model="model" :rules="rules">
        <NFormItem label="培训名称" path="name">
          <NInput v-model:value="model.name" placeholder="请输入培训名称" />
        </NFormItem>
        <NFormItem label="参与组织" path="deptId">
          <NSelect
              v-model:value="model.deptId"
              :options="deptData"
              label-field="deptName"
              value-field="deptId"
              placeholder="请选择"
          />
        </NFormItem>
        <!-- <NFormItem label="允许参与次数" path="batchTimes">
          <NInput v-model:value="model.batchTimes" placeholder="请输入允许参与次数" />
        </NFormItem>
        <NFormItem label="通过的分数线" path="passingScore">
          <NInput v-model:value="model.passingScore" placeholder="请输入通过的分数线" />
        </NFormItem> -->
        <NFormItem label="操作卡列表" path="details">
          <NSpace vertical :size="12" class="w-full">
            <NButton size="small" @click="openPickModal">+ 添加操作卡</NButton>
            <NDataTable
              size="small"
              :columns="detailColumns"
              :data="model.details"
              :pagination="false"
              :scroll-x="600"
              max-height="260px"
              :row-key="(row: TrainingExamDetail) => row.id"
            />
          </NSpace>
        </NFormItem>
      </NForm>
      <template #footer>
        <NSpace :size="16">
          <NButton @click="closeDrawer">{{ $t('common.cancel') }}</NButton>
          <NButton type="primary" @click="handleSubmit">{{ $t('common.confirm') }}</NButton>
        </NSpace>
      </template>
    </NDrawerContent>
  </NDrawer>

  <NModal v-model:show="pickModal" preset="card" title="选择操作卡" style="width: 720px">
    <NDataTable
      ref="pickTableRef"
      :row-key="(row: TrainingExamDetail) => row.id"
      :columns="pickColumns"
      :data="pickData"
      :checked-row-keys="checkedRowKeys"
      @update:checked-row-keys="handleCheckedRowKeysChange"
      :pagination="{ pageSize: 8 }"
    />
    <template #footer>
      <NSpace justify="end">
        <NButton @click="pickModal = false">取消</NButton>
        <NButton type="primary" @click="handlePickSubmit">确定</NButton>
      </NSpace>
    </template>
  </NModal>
</template>

<style scoped></style>