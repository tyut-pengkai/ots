<template>
    <div class="view-content-wrap flex-col-stretch">
        <div class="content-card">
            <div class="topic-card-title">
                {{title}}
            </div>
            <div class="card-option ql-editor">
                <span v-html="htmlVal"/>
            </div>
            <div class="card-btn">
                <n-button strong
                          secondary
                          type="primary"
                          size="large"
                          @click="handleSubmit"
                          class="submit-btn"
                >
                    确 定
                </n-button>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useRoute } from 'vue-router';
import { getQuestionInfo } from '@/service/api/business/question';
import '@vueup/vue-quill/dist/vue-quill.snow.css';

console.log("success");
const route = useRoute();
const title = ref('');
const htmlVal = ref('');
const getData = ()=>{
    getQuestionInfo(route.query.id).then((res:any)=>{
        console.log(res.data);
        title.value = res.data.questionName;
        let item = res.data.questionItemBos.find((val:any)=>val.id === route.query.optionId);
        htmlVal.value = item.itemContentSelect;
    })
}
getData();

const handleSubmit = ()=>{
    console.log('close');
    window.close();
}
</script>

<style scoped lang="scss">
.view-content-wrap {
    width: 100%;
    height: 100vh;
    padding: 0;
    display: flex;
    align-items: center;
    justify-content: center;
    background-color: transparent !important;
    .content-card{
        width: 100%;
        height: 100%;
        // width: 40%;
        // min-width: 350px;
        // min-height: 400px;
        background-image: url("@/assets/svg-icon/view_bg_2.svg");
        background-position: center;
        background-size: 100% 100%;
        background-repeat: no-repeat;
        padding: 14px 16px 16px 20px;
        color: #FFFFFF;
        display: flex;
        flex-direction: column;
        .topic-card-title{
            font-size: 18px;
            font-weight: 700;
            color: #fff;
            height: 48px;
            padding-left: 32px;
            display: flex;
            align-items: center;
            justify-content: space-between;
            background-image: url("@/assets/svg-icon/view_title_bg.svg");
            background-position: center;
            background-size: 100% 100%;
            background-repeat: no-repeat;
        }
    }
    .card-option{
        flex: 1;
        padding: 16px 24px;
    }
    .card-btn{
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
        :deep(.submit-btn){
            background-image: url("@/assets/imgs/topic/submit_bg.png");
        }
    }
}
</style>
