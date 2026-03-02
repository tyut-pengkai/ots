<template>
    <div class="view-overview-wrap flex-col-stretch">
        <div class="content-card">
            <div class="topic-card-title">
                <span>
                    {{title}}
                </span>
                <div class="close-btn">
                    <n-image
                        width="24"
                        height="24"
                        :src="closeImgUrl"
                        preview-disabled
                        @click="handleClose"
                    />
                </div>
            </div>
            <div class="card-option ql-editor">
                <span v-html="htmlVal"/>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useRoute } from 'vue-router';
import { getElementInfo } from '@/service/api/business/element';
import closeImgUrl from "@/assets/imgs/close_icon.png";
import '@vueup/vue-quill/dist/vue-quill.snow.css';

console.log("success");
const route = useRoute();
const htmlVal = ref('');
const title = ref('');
const getData = ()=>{
    getElementInfo(route.query.id).then((res:any)=>{
        title.value = res.data.name;
        htmlVal.value = res.data.description;
    })
}
getData();
const handleClose = ()=>{
    console.log('close');
    window.close();
}
</script>

<style scoped lang="scss">
.view-overview-wrap {
    width: 100%;
    height: 100vh;
    // padding: 16px;
    padding: 0;
    display: flex;
    align-items: center;
    justify-content: center;
    background-color: transparent !important;
    .content-card{
        // width: 40%;
        width: 100%;
        // min-width: 350px;
        // min-height: 400px;
        height: 100%;
        // background-image: url("@/assets/svg-icon/view_bg_1.svg");
        // background-position: center;
        // background-size: 100% 100%;
        // background-repeat: no-repeat;
        // background-color: #101014;
        background-color: rgba(15, 38, 65, 0.9);
        border: 1px solid #4176ff;
        box-shadow: inset 0 0 20px rgba(65, 118, 255, 0.2);
        
        // Corners configuration
        --corner-len: 12px;
        --corner-width: 2px;
        --corner-color: #ffaa00;
        
        background-image: 
            linear-gradient(to right, var(--corner-color), var(--corner-color)), 
            linear-gradient(to bottom, var(--corner-color), var(--corner-color)),
            linear-gradient(to left, var(--corner-color), var(--corner-color)), 
            linear-gradient(to bottom, var(--corner-color), var(--corner-color)),
            linear-gradient(to left, var(--corner-color), var(--corner-color)), 
            linear-gradient(to top, var(--corner-color), var(--corner-color)),
            linear-gradient(to right, var(--corner-color), var(--corner-color)), 
            linear-gradient(to top, var(--corner-color), var(--corner-color));
            
        background-position: 
            0 0, 0 0, 
            100% 0, 100% 0, 
            100% 100%, 100% 100%, 
            0 100%, 0 100%;
            
        background-size: 
            var(--corner-len) var(--corner-width), var(--corner-width) var(--corner-len),
            var(--corner-len) var(--corner-width), var(--corner-width) var(--corner-len),
            var(--corner-len) var(--corner-width), var(--corner-width) var(--corner-len),
            var(--corner-len) var(--corner-width), var(--corner-width) var(--corner-len);
            
        background-repeat: no-repeat;

        // padding: 8px 16px 16px 10px;
        padding: 0;
        color: #FFFFFF;
        display: flex;
        flex-direction: column;
        .topic-card-title{
            font-size: 18px;
            font-weight: 700;
            color: #fff;
            height: 48px;
            padding-left: 28px;
            padding-right: 16px;
            display: flex;
            align-items: center;
            justify-content: space-between;
            background-image: url("@/assets/svg-icon/view_title_bg.svg");
            // background: linear-gradient(90deg, rgba(30, 144, 255, 0.2) 0%, transparent 100%);
            // border-bottom: 1px solid rgba(65, 118, 255, 0.3);
            margin-top: 3px;
            margin-left: 3px;
            background-position: center;
            background-size: 100% 100%;
            background-repeat: no-repeat;
            .close-btn{
                //border:2px dashed;
                width: 24px;
                height: 24px;
                display: flex;
                align-items: center;
                justify-content: center;
                cursor: pointer;
                transition: all 0.3s;
                border-radius: 4px;
                &:hover {
                    background-color: rgba(255, 255, 255, 0.2);
                }
            }
        }
        .card-option{
            flex: 1;
            padding: 16px 24px;
            overflow-y: auto;
        }
    }
}
</style>
