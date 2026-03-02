<template>
    <NDrawer v-model:show="visible" :title="title" :width="800" class="max-w-90%" display-directive="if">
        <NDrawerContent :native-scrollbar="false" :title="title" closable>
            <NForm ref="formRef" :model="model" :rules="rules">
                <NFormItem label="序号" path="indexing" v-if="props.operateType === 'edit'">
                    <n-input-number style="width: 100%" v-model:value="model.indexing" :show-button="false" />
                </NFormItem>
                <NFormItem label="操作内容" path="optContent">
                    <NInput v-model:value="model.optContent" placeholder="请输入"/>
                </NFormItem>
                <n-row gutter="12">
                    <n-col :span="18">
                        <NFormItem label="操作解说" path="optCommentary">
                            <NInput v-model:value="model.optCommentary" placeholder="请输入" type="textarea"/>
                        </NFormItem>
                    </n-col>
                    <n-col :span="6">
                        <NFormItem label="操作语音开关" path="voicePlayTiming">
                            <NSelect v-model:value="model.voicePlayTiming"
                                     :options="[
                                         {
                                            label: '不播放',
                                            value: 'never'
                                         },{
                                            label: '操作前',
                                            value: 'before'
                                         },{
                                            label: '操作后',
                                            value: 'after'
                                         }
                                     ]"
                            />
                        </NFormItem>
                    </n-col>
                </n-row>
                <n-row gutter="12">
                    <n-col :span="18">
                        <NFormItem label="错误提示" path="errorMsg">
                            <NInput v-model:value="model.errorMsg" placeholder="请输入" type="textarea"/>
                        </NFormItem>
                    </n-col>
                    <n-col :span="6">
                        <NFormItem label="错误提示语音开关" path="errorVoiceStatus">
                            <n-switch v-model:value="model.errorVoiceStatus" :checked-value="1" :unchecked-value="0"/>
                        </NFormItem>
                    </n-col>
                </n-row>
                <n-divider title-placement="center" style="margin-top: 0">交互行为</n-divider>
                <NFormItem label="交互方式" path="eventType">
                    <NSelect v-model:value="model.eventType"
                             :options="interactionModeType"
                             filterable
                    />
                </NFormItem>
                <n-card size="small" style="margin-bottom: 10px;">
                    <NFormItem label="交互对象" path="mainObject">
                        <NSelect v-model:value="model.mainObjectElementType"
                                :options="mainObjectList"
                                style="margin-right: 8px"
                                @update-value="mainObjectElementTypeChange"
                                filterable
                        />
                        <NSelect v-model:value="model.mainObject"
                                :options="mainObjectChildList"
                                style="margin-left: 8px"
                                label-field="name"
                                value-field="id"
                                filterable
                        />
                    </NFormItem>
                    <div class="transfer-wrap" v-if="mainObjectAttr.length > 0">
                        <div class="transfer-item">
                            <n-divider title-placement="left" class="divider-title">
                                初始状态
                            </n-divider>
                            <template v-for="item in mainObjectAttr">
                                <NFormItem :label="item.description" :path="item.name" style="padding-left: 64px">
                                    <NSelect v-if="item.type === 'select'"
                                            v-model:value="model.mainObjectInitalStatus[item.name]"
                                            :options="item.options"
                                            filterable
                                    />
                                    <NInput v-else-if="item.type === 'input'"
                                            v-model:value="model.mainObjectInitalStatus[item.name]"
                                            placeholder="请输入"
                                    />
                                    <NInputNumber v-else-if="item.type === 'number'"
                                                v-model:value="model.mainObjectInitalStatus[item.name]"
                                                :show-button="false"
                                                style="width: 100%"
                                    />
                                </NFormItem>
                            </template>
                        </div>
                        <div class="transfer-middle-btn">
                            <div class="transfer-middle-border"/>
                            <NButton size="small" type="tertiary" style="margin: 8px 0" @click="handleCopy('mainObjectInitalStatus','mainObjectEndingStatus')">
                                <template #icon>
                                    <icon-material-symbols:arrow-forward-ios-rounded />
                                </template>
                            </NButton>
                            <NButton size="small" type="tertiary" style="margin: 8px 0" @click="handleCopy('mainObjectEndingStatus','mainObjectInitalStatus')">
                                <template #icon>
                                    <icon-material-symbols:arrow-back-ios-rounded />
                                </template>
                            </NButton>
                            <div class="transfer-middle-border" />
                        </div>
                        <div class="transfer-item">
                            <n-divider title-placement="left" class="divider-title">
                                交互后
                            </n-divider>
                            <template v-for="item in mainObjectAttr">
                                <NFormItem :label="item.description" :path="item.name" style="padding-left: 64px">
                                    <NSelect v-if="item.type === 'select'"
                                            v-model:value="model.mainObjectEndingStatus[item.name]"
                                            :options="item.options"
                                            placeholder="请选择"
                                            filterable
                                    />
                                    <NInput v-else-if="item.type === 'input'"
                                            v-model:value="model.mainObjectEndingStatus[item.name]"
                                            placeholder="请输入"
                                    />
                                    <NInputNumber v-else-if="item.type === 'number'"
                                                v-model:value="model.mainObjectEndingStatus[item.name]"
                                                :show-button="false"
                                                style="width: 100%"
                                    />
                                </NFormItem>
                            </template>
                        </div>
                    </div>
                </n-card>
                <n-card size="small" style="margin-bottom: -10px;">
                    <NFormItem label="影响对象" path="targetObject">
                        <NSelect v-model:value="model.targetObjectElementType"
                                :options="targetObjectList"
                                style="margin-right: 8px"
                                @update-value="targetObjectElementTypeChange"
                                filterable
                        />
                        <NSelect v-model:value="model.targetObject"
                                :options="targetObjectChildList"
                                style="margin-left: 8px"
                                label-field="name"
                                value-field="id"
                                filterable
                        />
                    </NFormItem>
                    <div class="transfer-wrap" v-if="targetObjectAttr.length > 0">
                        <div class="transfer-item">
                            <n-divider title-placement="left" class="divider-title">
                                初始状态
                            </n-divider>
                            <template v-for="item in targetObjectAttr">
                                <NFormItem :label="item.description" :path="item.name" style="padding-left: 64px">
                                    <NSelect v-if="item.type === 'select'"
                                            v-model:value="model.targetObjectInitalStatus[item.name]"
                                            :options="item.options"
                                            filterable
                                    />
                                    <NInput v-else-if="item.type === 'input'"
                                            v-model:value="model.targetObjectInitalStatus[item.name]"
                                            placeholder="请输入"
                                    />
                                    <NInputNumber v-else-if="item.type === 'number'"
                                                v-model:value="model.targetObjectInitalStatus[item.name]"
                                                :show-button="false"
                                                style="width: 100%"
                                    />
                                </NFormItem>
                            </template>
                        </div>
                        <div class="transfer-middle-btn">
                            <div class="transfer-middle-border"/>
                            <NButton size="small"
                                    type="tertiary"
                                    style="margin: 8px 0"
                                    @click="handleCopy('targetObjectInitalStatus','targetObjectEndingStatus')"
                            >
                                <template #icon>
                                    <icon-material-symbols:arrow-forward-ios-rounded />
                                </template>
                            </NButton>
                            <NButton size="small"
                                    type="tertiary"
                                    style="margin: 8px 0"
                                    @click="handleCopy('targetObjectEndingStatus','targetObjectInitalStatus')"
                            >
                                <template #icon>
                                    <icon-material-symbols:arrow-back-ios-rounded />
                                </template>
                            </NButton>
                            <div class="transfer-middle-border" />
                        </div>
                        <div class="transfer-item">
                            <n-divider title-placement="left" class="divider-title">
                                交互后
                            </n-divider>
                            <template v-for="item in targetObjectAttr">
                                <NFormItem :label="item.description" :path="item.name" style="padding-left: 64px">
                                    <NSelect v-if="item.type === 'select'"
                                            v-model:value="model.targetObjectEndingStatus[item.name]"
                                            :options="item.options"
                                            placeholder="请选择"
                                            filterable
                                    />
                                    <NInput v-else-if="item.type === 'input'"
                                            v-model:value="model.targetObjectEndingStatus[item.name]"
                                            placeholder="请输入"
                                    />
                                    <NInputNumber v-else-if="item.type === 'number'"
                                                v-model:value="model.targetObjectEndingStatus[item.name]"
                                                :show-button="false"
                                                style="width: 100%"
                                    />
                                </NFormItem>
                            </template>
                        </div>
                    </div>
                </n-card>
                <n-divider title-placement="center">影响结果</n-divider>
                <n-space>
                    <n-checkbox v-model:checked="model.effect.animation.choose"
                                style="margin-top: 6px"
                    >
                        动画
                    </n-checkbox>
                    <NFormItem label="延迟时间" label-placement="left">
                        <NInputNumber v-model:value="model.effect.animation.resourceDelayTime"
                                      :show-button="true"
                                      :min="0"
                                      :max="20"
                                      style="width: 110px"
                        />
                    </NFormItem>
                </n-space>
                <NFormItem label="动画资源" style="padding-left: 40px" v-if="model.effect.animation.choose" >
                    <animationDialog v-model:animation-id="model.effect.animation.resourceAttr.id"/>
                </NFormItem>
                <div class="transfer-wrap" v-if="animationAttr.length > 0 && model.effect.animation.choose" style="margin-bottom: 16px">
                    <div class="transfer-item">
                        <n-divider title-placement="left" class="divider-title">
                            初始状态
                        </n-divider>
                        <template v-for="item in animationAttr">
                            <NFormItem :label="item.description" :path="item.name" style="padding-left: 64px">
                                <NSelect v-if="item.type === 'select'"
                                         v-model:value="model.effect.animation.resourceAttr.animationInitalStatus[item.name]"
                                         :options="item.options"
                                         filterable
                                />
                                <NInput v-else-if="item.type === 'input'"
                                        v-model:value="model.effect.animation.resourceAttr.animationInitalStatus[item.name]"
                                        placeholder="请输入"
                                />
                                <NInputNumber v-else-if="item.type === 'number'"
                                              v-model:value="model.effect.animation.resourceAttr.animationInitalStatus[item.name]"
                                              :show-button="false"
                                              style="width: 100%"
                                />
                            </NFormItem>
                        </template>
                    </div>
                    <div class="transfer-middle-btn">
                        <div class="transfer-middle-border"/>
                        <NButton size="small" type="tertiary" style="margin: 8px 0" @click="handleCopyAnimation('animationInitalStatus','animationEndingStatus')">
                            <template #icon>
                                <icon-material-symbols:arrow-forward-ios-rounded />
                            </template>
                        </NButton>
                        <NButton size="small" type="tertiary" style="margin: 8px 0" @click="handleCopyAnimation('animationEndingStatus','animationInitalStatus')">
                            <template #icon>
                                <icon-material-symbols:arrow-back-ios-rounded />
                            </template>
                        </NButton>
                        <div class="transfer-middle-border" />
                    </div>
                    <div class="transfer-item">
                        <n-divider title-placement="left" class="divider-title">
                            交互后
                        </n-divider>
                        <div v-for="item in animationAttr">
                            <NFormItem :label="item.description" :path="item.name" style="padding-left: 64px">
                                <NSelect v-if="item.type === 'select'"
                                         v-model:value="model.effect.animation.resourceAttr.animationEndingStatus[item.name]"
                                         :options="item.options"
                                         filterable
                                />
                                <NInput v-else-if="item.type === 'input'"
                                        v-model:value="model.effect.animation.resourceAttr.animationEndingStatus[item.name]"
                                        placeholder="请输入"
                                />
                                <NInputNumber v-else-if="item.type === 'number'"
                                              v-model:value="model.effect.animation.resourceAttr.animationEndingStatus[item.name]"
                                              :show-button="false"
                                              style="width: 100%"
                                />
                            </NFormItem>
                        </div>
                    </div>
                </div>
                <n-space>
                    <n-checkbox v-model:checked="model.effect.popwindow.choose"
                                style="margin-top: 6px"
                    >
                        弹窗
                    </n-checkbox>
                    <NFormItem label="延迟时间" label-placement="left">
                        <NInputNumber v-model:value="model.effect.popwindow.resourceDelayTime"
                                      :show-button="true"
                                      :min="0"
                                      :max="20"
                                      style="width: 110px"
                        />
                    </NFormItem>
                </n-space>
                <template v-if="model.effect.popwindow.choose">
                    <NFormItem v-for="item in popAttr" :label="item.description" :path="item.name" style="padding-left: 40px">
                        <NSelect v-if="item.type === 'select'"
                                 v-model:value="model.effect.popwindow.resourceAttr[item.name]"
                                 :options="item.options"
                                 filterable
                                 @update:value="popTypeChange(item.description,model.effect.popwindow.resourceAttr[item.name])"
                        />
                        <NInput v-else-if="item.type === 'input'"
                                v-model:value="model.effect.popwindow.resourceAttr[item.name]"
                                placeholder="请输入"
                        />
                        <NInputNumber v-else-if="item.type === 'number'"
                                      v-model:value="model.effect.popwindow.resourceAttr[item.name]"
                                      :show-button="false"
                                      style="width: 100%"
                        />
                    </NFormItem>
                    <NFormItem v-for="item in popTypeAttr"  :label="item.description" :path="item.name" style="padding-left: 40px">
                        <NSelect v-if="item.type === 'select'"
                                 v-model:value="model.effect.popwindow.resourceAttr[item.name]"
                                 :options="item.options"
                                 filterable
                        />
                        <NInput v-else-if="item.type === 'input'"
                                v-model:value="model.effect.popwindow.resourceAttr[item.name]"
                                placeholder="请输入"
                        />
                        <NInputNumber v-else-if="item.type === 'number'"
                                      v-model:value="model.effect.popwindow.resourceAttr[item.name]"
                                      :show-button="false"
                                      style="width: 100%"
                        />

                        <n-radio-group v-else-if="item.type === 'radio'"
                                       v-model:value="model.effect.popwindow.resourceAttr[item.name]"
                                       name="radioGroup"
                        >
                            <n-space>
                                <n-radio v-for="song in item.options" :key="song" :value="song">
                                    {{ song }}
                                </n-radio>
                            </n-space>
                        </n-radio-group>
                        <QuestionsDialog  v-else-if="item.type === 'questionsDialog'"
                                          v-model:questionId="model.effect.popwindow.resourceAttr[item.name]"
                                          :hsc-id = "props.stepOneHscId"
                        />
                        <FileResourceDialog v-else-if="item.type === 'fileResourceDialog'"
                                            v-model:fileResourceId="model.effect.popwindow.resourceAttr[item.name]"
                                            :description="item.description"
                        />
                        <Editor v-else-if="item.type === 'textarea'"
                                v-model:value="model.effect.popwindow.resourceAttr[item.name]" :min-height="192"
                        />
                    </NFormItem>
                </template>
                <n-space>
                    <n-checkbox v-model:checked="model.effect.voice.choose"
                                style="margin-top: 6px"
                    >
                        语音
                    </n-checkbox>
                    <NFormItem label="延迟时间" label-placement="left">
                        <NInputNumber v-model:value="model.effect.voice.resourceDelayTime"
                                      :show-button="true"
                                      :min="0"
                                      :max="20"
                                      style="width: 110px"
                        />
                    </NFormItem>
                </n-space>
                <template v-if="model.effect.voice.choose">
                    <NFormItem v-for="item in voiceAttr" :label="item.description" :path="item.name" style="padding-left: 40px">
                        <NSelect v-if="item.type === 'select'"
                                 v-model:value="model.effect.voice.resourceAttr[item.name]"
                                 :options="item.options"
                                 filterable
                        />
                        <NInput v-else-if="item.type === 'input'"
                                v-model:value="model.effect.voice.resourceAttr[item.name]"
                                placeholder="请输入"
                        />
                        <NInputNumber v-else-if="item.type === 'number'"
                                      v-model:value="model.effect.voice.resourceAttr[item.name]"
                                      :show-button="false"
                                      style="width: 100%"
                        />
                    </NFormItem>
                </template>
                <!-- <n-divider title-placement="center">评分标准</n-divider>
                <NFormItem label="评分对象" :path="model.scoringCriteria">
                    <NSelect v-model:value="model.scoringCriteria"
                             :options="scoringCriteria"
                             placeholder="请选择"
                             filterable
                    />
                </NFormItem>
                <NFormItem label="分值">
                    <NInputNumber v-model:value="model.score"
                                  :show-button="false"
                                  style="width: 100%"
                    />
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
import {computed, reactive, watch, ref} from "vue";
import {useFormRules, useNaiveForm} from "@/hooks/common/form";
import {useDict} from "@/hooks/business/dict";
import {getElementList} from "@/service/api/business/question";
import {fetchGetDictDataByType} from "@/service/api/system";
import QuestionsDialog from '../components/questions-dialog.vue';
import FileResourceDialog from '../components/fileResource-dialog.vue';
import animationDialog from '../components/animation-dialog.vue';
import {
    addSopCardStepOpt,
    editSopCardStepGroup,
    editSopCardStepOptInfo
} from '@/service/api/business/trainingoperationcard';
import {jsonClone} from "~/packages/utils/src/klona";


interface Props {
    /** the type of operation */
    operateType: NaiveUI.TableOperateType;
    /** the edit row data */
    rowData?: any;
    stepTwoHscId: any;
    stepOneHscId: any;
}
const props = defineProps<Props>();
const {options: interactionModeType } = useDict('interaction_mode_type ');
const {options: ueModelType } = useDict('ue_model_type ');
const {options: scoringCriteria } = useDict('scoring_criteria ');

/** 获取对应字典数据 */
let ueModelTypeData:any = [];
let responseResultTypeData:any = []
let popWindowTypeData:any = [];
const getDictData = async ()=>{
    await Promise.all([
        fetchGetDictDataByType('ue_model_type'),
        fetchGetDictDataByType('response_result_type'),
        fetchGetDictDataByType('popwindow_type')
    ]).then(values=>{
        ueModelTypeData = values[0].data;
        responseResultTypeData = values[1].data;
        popWindowTypeData = values[2].data;
    })
}
interface Emits {
    (e: 'submitted'): void;
}
const emit = defineEmits<Emits>();
const visible = defineModel<boolean>('visible', {
    default: false
});
const title = computed(() => {
    const titles: Record<NaiveUI.TableOperateType, string> = {
        add: '新增培训操作卡步骤',
        edit: '编辑培训操作卡步骤',
        copy: '新增培训操作卡步骤'
    };
    return titles[props.operateType];
});
const createDefaultModel = ()=> {
    return {
        id:'',
        hscsgId: props.stepTwoHscId,
        indexing: null,
        optContent: null,
        optCommentary: null,
        voicePlayTiming: 'before',
        errorMsg: null,
        errorVoiceStatus: 1,
        eventType: null,
        mainObjectElementType: null,
        mainObject: null,
        mainObjectInitalStatus:{},
        mainObjectEndingStatus:{},
        targetObjectElementType: null,
        targetObject:null,
        targetObjectInitalStatus: {},
        targetObjectEndingStatus: {},
        effect:{
            animation:{
                choose: false,
                resourceAttr: {
                    id:null,
                    animationInitalStatus:{},
                    animationEndingStatus:{}
                },
                resourceDelayTime: null
            },
            popwindow:{
                choose: false,
                resourceAttr: {},
                resourceDelayTime: null
            },
            voice:{
                choose: false,
                resourceAttr: {},
                resourceDelayTime: null
            }
        },
        scoringCriteria: null,
        score: null
    };
}
const model: any = reactive(createDefaultModel());
const handleUpdateModelWhenEdit = ()=> {
    if (props.operateType === 'add') {
        Object.assign(model, createDefaultModel());


        // 编辑时，将当前项的 indexing 传递给抽屉组件
        if (props.rowData && props.rowData.indexing !== undefined && props.rowData.indexing !== null) {
            model.indexing = props.rowData.indexing;
        }
        popTypeAttr.value = [];
        mainObjectAttr.value = [];
        targetObjectAttr.value = [];
        getAnimationAttr();
        setAnimationAttr();
        popOpt();
        voiceOpt();
        return;
    }
    if (['edit','copy'].includes(props.operateType) && props.rowData) {
        Object.assign(model, props.rowData);
        if(!model.effect.animation){
            model.effect.animation = {
                choose: false,
                resourceAttr: {
                    id:null,
                    animationInitalStatus:{},
                    animationEndingStatus:{}
                },
                resourceDelayTime: null
            }
        }
        if(!model.effect.popwindow){
            model.effect.popwindow = {
                choose: false,
                resourceAttr: {},
                resourceDelayTime: null
            }
        }
        if(!model.effect.voice){
            model.effect.voice = {
                choose: false,
                resourceAttr: {},
                resourceDelayTime: null
            }
        }
        if(model.mainObjectElementType){
            getMainObjectElementList(model.mainObjectElementType);
            getMainObjectAttr(model.mainObjectElementType);
        }
        if(model.targetObjectElementType){
            getTargetObjectElementList(model.targetObjectElementType);
            getTargetObjectAttr(model.targetObjectElementType);
        }
        getAnimationAttr();
        getPopAttr();
        if(model.effect.popwindow.resourceAttr.popwindowType){
            getPopTypeAttr(model.effect.popwindow.resourceAttr.popwindowType)
        }
        getVoiceAttr();
    }
}
const {formRef, validate, restoreValidation} = useNaiveForm();
const {createRequiredRule} = useFormRules();
const rules = {};
const closeDrawer = ()=>{
    visible.value = false;
}
const handleSubmit = async ()=> {
    await validate();
    if (['add','copy'].includes(props.operateType)) {
        let body = jsonClone(model);
        if(props.operateType === 'add' && props?.rowData?.insertLocal){
            body.insertLocal = props?.rowData?.insertLocal;
            body.id = props?.rowData?.id;
        }
        const {error} = await addSopCardStepOpt(body);
        if (error) return;
    }
    if (props.operateType === 'edit') {
        const {error} = await editSopCardStepOptInfo(model);
        if (error) return;
    }

    window.$message?.success($t('common.updateSuccess'));
    closeDrawer();
    emit('submitted');
}
/**查询属性里的字典参数*/
const getAttrDictData = (attr:any)=>{
    attr.forEach((val:any)=>{
        if(val.type === 'dict'){
            const {options: data } = useDict(val.options);
            val.options = data;
            val.type = 'select';
        }
    })
}
/** 交互对象 */
const mainObjectList:any = ref([]);
const mainObjectChildList:any = ref([]);
const mainObjectElementTypeChange = (val:any)=>{
    model.mainObject = null;
    getMainObjectElementList(val);
    getMainObjectAttr(val);
    setMainObjectAttr();
}
/** 获取交互对象子项列表 */
const  getMainObjectElementList = async (type:any)=>{
    getElementList({elementType:type}).then(res=>{
        mainObjectChildList.value = res.data;
    })
}
/** 获取交互对象属性配置 */
const mainObjectAttr:any = ref([]);
const getMainObjectAttr = (type:any)=>{
    let dictItem:any = ueModelTypeData?.find((val:any)=>val.dictValue === type);
    if(dictItem){
        mainObjectAttr.value = JSON.parse(dictItem.attr);
        getAttrDictData(mainObjectAttr.value);
    }
}
/** 设置交互对象属性表单配置 */
const setMainObjectAttr = ()=>{
    model.mainObjectInitalStatus = {};
    model.mainObjectEndingStatus = {};
    mainObjectAttr.value.forEach((item:any)=>{
        model.mainObjectInitalStatus[item.name] = item.defaultValue;
        model.mainObjectEndingStatus[item.name] = item.defaultValue;
    })
}
/** 影响对象 */
const targetObjectList:any = ref([]);
const targetObjectChildList:any = ref([]);
const targetObjectElementTypeChange = (val:any)=>{
    model.targetObject = null;
    getTargetObjectElementList(val);
    getTargetObjectAttr(val);
    setTargetObjectAttr();
}
/** 获取影响对象子项列表 */
const getTargetObjectElementList = (type:any)=>{
    getElementList({elementType: type }).then(res=>{
        targetObjectChildList.value = res.data;
    })
}
/** 获取影响对象属性配置 */
const targetObjectAttr:any = ref([]);
const getTargetObjectAttr = (type:any)=>{
    let dictItem:any = ueModelTypeData?.find((val:any)=>val.dictValue === type);
    if(dictItem){
        targetObjectAttr.value = JSON.parse(dictItem.attr);
        getAttrDictData(targetObjectAttr.value);
    }
}
/** 设置影响对象属性表单配置 */
const setTargetObjectAttr = ()=>{
    model.targetObjectInitalStatus = {};
    model.targetObjectEndingStatus = {};
    targetObjectAttr.value.forEach((item:any)=>{
        model.targetObjectInitalStatus[item.name] = item.defaultValue;
        model.targetObjectEndingStatus[item.name] = item.defaultValue;
    })
}
/** 获取动画属性配置 */
const animationAttr:any = ref([]);
const getAnimationAttr = ()=>{
    let dictItem:any = responseResultTypeData?.find((val:any)=>val.dictValue === 'animation');
    if(dictItem){
        animationAttr.value = JSON.parse(dictItem.attr);
        getAttrDictData(animationAttr.value);
    }
}
/** 设置动画属性表单配置 */
const setAnimationAttr = ()=>{
    model.effect.animation.resourceAttr.animationInitalStatus = {};
    model.effect.animation.resourceAttr.animationEndingStatus = {};
    animationAttr.value.forEach((item:any)=>{
        model.effect.animation.resourceAttr.animationInitalStatus[item.name] = item.defaultValue;
        model.effect.animation.resourceAttr.animationEndingStatus[item.name] = item.defaultValue;
    })
}

/** 语音表单数据配置 */
const popAttr:any = ref([]);
const popOpt = async()=>{
    getPopAttr();
    setPopAttr();
}
/** 获取弹窗属性配置*/
const getPopAttr = ()=>{
    let dictItem:any = responseResultTypeData?.find((val:any)=>val.dictValue === 'popwindow');
    if(dictItem){
        popAttr.value = JSON.parse(dictItem.attr);
        getAttrDictData(popAttr.value);
    }
}
/** 设置弹窗属性表单配置 */
const setPopAttr = ()=>{
    model.effect.popwindow.resourceAttr = {};
    popAttr.value.forEach((item:any)=>{
        model.effect.popwindow.resourceAttr[item.name] = item.defaultValue;
    })
}
/** 弹窗类型选择改变时 */
const popTypeChange = async (label:any,type:any)=>{
    if(label !== '弹窗类型') return;
    getPopTypeAttr(type);
    setPopTypeAttr();
}
/** 获取弹窗类型属性配置 */
const popTypeAttr:any = ref([]);
const getPopTypeAttr = (type:any)=>{
    let dictItem:any = popWindowTypeData?.find((val:any)=>val.dictValue === type);
    if(dictItem){
        popTypeAttr.value = JSON.parse(dictItem.attr);
        getAttrDictData(popTypeAttr.value);
    }
}
/** 设置弹窗类型属性表单配置 */
const setPopTypeAttr = ()=>{
    let resourceAttr:any = {}
    popAttr.value.forEach((item:any)=>{
        resourceAttr[item.name] = model.effect.popwindow.resourceAttr[item.name];
    })
    popTypeAttr.value.forEach((item:any)=>{
        resourceAttr[item.name] = item.type === 'number' ? Number(item.defaultValue) : item.defaultValue;
    })
    model.effect.popwindow.resourceAttr = resourceAttr;
}

/** 语音表单数据配置 */
const voiceAttr:any = ref([]);
const voiceOpt = async()=>{
    getVoiceAttr();
    setVoiceAttr();
}
/** 获取语音属性配置 */
const getVoiceAttr = ()=>{
    let dictItem:any = responseResultTypeData?.find((val:any)=>val.dictValue === 'voice');
    if(dictItem){
        voiceAttr.value = JSON.parse(dictItem.attr);
        getAttrDictData(voiceAttr.value);
    }
}
/** 设置语音属性表单配置 */
const setVoiceAttr = ()=>{
    model.effect.voice.resourceAttr = {};
    voiceAttr.value.forEach((item:any)=>{
        model.effect.voice.resourceAttr[item.name] = item.defaultValue;
    })
}

/** 表单参数复制 */
const handleCopy = (presentKey:any,receptionKey:any)=>{
    model[receptionKey] = jsonClone(model[presentKey]);
}
/** 动画表单参数复制 */
const handleCopyAnimation = (presentKey:any,receptionKey:any)=>{
    model.effect.animation.resourceAttr[receptionKey] = jsonClone( model.effect.animation.resourceAttr[presentKey]);
}

watch(visible, async () => {
    if (visible.value) {
        await getDictData();
        handleUpdateModelWhenEdit();
        mainObjectList.value = ueModelType.value.filter(val=>{
            return ['Input_device','tools'].includes(val.value);
        })
        targetObjectList.value = ueModelType.value.filter(val=>{
            return !['Input_device','tools'].includes(val.value);
        })
    }
});
</script>
<style lang="scss" scoped>
.transfer-wrap{
    display: flex;
    justify-content: space-between;
    .transfer-item{
        width: calc(50% - 38px);
    }
    .transfer-middle-btn{
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        .transfer-middle-border{
            height: calc(50% - 56px);
            border-right: 2px solid #EFEFF5;
            width: 1px;
        }
    }
}
.divider-title{
    font-size: 14px;
    margin-top: 0;
}
</style>
