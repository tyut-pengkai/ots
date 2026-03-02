<template>
    <!-- 原始宽度设置为 800，已注释；下方调整为 1200 以容纳实时预览区域 -->
    <NDrawer v-model:show="visible" :width="1300" class="max-w-90%" display-directive="show">
        <NDrawerContent :native-scrollbar="false" :title="title" closable>
            <!-- 新增外层容器：表单与预览并排显示 -->
            <div class="edit-preview-wrap">
              <!-- 左侧侧实时预览：与页面视觉一致，禁用交互 -->
              <div class="preview-col">
                <TopicPreview :topic-data="model" :is-disabled="true" />
              </div>
                <div class="edit-col">
                <NForm ref="formRef" :model="model" :rules="rules">
                <NFormItem label="题目名称" path="questionName">
                    <NInput v-model:value="model.questionName" placeholder="请输入"/>
                </NFormItem>
                <NFormItem label="所属操作卡" path="hscId">
                    <NSelect
                        v-model:value="model.hscId"
                        :options="sopCardList"
                        label-field="name"
                        value-field="id"
                        placeholder="请选择"
                        @update:value="hscIdChange"
                        :disabled="!!props?.hscId"
                    />
                </NFormItem>
                <NFormItem label="题型" path="questionType">
                    <NSelect
                        v-model:value="model.questionType"
                        :options="questionTypeOptions"
                        placeholder="请选择"
                        @update:value="questionTypeChange"
                    />
                </NFormItem>
                <NFormItem label="题干" path="questionDescription">
                    <Editor v-model:value="model.questionDescription" :min-height="192" />
                </NFormItem>
                <NFormItem label="选项" path="questionItemBos">
                    <div class="estimate-wrap" v-if="model.questionType === 'judge'">
                        <div v-for="item in model.questionItemBos" class="estimate-item">
                            <div class="estimate-item-name">{{item.itemTag}}</div>
                            <div class="estimate-item-isAnswer">
                                <span style="margin-right: 8px">正确答案</span>
                                <n-switch v-model:value="item.isAnswer"
                                          size="small"
                                          checked-value="1"
                                          unchecked-value="0"
                                          @update:value="switchChange(item)"
                                />
                            </div>
                        </div>
                    </div>
                    <div v-if="model.questionType === 'select'" class="option-wrap">
                        <div v-for="(item,idx) in model.questionItemBos" class="option-item">
                            <div class="option-item-name">{{item.itemTag}}</div>
                            <div class="option-item-content">
                                <Editor v-model:value="item.itemDescription" :min-height="192" />
                            </div>
                            <div class="estimate-item-isAnswer">
                                <span style="margin-right: 8px">正确答案</span>
                                <n-switch v-model:value="item.isAnswer"
                                          size="small"
                                          checked-value="1"
                                          unchecked-value="0"
                                          @update:value="switchChange(item)"
                                />
                            </div>
                            <NButton v-if="idx === (model.questionItemBos.length - 1) && idx < 5"
                                     size="small"
                                     ghost
                                     type="primary"
                                     style="margin-left: auto"
                                     @click="addOption(idx)"
                            >
                                <template #icon>
                                    <icon-material-symbols:add class="text-icon" />
                                </template>
                            </NButton>
                            <NButton v-else
                                     size="small"
                                     ghost
                                     type="error"
                                     style="margin-left: auto"
                                     @click="delOption(idx)"
                            >
                                <template #icon>
                                    <icon-material-symbols:delete-outline class="text-icon" />
                                </template>
                            </NButton>
                        </div>
                    </div>
                    <div v-if="model.questionType === 'opt'" class="option-wrap">
                        <div v-for="(item,idx) in model.questionItemBos" class="option-item">
                            <div class="option-item-name">{{item.itemTag}}</div>
                            <NInput v-model:value="item.itemDescription" placeholder="请输入选项内容" style="width: 250px;margin: 0 36px"/>
                            <NSelect
                                v-model:value="item.elementId"
                                :options="elementData"
                                label-field="name"
                                value-field="id"
                                placeholder="请选择交互模型"
                                style="width: 200px;margin-right: 16px"
                            />
                            <div class="estimate-item-isAnswer">
                                <span style="margin-right: 8px">正确答案</span>
                                <n-switch v-model:value="item.isAnswer"
                                          size="small"
                                          checked-value="1"
                                          unchecked-value="0"
                                          @update:value="switchChange(item)"
                                />
                            </div>
                            <NButton v-if="idx === (model.questionItemBos.length - 1) && idx < 5"
                                     size="small"
                                     ghost
                                     type="primary"
                                     style="margin-left: auto"
                                     @click="addOption(idx)"
                            >
                                <template #icon>
                                    <icon-material-symbols:add class="text-icon" />
                                </template>
                            </NButton>
                            <NButton v-else
                                     size="small"
                                     ghost
                                     type="error"
                                     style="margin-left: auto"
                                     @click="delOption(idx)"
                            >
                                <template #icon>
                                    <icon-material-symbols:delete-outline class="text-icon" />
                                </template>
                            </NButton>
                        </div>
                    </div>
                    <div v-if="model.questionType === 'step'" class="option-wrap">
                        <div v-for="(item,idx) in model.questionItemBos" class="option-item">
                            <div class="option-item-name">{{item.itemTag}}</div>
                            <div class="option-item-content">
                                <Editor v-model:value="item.itemDescription" :min-height="192" />
                                <div style="display: flex;margin-top: 16px;align-items: center">
                                    <NSelect
                                        v-model:value="item.hscsoId"
                                        :options="sopCardStepOptList"
                                        label-field="optContent"
                                        value-field="id"
                                        placeholder="请选择步骤"
                                        style="width: 200px;margin-right: 16px"
                                    />
                                </div>
                            </div>

                            <NButton v-if="idx === (model.questionItemBos.length - 1) && idx < 5"
                                     size="small"
                                     ghost
                                     type="primary"
                                     style="margin-left: auto"
                                     @click="addOption(idx)"
                            >
                                <template #icon>
                                    <icon-material-symbols:add class="text-icon" />
                                </template>
                            </NButton>
                            <NButton v-else
                                     size="small"
                                     ghost
                                     type="error"
                                     style="margin-left: auto"
                                     @click="delOption(idx)"
                            >
                                <template #icon>
                                    <icon-material-symbols:delete-outline class="text-icon" />
                                </template>
                            </NButton>
                        </div>
                    </div>
                    <div v-if="model.questionType === 'content_select'" class="option-wrap">
                        <div v-for="(item,idx) in model.questionItemBos" class="option-item">
                            <div class="option-item-name">{{item.itemTag}}</div>
                            <div class="option-item-content">
                                <Editor v-model:value="item.itemDescription" :min-height="192" />
                                <Editor v-model:value="item.itemContentSelect" :min-height="192" style="margin-top: 8px"/>
                            </div>
                            <div class="estimate-item-isAnswer">
                                <span style="margin-right: 8px">正确答案</span>
                                <n-switch v-model:value="item.isAnswer"
                                          size="small"
                                          checked-value="1"
                                          unchecked-value="0"
                                          @update:value="switchChange(item)"
                                />
                            </div>
                            <NButton v-if="idx === (model.questionItemBos.length - 1) && idx < 5"
                                     size="small"
                                     ghost
                                     type="primary"
                                     style="margin-left: auto"
                                     @click="addOption(idx)"
                            >
                                <template #icon>
                                    <icon-material-symbols:add class="text-icon" />
                                </template>
                            </NButton>
                            <NButton v-else
                                     size="small"
                                     ghost
                                     type="error"
                                     style="margin-left: auto"
                                     @click="delOption(idx)"
                            >
                                <template #icon>
                                    <icon-material-symbols:delete-outline class="text-icon" />
                                </template>
                            </NButton>
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
import {computed, reactive, watch, ref} from 'vue';
import {fetchCreateQuestion, fetchUpdateQuestion, getSopCardList, getElementList, getSopCardStepOpt} from '@/service/api/business/question';
import {useFormRules, useNaiveForm} from '@/hooks/common/form';
import {$t} from '@/locales';
import {useDict} from "@/hooks/business/dict";
import type {UmoEditor} from "@umoteam/editor";
import TopicPreview from '@/views/viewtopic/components/topic-preview.vue';

defineOptions({
    name: 'QuestionOperateDrawer'
});

interface Props {
    /** the type of operation */
    operateType: NaiveUI.TableOperateType;
    /** the edit row data */
    rowData?: Api.Business.Question | null;
    hscId?:string
}

const props = defineProps<Props>();

const {options: questionTypeOptions} = useDict('question_type');

interface Emits {
    (e: 'submitted'): void;
}

const emit = defineEmits<Emits>();

const visible = defineModel<boolean>('visible', {
    default: false
});

const umoEditorRef = ref<InstanceType<typeof UmoEditor>>();
const {formRef, validate, restoreValidation} = useNaiveForm();
const {createRequiredRule} = useFormRules();

const title = computed(() => {
    const titles: Record<NaiveUI.TableOperateType, string> = {
        add: '新增考题配置',
        edit: '编辑考题配置'
    };
    return titles[props.operateType];
});

type Model = Api.Business.QuestionOperateParams;

const model: Model = reactive(createDefaultModel());

function createDefaultModel(): Model {
    return {
        id:'',
        hscId: null,
        questionName: '',
        questionDescription: null,
        questionType: 'judge',
        questionItemBos : [
            {
                itemTag:'正确',
                isAnswer:'0'
            },
            {
                itemTag:'错误',
                isAnswer:'0'
            }
        ]
    };
}

type RuleKey = Extract<
    keyof Model,
    | 'id'
    | 'questionName'
    | 'hscId'
    | 'questionType'
    | 'questionDescription'
>;

const rules: Record<RuleKey, App.Global.FormRule> = {
    id: createRequiredRule('id不能为空'),
    questionName: createRequiredRule('题目名称不能为空'),
    hscId:createRequiredRule('请选择所属操作卡'),
    questionType:createRequiredRule('请选择题型'),
    questionDescription:createRequiredRule('题干不能为空')
};

function handleUpdateModelWhenEdit() {
    if (props.operateType === 'add') {
        Object.assign(model, createDefaultModel());
        if(props?.hscId){
            model.hscId = props?.hscId;
        }
        return;
    }

    if (props.operateType === 'edit' && props.rowData) {
        Object.assign(model, props.rowData);
        getSopCardStepOptData(model.hscId);
    }
}

function closeDrawer() {
    visible.value = false;
}

async function handleSubmit() {
    await validate();

    const {id, questionName, hscId, questionDescription, questionType, questionItemBos} = model;
    if (props.operateType === 'add') {
        const {error} = await fetchCreateQuestion({hscId, questionName, questionDescription, questionType ,questionItemBos});
        if (error) return;
    }

    if (props.operateType === 'edit') {
        const {error} = await fetchUpdateQuestion({id, hscId, questionName, questionDescription, questionType, questionItemBos});
        if (error) return;
    }

    window.$message?.success($t('common.updateSuccess'));
    closeDrawer();
    emit('submitted');
}
/** 获取操作卡数据 */
const sopCardList:any = ref([]);
const getSopCardData = ()=>{
    getSopCardList().then(res=>{
        if(res.response.data.code == "200"){
            sopCardList.value = res.response.data.data;
        }
    })
}

/** 获取模型列表 */
const elementData:any = ref([]);
const getElementData = ()=>{
    getElementList({}).then(res=>{
        if(res.response.data.code == "200"){
            elementData.value = res.response.data.data;
        }
    })
}
/** 获取步骤数据列表 */

const sopCardStepOptList:any = ref([]);
const getSopCardStepOptData = (id:any)=>{
    getSopCardStepOpt(id).then(res=>{
        if(res.response.data.code == "200"){
            sopCardStepOptList.value = res.response.data.data;
        }
    })
}
/** 所属操作卡改变时 */
const hscIdChange = ()=>{
    if(model.questionType === 'step'){
        model.questionItemBos.forEach((item:any)=>{
            item.hscsoId = '';
        })
    }
    getSopCardStepOptData(model.hscId);
}
/** 题型改变时 */
const questionTypeChange = ()=>{
    if(model.questionType === 'judge'){
        model.questionItemBos = [
            {
                itemTag:'正确',
                isAnswer:'0'
            },
            {
                itemTag:'错误',
                isAnswer:'0'
            }
        ]
    }else if(model.questionType === 'select'){
        model.questionItemBos = [
            {
                itemTag:'A',
                itemDescription:'',
                isAnswer:'0'
            }
        ]
    }else if(model.questionType === 'opt'){
        model.questionItemBos = [
            {
                itemTag:'A',
                itemDescription:'',
                isAnswer:'0',
                elementId: null
            }
        ]
    }else if(model.questionType === 'step'){
        model.questionItemBos = [
            {
                itemTag:'A',
                itemDescription:'',
                isAnswer:'0',
                hscsoId: null
            }
        ]
    }else if(model.questionType === 'content_select'){
        model.questionItemBos = [
            {
                itemTag:'A',
                itemDescription:'',
                itemContentSelect: '',
                isAnswer:'0'

            }
        ]
    }
}
/** 是否为正确答案选项 */
const switchChange = (item:any)=>{
    if(item.isAnswer === '1'){
        model.questionItemBos?.forEach((val:any)=>{
            if(item.itemTag != val.itemTag){
                val.isAnswer = '0';
            }
        })
    }
}
const alphabet = ['A','B','C','D','E','F'];
/** 新增选项 */
const addOption = (idx:any)=>{
    console.log(model.questionType);
    if(model.questionType === 'judge'){
        model.questionItemBos?.push({
            itemTag:alphabet[idx+1],
            itemDescription:'',
            isAnswer:'0'
        })
    }else if(model.questionType === 'select'){
        model.questionItemBos?.push({
            itemTag:alphabet[idx+1],
            itemDescription:'',
            isAnswer:'0'
        })
    }else if(model.questionType === 'step'){
        model.questionItemBos?.push({
            itemTag:alphabet[idx+1],
            itemDescription:'',
            isAnswer:'0',
            hscsoId: null
        })
    }else if(model.questionType === 'content_select'){
        model.questionItemBos?.push({
            itemTag:alphabet[idx+1],
            itemDescription:'',
            itemContentSelect: '',
            isAnswer:'0'
        })
    }
}
/** 删除选项 */
const delOption = (idx:any)=>{
    model.questionItemBos?.splice(idx,1);
    model.questionItemBos?.forEach((val:any,num:any)=>{
        val.itemTag = alphabet[num];
    })
}

watch(visible, () => {
    if (visible.value) {
        handleUpdateModelWhenEdit();
        restoreValidation();
        getSopCardData();
        getElementData();
    }
});
</script>
<style lang="scss" scoped>
.estimate-wrap{
    width: 100%;
    .estimate-item{
        display: flex;
        margin-bottom: 8px;
        .estimate-item-name{
            margin-right: 36px;
        }
    }
}
.option-wrap{
    width: 100%;
    .option-item{
        display: flex;
        margin-top: 16px;
        .option-item-content{
            flex: 1;
            padding: 0 16px;
        }
        .estimate-item-isAnswer{
            margin-right: 16px;
        }
    }
}
.edit-preview-wrap{
  display: flex;
  gap: 16px;
  align-items: flex-start;
  .edit-col{
    flex: 1;
    min-width: 480px;
  }
  .preview-col{
    flex: 1;
    min-width: 480px;
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 8px 0;
    position: sticky;
    top: 50%;
    transform: translateY(-50%);
  }
}
</style>
