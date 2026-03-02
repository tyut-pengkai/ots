<script lang="ts" setup>
import {ref} from 'vue';
import {useNaiveForm} from '@/hooks/common/form';
import {$t} from '@/locales';
import {useDict} from '@/hooks/business/dict';

defineOptions({
    name: 'ElementSearch'
});

interface Emits {
    (e: 'reset'): void;

    (e: 'search'): void;
}

const emit = defineEmits<Emits>();

const {formRef, validate, restoreValidation} = useNaiveForm();


const model = defineModel<Api.Business.ElementSearchParams>('model', {required: true});

const {options: ueModelTypeOptions} = useDict('ue_model_type');

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
    <NCard :bordered="false" class="card-wrapper" size="small">
        <NCollapse>
            <NCollapseItem :title="$t('common.search')" name="user-search">
                <NForm ref="formRef" :label-width="80" :model="model" label-placement="left">
                    <NGrid item-responsive responsive="screen">
                        <NFormItemGi class="pr-24px" label="模型标识" path="ueId" span="24 s:12 m:6">
                            <NInput v-model:value="model.ueId" placeholder="请输入模型标识"/>
                        </NFormItemGi>
                        <NFormItemGi class="pr-24px" label="模型类别" path="elementType" span="24 s:12 m:6">
                            <NSelect
                                v-model:value="model.elementType"
                                :options="ueModelTypeOptions"
                                clearable
                                placeholder="请选择模型类别"
                            />
                        </NFormItemGi>
                        <NFormItemGi class="pr-24px" label="模型名称" path="name" span="24 s:12 m:6">
                            <NInput v-model:value="model.name" placeholder="请输入模型名称"/>
                        </NFormItemGi>
                        <NFormItemGi class="pr-24px" label="描述" path="description" span="24 s:12 m:6">
                            <NInput v-model:value="model.description" placeholder="请输入描述"/>
                        </NFormItemGi>
                        <NFormItemGi class="pr-24px" span="24">
                            <NSpace class="w-full" justify="end">
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
                        </NFormItemGi>
                    </NGrid>
                </NForm>
            </NCollapseItem>
        </NCollapse>
    </NCard>
</template>

<style scoped></style>
