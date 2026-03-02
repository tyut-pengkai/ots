<script setup lang="ts">
import { ref } from 'vue';
import { useNaiveForm } from '@/hooks/common/form';
import { $t } from '@/locales';
import { useDict } from '@/hooks/business/dict';
defineOptions({
  name: 'ElementSearch'
});

interface Emits {
  (e: 'reset'): void;
  (e: 'search'): void;
}

const emit = defineEmits<Emits>();

const { formRef, validate, restoreValidation } = useNaiveForm();


const model = defineModel<Api.Business.ElementSearchParams>('model', { required: true });

const { options: ueModelTypeOptions } = useDict('ue_model_type');

async function reset() {
  Object.assign(model.value.params!, {});
  await restoreValidation();
  emit('reset');
}

async function search() {
  await validate();
  emit('search');
}
</script>

<template>
  <NCard :bordered="false" size="small" class="card-wrapper">
    <NCollapse>
      <NCollapseItem :title="$t('common.search')" name="user-search">
        <NForm ref="formRef" :model="model" label-placement="left" :label-width="80">
          <NGrid responsive="screen" item-responsive>
            <NFormItemGi span="24 s:12 m:6" label="$column.columnComment" path="id" class="pr-24px">
              <NInput v-model:value="model.id" placeholder="请输入$column.columnComment" />
            </NFormItemGi>
            <NFormItemGi span="24 s:12 m:6" label="模型标识" path="ueId" class="pr-24px">
              <NInput v-model:value="model.ueId" placeholder="请输入模型标识" />
            </NFormItemGi>
            <NFormItemGi span="24 s:12 m:6" label="模型类别" path="elementType" class="pr-24px">
              <NSelect
                v-model:value="model.elementType"
                placeholder="请选择模型类别"
                :options="ueModelTypeOptions"
                clearable
              />
            </NFormItemGi>
            <NFormItemGi span="24 s:12 m:6" label="模型名称" path="name" class="pr-24px">
              <NInput v-model:value="model.name" placeholder="请输入模型名称" />
            </NFormItemGi>
            <NFormItemGi span="24 s:12 m:6" label="描述" path="description" class="pr-24px">
              <NInput v-model:value="model.description" placeholder="请输入描述" />
            </NFormItemGi>
            <NFormItemGi span="24" class="pr-24px">
              <NSpace class="w-full" justify="end">
                <NButton @click="reset">
                  <template #icon>
                    <icon-ic-round-refresh class="text-icon" />
                  </template>
                  {{ $t('common.reset') }}
                </NButton>
                <NButton type="primary" ghost @click="search">
                  <template #icon>
                    <icon-ic-round-search class="text-icon" />
                  </template>
                  {{ $t('common.search') }}
                </NButton>
              </NSpace>
            </NFormItemGi>
          </NGrid>
        </NForm>
      </NCollapseItem>
    </NCollapse>
  </NCard>
</template>

<style scoped></style>
