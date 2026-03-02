<template>
    <NDrawer v-model:show="visible" :title="title" :width="800" class="max-w-90%" display-directive="show">
        <NDrawerContent :native-scrollbar="false" :title="title" closable>
            <NForm ref="formRef" :model="model" :rules="rules">
                <NFormItem label="序号" path="indexing" v-if="props.operateType === 'edit'">
                    <NInput v-model:value="model.indexing" placeholder="请输入"/>
                </NFormItem>
                <NFormItem label="步骤名称" path="groupName">
                    <NInput v-model:value="model.groupName" placeholder="请输入"/>
                </NFormItem>
                <NFormItem label="具体内容" path="groupContent">
                    <NInput v-model:value="model.groupContent" placeholder="请输入" type="textarea"/>
                </NFormItem>
                <NFormItem label="执行标准/注意事项" path="groupContentDetail">
                    <NInput v-model:value="model.groupContentDetail" placeholder="请输入" type="textarea"/>
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

<script setup lang="ts">
import {$t} from "@/locales";
import {computed, reactive, watch} from "vue";
import {useFormRules, useNaiveForm} from "@/hooks/common/form";
import {
    addSopCardStepGroup,
    editSopCardStepGroup,
} from '@/service/api/business/trainingoperationcard';
interface Props {
    /** the type of operation */
    operateType: NaiveUI.TableOperateType;
    /** the edit row data */
    rowData?: any;
    stepOneHscId: any;
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
        add: '新增培训操作卡分组',
        edit: '编辑培训操作卡分组'
    };
    return titles[props.operateType];
});
const createDefaultModel = ()=> {
    return {
        id:'',
        indexing: null,
        groupName: null,
        groupContent: null,
        groupContentDetail: null
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
    groupName: createRequiredRule('步骤名称不能为空'),
    groupContent: createRequiredRule('具体内容不能为空'),
    groupContentDetail: createRequiredRule('执行标准/注意事项不能为空')
};
const closeDrawer = ()=>{
    visible.value = false;
}
const handleSubmit = async ()=> {
    await validate();
    const {id, indexing, groupName, groupContent, groupContentDetail} = model;
    if (props.operateType === 'add') {
        const {error} = await addSopCardStepGroup({ groupName, groupContent, groupContentDetail, hscId:props.stepOneHscId});
        if (error) return;
    }
    if (props.operateType === 'edit') {
        const {error} = await editSopCardStepGroup({id, indexing, groupName, groupContent, groupContentDetail, hscId:props.stepOneHscId});
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
