<template>
    <div class="view-topic-wrap flex-col-stretch">
        <div class="topic-card-wrapper">
            <!-- 原静态展示已注释，改为复用 TopicPreview 组件渲染 -->
            <!--
            <div class="topic-card-title">{{topicData?.questionName}}</div>
            <div class="topic-card-topic" >
                <div class="topic-wrap" v-html="topicData?.questionDescription" />
            </div>
            <div class="topic-card-option">
                <n-radio-group v-model:value="optionVal" name="radioGroup" :disabled="isRadioGroup">
                    <n-space>
                        <n-radio v-for="song in topicData?.questionItemBos" :key="song.id" :value="song.id">
                            <span v-if="topicData?.questionType === 'judge'">
                                {{song.itemTag}}
                            </span>
                            <span v-if="['select','step'].includes(topicData?.questionType)" style="display: flex">
                                <span class="option-tag">{{song.itemTag}}</span>
                                <span class="ql-editor" v-html="song.itemDescription" />
                            </span>

                            <span v-else-if="topicData?.questionType === 'content_select'" class="inline-option">
                                <span class="option-tag">{{song.itemTag}}</span>
                                <div v-html="song.itemDescription" />
                            </span>
                        </n-radio>
                    </n-space>
                </n-radio-group>
            </div>
            -->
            <TopicPreview
                :topic-data="topicData"
                :is-disabled="isRadioGroup"
                v-model="optionVal"
            >
                <template #footer>
                    <div class="topic-card-btn">
                        <!-- <n-popconfirm
                            @positive-click="handlePositiveClick"
                            class="topic-popover"
                        >
                            <template #trigger>
                                <n-button strong
                                          secondary
                                          type="primary"
                                          size="large"
                                          class="cancel-btn"
                                >
                                    取 消
                                </n-button>
                            </template>
                            确定关闭当前页面吗？
                        </n-popconfirm> -->
                        <n-button strong
                                  secondary
                                  type="primary"
                                  size="large"
                                  :disabled="isRadioGroup"
                                  @click="handleSubmit"
                                  :loading="isBtnLoading"
                                  class="submit-btn"
                        >
                            确 定
                        </n-button>
                    </div>
                </template>
            </TopicPreview>
        </div>
    </div>
</template>
<script setup lang="tsx">
import { getQuestionInfo } from '@/service/api/business/question';
import {useRoute, useRouter} from 'vue-router';
import { ref } from 'vue';
import { submitAnswer } from '@/service/api/business/question';
import '@vueup/vue-quill/dist/vue-quill.snow.css';
import TopicPreview from './components/topic-preview.vue';

console.log("success");
const route = useRoute();
const router = useRouter();
// isView=1 或带有 previewKey 时禁用交互（预览态）
const isRadioGroup:any = ref(route.query.isView === '1' || !!route.query.previewKey);
const topicData:any = ref();
const optionVal = ref();
// 原 getData 方法仅拉取接口，现根据 previewKey 优先读取本地预览数据
// const getData = ()=>{
//     console.log(route);
//     getQuestionInfo(route.query.id).then(res=>{
//         topicData.value = res.data;
//     })
// }
const getData = ()=>{
    const previewKey = route.query.previewKey as string | undefined;
    if (previewKey) {
        try {
            const raw = sessionStorage.getItem(previewKey);
            if (raw) {
                topicData.value = JSON.parse(raw);
                return;
            }
        } catch (e) {
            // 解析失败则回退到接口拉取
        }
    }
    getQuestionInfo(route.query.id).then(res=>{
        topicData.value = res.data;
    })
}
getData();

const isBtnLoading = ref(false);
const handleSubmit = ()=>{
    if(!optionVal.value) return window.$message?.warning('请选择选项！');
    console.log('data=======>>>',topicData.value);
    console.log('optionVal=======>>>',optionVal.value);
    isBtnLoading.value = true;
    submitAnswer({
        hscsoId:route.query.hscsoId,
        batchId:route.query.batchId,
        answer:optionVal.value
    }).then(res=>{
        isBtnLoading.value =false;
        if (topicData.value?.questionType !== 'content_select') console.log('close');
        let item = topicData.value.questionItemBos.find((val:any)=>val.id === optionVal.value);
        if(item.itemContentSelect){
            router.replace({
                path: '/viewContent',
                query:{
                    id: route.query.id,
                    optionId: item.id
                }
            })
        }
    }).catch(()=>{
        isBtnLoading.value =false;
    })
}
const handlePositiveClick = ()=>{
    console.log('close');
    window.close();
}
</script>
<style scoped lang="scss">
    .view-topic-wrap{
        width: 100%;
        height: 100vh;
        padding: 0;
        display: flex;
        align-items: center;
        justify-content: center;
        background-color: transparent !important;
        .topic-card-wrapper{
            width: 100%;
            height: 100%;
            // 移除背景与内边距，交由 TopicPreview 组件处理
            /*
            min-height: 400px;
            background-image: url("@/assets/imgs/view_bg_2.png");
            background-position: center;
            background-size: 100% 100%;
            background-repeat: no-repeat;
            padding: 14px 16px 16px 20px;
            display: flex;
            flex-direction: column;
            */

            // 强制 TopicPreview 占满容器
            :deep(.topic-card) {
                height: 100%;
            }

            // 按钮样式
            .topic-card-btn{
                display:flex;
                align-items:center;
                justify-content: center;
                padding:16px 0 8px;
                :deep(.n-button){
                    width: 120px;
                    background-position: center;
                    background-size: 100% 100%;
                    background-repeat: no-repeat;
                    color: #fff;
                }
                :deep(.cancel-btn){
                    background-image: url("@/assets/imgs/topic/cancel_bg.png");
                    margin-right: 32px;
                }
                :deep(.submit-btn){
                    background-image: url("@/assets/imgs/topic/submit_bg.png");
                }
            }
        }
    }
</style>
