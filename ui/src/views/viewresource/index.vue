<template>
    <div class="view-resource-wrap flex-col-stretch">
        <div class="view-resource-title" />
        <div class="view-resource-content">
            <div v-if="resourceType === 'textarea'"
                 v-html="richText"
                 style="width: 85%; height: 100%"
                 class="ql-editor"
            />
           <img v-else-if="resourceType === 'image'"
                 :src="imageUrl"
                 alt="Resource Image"
                 style="max-width: 85%; max-height: 100%; object-fit: contain;"
            />

            <video v-else-if="resourceType === 'video'"
                   :src="videoUrl"
                    controls
                    height="100%"
                    width="85%"
                    style="width: 85%; height: 100%"
            />
            <template v-else-if="resourceType === 'pdf'">
                <div class="controls">
                    <button @click="prevPage" :disabled="pageNum <= 1">上一页</button>
                    <span>页码: {{ pageNum }} / {{ pageCount }}</span>
                    <button @click="nextPage" :disabled="pageNum >= pageCount">下一页</button>
                </div>
                <canvas ref="pdfCanvas" />
            </template>
        </div>
        <div class="view-resource-btn">
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
</template>

<script setup lang="ts">
    import { ref, markRaw, shallowRef } from 'vue';
    import { getSopCardStepResult } from '@/service/api/viewResource';
    import { useRoute } from "vue-router";
    import * as pdfjsLib from 'pdfjs-dist/legacy/build/pdf.js';
    import '@vueup/vue-quill/dist/vue-quill.snow.css';

    console.log("success");
    const route = useRoute();
    const resourceType:any = ref(null);
    const richText = ref('');
    const imageUrl = ref('');
    const videoUrl = ref('');
    const pdfUrl = ref('');
    getSopCardStepResult(route.query.id).then((res:any)=>{
        if(res.response.data.data.resourceAttr.popwindowType === "document"){
            pdfUrl.value = res.response.data.data.resourcePath;
            resourceType.value = 'pdf';
            initPdf();
        }else if(res.response.data.data.resourceAttr.popwindowType === "image"){
            imageUrl.value = res.response.data.data.resourcePath;
            resourceType.value = 'image';
        }
        else if(res.response.data.data.resourceAttr.popwindowType === "video"){
            videoUrl.value = res.response.data.data.resourcePath;
            resourceType.value = 'video';
        }else if(res.response.data.data.resourceAttr.popwindowType === "textarea"){
            richText.value = res.response.data.data.resourceAttr.text;
            resourceType.value = 'textarea';
        }
    })

    const handleSubmit = (()=>{
            console.log('close');
    })

    // 配置 Worker
    pdfjsLib.GlobalWorkerOptions.workerSrc = new URL(
        'pdfjs-dist/build/pdf.worker.min.js',
        import.meta.url
    ).toString()
    const pdfCanvas = ref<HTMLCanvasElement | null>(null);
    const pdfDoc = shallowRef<pdfjsLib.PDFDocumentProxy | null>(null);
    const pageNum = ref(1);
    const pageCount = ref(0);
    const pageRendering = ref(false);
    const pageNumPending = ref<number | null>(null);
    const error = ref('');
    const scale = 1.5;
    /**
     * 渲染指定页码
     */
    const renderPage = async (num: number) => {
        if (!pdfDoc.value || !pdfCanvas.value) return;

        pageRendering.value = true;

        try {
            // 获取页面
            const page = await pdfDoc.value.getPage(num);

            const viewport = page.getViewport({ scale });
            const canvas = pdfCanvas.value;
            const context = canvas.getContext('2d');

            if (!context) {
                throw new Error('无法获取 Canvas Context');
            }

            canvas.height = viewport.height;
            canvas.width = viewport.width;

            // 渲染上下文
            const renderContext = {
                canvasContext: context,
                viewport: viewport,
            };

            const renderTask = page.render(renderContext);

            // 等待渲染完成
            await renderTask.promise;

            pageRendering.value = false;

            if (pageNumPending.value !== null) {
                // 如果有挂起的页面渲染请求，则渲染该页
                renderPage(pageNumPending.value);
                pageNumPending.value = null;
            }
        } catch (err: any) {
            console.error('渲染页面出错:', err);
            error.value = '渲染页面出错: ' + err.message;
            pageRendering.value = false;
        }
    };

    /**
     * 队列渲染：如果当前正在渲染，则挂起请求
     */
    const queueRenderPage = (num: number) => {
        if (pageRendering.value) {
            pageNumPending.value = num;
        } else {
            renderPage(num);
        }
    };

    /**
     * 上一页
     */
    const prevPage = () => {
        if (pageNum.value <= 1) return;
        pageNum.value--;
        queueRenderPage(pageNum.value);
    };

    /**
     * 下一页
     */
    const nextPage = () => {
        if (pageNum.value >= pageCount.value) return;
        pageNum.value++;
        queueRenderPage(pageNum.value);
    };

    /**
     * 初始化加载 PDF
     */
    const initPdf = async () => {
        try {
            error.value = '';
            console.log('正在加载 PDF:', pdfUrl.value);
            console.log('PDFJS Version:', pdfjsLib.version);

            const loadingTask = pdfjsLib.getDocument(pdfUrl.value);

            const doc = await loadingTask.promise;
            pdfDoc.value = markRaw(doc);
            pageCount.value = doc.numPages;

            console.log('PDF 加载成功，总页数:', pageCount.value);

            // 渲染第一页
            renderPage(pageNum.value);
        } catch (err: any) {
            console.error('加载 PDF 出错:', err);
            error.value = '加载 PDF 出错: ' + err.message;
        }
    };
</script>

<style lang="scss">
 :deep(#toolbar){
     display: none;
 }
</style>
<style scoped lang="scss">
    .view-resource-wrap{
        width: 100%;
        height: 100vh;
        padding: 0;
        display: flex;
        background-color: transparent !important;
        background-image: url("@/assets/imgs/view_resource_bg.png");
        background-position: center;
        background-size: 100% 100%;
        background-repeat: no-repeat;
        .view-resource-title{
            height: 100px;
            color: #fff;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        .view-resource-content{
            height: calc(100% - 140px);
            position: relative;
            display: flex;
            justify-content: center;
            padding-top: 16px;
            :deep(.ql-editor) {
                color: #FFFFFF;
            }
            :deep(.vue-pdf-embed > div){
                width: 100%;
                height: 100%;
            }
            :deep(.vue-pdf-embed__page){
                width: 100%;
                height: 100%;
                overflow-x: hidden;
                overflow-y: scroll;
            }
            .controls {
                display: flex;
                gap: 15px;
                align-items: center;
                color: #FFFFFF;
                position: absolute;
                right: 2%;
                bottom: 0;
            }

            .controls button {
                padding: 8px 16px;
                cursor: pointer;
                background-color: #1890ff;
                color: white;
                border: none;
                border-radius: 4px;
            }

            .controls button:disabled {
                background-color: #ccc;
                cursor: not-allowed;
            }
        }
        .view-resource-btn{
            display:flex;
            align-items:center;
            justify-content: center;
            padding:16px 0 8px;
            :deep(.submit-btn){
                width: 120px;
                background-position: center;
                background-size: 100% 100%;
                background-repeat: no-repeat;
                color: #fff;
                background-image: url("@/assets/imgs/topic/submit_bg.png");
            }
        }
    }
</style>
