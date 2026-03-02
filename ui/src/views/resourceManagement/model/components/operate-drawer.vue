<template>
    <NDrawer v-model:show="visible" :title="title" :width="1300" class="max-w-90%" display-directive="show">
        <NDrawerContent :native-scrollbar="false" :title="title" closable>
            <div class="edit-preview-wrap">
                <div class="preview-col">
                    <div class="file-card">
                        <div v-html="model.description"
                             class="ql-editor"
                        />
                    </div>
                </div>
                <div class="edit-col">
                    <NForm ref="formRef" :model="model" :rules="rules">
                        <NFormItem label="模型标识" path="ueId">
                            <NInput v-model:value="model.ueId" placeholder="请输入模型标识"/>
                        </NFormItem>
                        <NFormItem label="模型类别" path="elementType">
                            <NSelect
                                v-model:value="model.elementType"
                                :options="ueModelTypeOptions"
                                clearable
                                placeholder="请选择模型类别"
                            />
                        </NFormItem>
                        <NFormItem label="模型名称" path="name">
                            <NInput v-model:value="model.name" placeholder="请输入模型名称"/>
                        </NFormItem>
                        <NFormItem label="描述" path="description">
                            <Editor v-if="model.elementType === 'equipment_info'" v-model:value="model.description" :min-height="192" />
                            <NInput v-else v-model:value="model.description" placeholder="请输入描述"/>
                        </NFormItem>
                        <NFormItem label="附件" path="file">
                            <div class="upload-wrap">
                                <n-upload
                                    v-model:file-list="model.file"
                                    :default-upload="false"
                                    show-remove-button
                                    :show-file-list="false"
                                    :max="1"
                                    @update:file-list="handleFileListChange"
                                >
                                    <n-button>上传文件</n-button>
                                </n-upload>
                                <br/>
                                <div v-for="item in model.file" class="file-list-wrap">
                                    <n-image
                                        width="100"
                                        height="50"
                                        :src="item.path"
                                    />
                                    <span class="img-name">{{item.name}}</span>
                                    <n-button quaternary circle class="del-btn" @click="handleRemoveFile">
                                        <template #icon>
                                            <icon-material-symbols:delete-outline class="text-icon" />
                                        </template>
                                    </n-button>
                                </div>
                            </div>
                        </NFormItem>
                    </NForm>
                </div>
            </div>
            <template #footer>
                <NSpace :size="16">
                    <NButton @click="closeDrawer">{{ $t('common.cancel') }}</NButton>
                    <NButton type="primary" @click="handleSubmit">{{ $t('common.confirm') }}</NButton>
                </NSpace>
            </template>
        </NDrawerContent>
    </NDrawer>
</template>

<script lang="ts" setup>
import {computed, reactive, watch} from 'vue';
import {fetchCreateElement, fetchUpdateElement} from '@/service/api/business/element';
import {useFormRules, useNaiveForm} from '@/hooks/common/form';
import {useDict} from '@/hooks/business/dict';
import {$t} from '@/locales';
import FileUpload from '@/components/custom/file-upload.vue';
import { AcceptType } from '@/enum/business';
import '@vueup/vue-quill/dist/vue-quill.snow.css';

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

const {options: ueModelTypeOptions} = useDict('ue_model_type');

const {formRef, validate, restoreValidation} = useNaiveForm();
const {createRequiredRule} = useFormRules();

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
        elementType: null,
        name: '',
        description: '',
        file: []
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
        console.log('model===========>>>',model);
        return;
    }

    if (props.operateType === 'edit' && props.rowData) {
        console.log('rowData===========>>>',props.rowData);
        console.log('model===========>>>',model);
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
    formData.append('file', model.file[0]?.file ?? '');
    console.log(model.file);
    if (props.operateType === 'add') {
        const {error} = await fetchCreateElement(formData);
        if (error) return;
    }

    if (props.operateType === 'edit') {
        formData.append('id', model.id ?? '');
        const {error} = await fetchUpdateElement(formData);
        if (error) return;
    }

    window.$message?.success($t('common.updateSuccess'));
    closeDrawer();
    emit('submitted');
}
const handleFileListChange = (data:any)=>{
    const reader = new FileReader();
    reader.readAsDataURL(new Blob([model.file[0].file], { type: 'image/png' }));
    reader.onload = (e:any) => {
        model.file[0].path = e.target.result;
    };
}
const handleRemoveFile = ()=>{
    model.file.length = 0;
}

watch(visible, () => {
    if (visible.value) {
        handleUpdateModelWhenEdit();
        restoreValidation();
    }
});

</script>

<style scoped lang="scss">
.edit-preview-wrap {
    display: flex;
    gap: 16px;
    align-items: flex-start;
    .edit-col{
        flex: 1;
        min-width: 480px;
    }
    .preview-col {
        flex: 1;
        min-width: 480px;
        padding: 8px 0;
        .file-card {
            width: 100%;
            min-width: 350px;
            min-height: 400px;
            background-image: url('@/assets/svg-icon/view_bg_2.svg');
            background-position: center;
            background-size: 100% 100%;
            background-repeat: no-repeat;
            padding: 3% 2.5% 2.5%;
            color: #fff;
        }
    }
}
.upload-wrap {
    width: 100%;
    .file-list-wrap {
        width: 100%;
        padding: 8px;
        display: flex;
        align-items: center;
        background: #F3F3F5;
        position: relative;
        border-radius: 5px;

        .img-name {
            margin-left: 100px;
        }

        .del-btn {
            float: right;
            position: absolute;
            right: 16px;
        }
    }
}
</style>
