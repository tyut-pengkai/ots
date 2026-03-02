<script setup lang="ts">
import { computed, reactive, watch } from 'vue';
import { fetchCreateElement, fetchUpdateElement } from '@/service/api/business/element';
import { useFormRules, useNaiveForm } from '@/hooks/common/form';
import { useDict } from '@/hooks/business/dict';import { $t } from '@/locales';

defineOptions({
  name: 'ElementOperateDrawer'
});

interface Props {
  /** the type of operation */
  operateType: NaiveUI.TableOperateType;
  /** the edit row data */
  rowData?: Api.Business.Element | null;
}

const props = defineProps<Props>();

interface Emits {
  (e: 'submitted'): void;
}

const emit = defineEmits<Emits>();

const visible = defineModel<boolean>('visible', {
  default: false
});

const { options: ueModelTypeOptions } = useDict('ue_model_type');

const { formRef, validate, restoreValidation } = useNaiveForm();
const { createRequiredRule } = useFormRules();

const title = computed(() => {
  const titles: Record<NaiveUI.TableOperateType, string> = {
    add: '新增资产管理',
    edit: '编辑资产管理'
  };
  return titles[props.operateType];
});

type Model = Api.Business.ElementOperateParams;

const model: Model = reactive(createDefaultModel());

function createDefaultModel(): Model {
  return {
      ueId: '',
      elementType: '',
      name: '',
      description: '',
  };
}

type RuleKey = Extract<
  keyof Model,
  | 'id'
  | 'elementType'
>;

const rules: Record<RuleKey, App.Global.FormRule> = {
  id: createRequiredRule('id不能为空'),
  elementType: createRequiredRule('模型类别不能为空'),
};

function handleUpdateModelWhenEdit() {
  if (props.operateType === 'add') {
    Object.assign(model, createDefaultModel());
    return;
  }

  if (props.operateType === 'edit' && props.rowData) {
    Object.assign(model, props.rowData);
  }
}

function closeDrawer() {
  visible.value = false;
}

async function handleSubmit() {
  await validate();
  const formData = new FormData();
  formData.append('ueId', model.ueId ?? '');
  formData.append('elementType', model.elementType ?? '');
  formData.append('name', model.name ?? '');
  formData.append('description', model.description ?? '');
  // const { id, ueId, elementType, name, description } = model;

  // request
  if (props.operateType === 'add') {
    const { error } = await fetchCreateElement(formData);
    if (error) return;
  }

  if (props.operateType === 'edit') {
    formData.append('id', model.id ?? '');
    const { error } = await fetchUpdateElement(formData);
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
</script>

<template>
  <NDrawer v-model:show="visible" :title="title" display-directive="show" :width="800" class="max-w-90%">
    <NDrawerContent :title="title" :native-scrollbar="false" closable>
      <NForm ref="formRef" :model="model" :rules="rules">
        <NFormItem label="模型标识" path="ueId">
          <NInput v-model:value="model.ueId" placeholder="请输入模型标识" />
        </NFormItem>
        <NFormItem label="模型类别" path="elementType">
          <NSelect
            v-model:value="model.elementType"
            placeholder="请选择模型类别"
            :options="ueModelTypeOptions"
            clearable
          />
        </NFormItem>
        <NFormItem label="模型名称" path="name">
          <NInput v-model:value="model.name" placeholder="请输入模型名称" />
        </NFormItem>
        <NFormItem label="描述" path="description">
          <NInput v-model:value="model.description" placeholder="请输入描述" />
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
</template>

<style scoped></style>
