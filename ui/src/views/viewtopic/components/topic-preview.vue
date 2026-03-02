<template>
    <!-- 预览卡片：复用 viewtopic 的视觉结构，仅展示，不含提交/取消按钮 -->
    <div class="topic-card">
        <!-- 标题：题目名称 -->
        <div class="topic-card-title">{{ getQuestionTypeLabel(topicData?.questionType) }}</div>
        <!-- 题干：富文本渲染 -->
        <div class="topic-card-topic">
            <!-- 使用 v-html 渲染富文本；内容来源为受控编辑数据 -->
            <div class="topic-wrap" v-html="topicData?.questionDescription"/>
        </div>
        <!-- 选项区：按题型分类渲染 -->
        <div :class="{ 'is-horizontal': topicData?.questionType === 'content_select' }" class="topic-card-option">
            <template v-if="topicData?.questionType === 'content_select'">
                <div v-for="song in topicData?.questionItemBos || []" :key="song.id ?? song.itemTag"
                     class="content-item"
                >
                    <div class="content-item-editor" :class="selectedValue === song.id ? 'is-checked' : ''" @click="clickContentItem(song.id)">
                        <span class="ql-editor"
                              v-html="song.itemDescription"
                        />
                    </div>
                </div>
            </template>
            <!-- 预览为禁用态，避免误操作 -->
            <n-radio-group v-else v-model:value="selectedValue" :disabled="isDisabled" name="previewRadioGroup">
                <n-space>
                    <!-- 遍历选项；优先使用后端 id，其次使用 itemTag 作为 key/value -->
                    <n-radio
                        v-for="song in topicData?.questionItemBos || []"
                        :key="song.id ?? song.itemTag"
                        :value="song.id ?? song.itemTag"
                    >
                        <!-- 判断题：仅显示标签（正确/错误） -->
                        <span v-if="topicData?.questionType === 'judge'">
                            {{ song.itemTag }}
                        </span>
                        <!-- 单选/步骤/交互型（opt）：显示标签 + 富文本描述 -->
                        <span v-else-if="['select', 'step', 'opt'].includes(topicData?.questionType)" class="option-row">
                            <span class="option-tag">{{ song.itemTag }}</span>
                            <!-- 富文本内容需要 quill 样式，类名保持与页面一致 -->
                            <span class="ql-editor" v-html="song.itemDescription"/>
                        </span>
                        <!-- 富文本选中型：标签 + 内容块（支持更复杂的布局） -->
                        <span v-else-if="topicData?.questionType === 'content_select'" class="inline-option">
                            <span class="option-tag">{{ song.itemTag }}</span>
                            <span class="ql-editor" v-html="song.itemDescription"/>
                        </span>
                    </n-radio>
                </n-space>
            </n-radio-group>
        </div>
        <!-- 底部插槽：用于放置操作按钮等 -->
        <slot name="footer"></slot>
    </div>
    <!-- 注：预览组件不包含按钮区（topic-card-btn），由调用方页面/抽屉自行提供 -->
</template>

<script lang="ts" setup>
/**
 * TopicPreview 预览组件
 * - 以正在编辑的题目数据进行展示渲染
 * - 仅展示用，不包含交互提交逻辑
 */
import {computed} from 'vue';
import '@vueup/vue-quill/dist/vue-quill.snow.css';
import {useDict} from "@/hooks/business/dict";

const {options: questionTypeOptions} = useDict('question_type');
console.log('questionTypeOptions========>>>',questionTypeOptions);
const props = defineProps<{
    topicData: any; // 使用与编辑抽屉 model 相同结构
    isDisabled?: boolean; // 是否禁用单选（预览场景一般为 true）
    modelValue?: any; // 选中的值
}>();

const emit = defineEmits<{
    (e: 'update:modelValue', value: any): void;
}>();

const selectedValue = computed({
    get: () => props.modelValue,
    set: (val) => emit('update:modelValue', val)
});

const getQuestionTypeLabel = (type:any)=>{
    let item:any = questionTypeOptions.value.find(item => item.value === type);
    if(item)return item.label;
}

const clickContentItem = (id:any)=>{
    if (props.isDisabled) return;
    selectedValue.value = id;
}
</script>

<style lang="scss" scoped>
/* 复用 viewtopic 的样式规范，确保视觉一致 */
.topic-card {
    width: 100%;
    min-width: 350px;
    min-height: 400px;
    background-image: url('@/assets/svg-icon/view_bg_2.svg');
    background-position: center;
    background-size: 100% 100%;
    background-repeat: no-repeat;
    padding: 1.5% 2.5% 2.5%;
    display: flex;
    flex-direction: column;

    .topic-card-title {
        font-size: 18px;
        font-weight: 700;
        color: #fff;
        height: 40px;
        padding-left: 28px;
        display: flex;
        align-items: center;
        justify-content: space-between;
        background-image: url('@/assets/svg-icon/view_title_bg.svg');
        background-position: center;
        background-size: 100% 100%;
        background-repeat: no-repeat;
    }

    .topic-card-topic {
        padding-left: 48px;
        margin-top: 40px;

        .topic-wrap {
            color: #ffffff;
            border-left: 4px solid #4080f7;
            padding-left: 8px;
        }
    }

    .topic-card-option {
        padding-left: 72px;
        padding-top: 16px;
        flex: 1;

        :deep(.n-radio__label) {
            color: #ffffff;
        }

        :deep(.n-space) {
            flex-direction: column !important;
        }

        &.is-horizontal {
            display: flex;
            :deep(.n-space) {
                flex-direction: row !important;
                flex-wrap: wrap;
            }

            :deep(.n-radio) {
                margin-right: 24px;
                margin-bottom: 16px;
            }
        }

        .option-tag {
            font-weight: bold;
            margin-right: 8px;
        }

        .option-row {
            display: flex;
            align-items: center;
        }
        .content-item{
            display: inline-block;
            color: #FFFFFF;
            margin-right: 16px;
            .content-item-tag{
                font-weight: bold;
                margin-right: 8px;
                vertical-align: top;
            }
            .content-item-editor{
                display: inline-block;
                padding: 8px;
                border: 2px solid transparent;

                //修复content_select类型下，富文本内容包含图片时，图片自带 style="cursor: nwse-resize;" 导致鼠标无法变成手型的问题
                :deep(img) {
                    cursor: pointer !important;
                }
            }
            .is-checked{
                border-color: #18a058 !important;
                position: relative;
                &::before{
                    content: '';
                    position: absolute;
                    bottom: 0;
                    right: 0;
                    width: 0;
                    height: 0;
                    border-top: 8px solid transparent;
                    border-left: 8px solid transparent;
                    border-right: 8px solid #18a058;
                    border-bottom: 8px solid #18a058;
                }
                &::after{
                    content: '✓';
                    position: absolute;
                    bottom: -1px;
                    right: 0;
                    font-size: 12px;
                    color: white;
                }
            }
        }
        .ql-editor {
            padding: 0;
            min-height: auto;

            :deep(p) {
                margin: 0;
            }
        }

        .inline-option {
            display: inline-flex;
            align-items: flex-start;
            gap: 8px;
        }
    }
}
</style>
