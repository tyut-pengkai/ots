<template>
    <NDrawer v-model:show="visible" :title="title" :width="800" class="max-w-90%" display-directive="show">
        <NDrawerContent :native-scrollbar="false" :title="title" closable>
            <NForm ref="formRef" :model="model" :rules="rules">
                <NFormItem label="操作卡名称" path="name">
                    <NInput v-model:value="model.name" placeholder="请输入"/>
                </NFormItem>
                <NFormItem label="初始化坐标" path="initialZB">
                    <NInput v-model:value="model.initialZB" placeholder="请输入"/>
                </NFormItem>
                <!-- <NFormItem label="关卡地图" path="cardMap">
                    <NInput v-model:value="model.cardMap" placeholder="请输入"/>
                </NFormItem> -->
                <!-- <NFormItem label="操作卡类型" path="sopCardType">
                    <n-radio-group v-model:value="model.sopCardType">
                        <n-space>
                            <n-radio :value="0">VR</n-radio>
                            <n-radio :value="1">PC</n-radio>
                        </n-space>
                    </n-radio-group>
                </NFormItem> -->
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

<script setup lang="ts">
import {$t} from "@/locales";
import {computed, reactive, watch} from "vue";
import {useFormRules, useNaiveForm} from "@/hooks/common/form";
import { addStepOne, editStepOne } from '@/service/api/business/trainingoperationcard';

interface Props {
    /** the type of operation */
    operateType: NaiveUI.TableOperateType;
    /** the edit row data */
    rowData?: any;
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
    const titles: Record<NaiveUI.TableOperateType, string> = {
        add: '新增培训操作卡',
        edit: '编辑培训操作卡'
    };
    return titles[props.operateType];
});
const createDefaultModel = ()=> {
    return {
        id:'',
        sopCardType: 0,
        name: null,
        initialZB: null,
        cardMap: null,
        sopCardModel: 'train'
    };
}
const model: any = reactive(createDefaultModel());

const handleUpdateModelWhenEdit = ()=> {
    if (props.operateType === 'add') {
        Object.assign(model, createDefaultModel());
        return;
    }
    if (props.operateType === 'edit' && props.rowData) {
        Object.assign(model, props.rowData);
    }
}
const {formRef, validate, restoreValidation} = useNaiveForm();
const {createRequiredRule} = useFormRules();
const rules = {
    id: createRequiredRule('id不能为空'),
    name: createRequiredRule('操作卡名称不能为空'),
    initialZB: createRequiredRule('初始化坐标不能为空'),
    // cardMap: createRequiredRule('关卡地图不能为空'),
    // sopCardType: createRequiredRule('操作卡类型不能为空'),
};
const closeDrawer = ()=>{
    visible.value = false;
}
const handleSubmit = async ()=>{
    await validate();
    const {id, name, initialZB, cardMap, sopCardModel, sopCardType} = model;
    if (props.operateType === 'add') {
        const {error} = await addStepOne({ name, initialZB, cardMap, sopCardModel, sopCardType});
        if (error) return;
    }
    if (props.operateType === 'edit') {
        const {error} = await editStepOne({id, name, initialZB, cardMap, sopCardModel, sopCardType});
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

<style scoped>

</style>
