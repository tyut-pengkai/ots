<script lang="ts" setup>
import {ref} from 'vue';
import {useNaiveForm} from '@/hooks/common/form';
import {$t} from '@/locales';
import {useDict} from "@/hooks/business/dict";

defineOptions({
    name: 'QuestionSearch'
});

const {options: questionTypeOptions} = useDict('question_type');
interface Emits {
    (e: 'reset'): void;

    (e: 'search'): void;
}

const emit = defineEmits<Emits>();

const {formRef, validate, restoreValidation} = useNaiveForm();


const model = defineModel<Api.Business.QuestionSearchParams>('model', {required: true});


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
                        <NFormItemGi class="pr-24px" label="题目名称" path="questionName" span="24 s:12 m:6">
                            <NInput v-model:value="model.questionName" placeholder="请输入题目名称"/>
                        </NFormItemGi>
                        <NFormItemGi class="pr-24px" label="题型" path="questionType" span="24 s:12 m:6">
                            <NSelect
                                v-model:value="model.questionType"
                                :options="questionTypeOptions"
                                clearable
                                placeholder="请选择题型"
                            />
                        </NFormItemGi>
                        <NFormItemGi class="pr-24px" label="题干" path="questionDescription" span="24 s:12 m:6">
                            <NInput v-model:value="model.questionDescription" placeholder="请输入题干"/>
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
