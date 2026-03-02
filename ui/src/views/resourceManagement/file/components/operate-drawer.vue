<template>
    <NDrawer v-model:show="visible" :title="title" :width="1300" class="max-w-90%" display-directive="show">
        <NDrawerContent :native-scrollbar="false" :title="title" closable>
            <div class="edit-preview-wrap">
                <div class="preview-col">
                    <div class="file-card">
                        <template v-if="model.assetType === 'image' && fileUrl">
                            <n-image
                                :src="fileUrl"
                                height="90%"
                                style="width: 90%"
                            />
                        </template>
                        <template v-else-if="model.assetType === 'video' && fileUrl">
                            <video :src="fileUrl"
                                   controls
                                   height="85%"
                                   width="90%"
                                   style="width: 90%; height: 85%"
                            />
                        </template>
                        <template v-else-if="model.assetType === 'document' && fileUrl">
                            <div class="controls">
                                <button @click="prevPage" :disabled="pageNum <= 1">上一页</button>
                                <span>页码: {{ pageNum }} / {{ pageCount }}</span>
                                <button @click="nextPage" :disabled="pageNum >= pageCount">下一页</button>
                            </div>
                            <canvas ref="pdfCanvas" style="width: 95%;height: 90%"/>
                        </template>
                    </div>
                </div>
                <div class="edit-col">
                    <NForm ref="formRef" :model="model" :rules="rules">
                        <NFormItem label="文件类型" path="assetType">
                            <NSelect
                                v-model:value="model.assetType"
                                :options="fileAssetTypeOptions"
                                placeholder="请选择模型类别"
                                :disabled="!!props?.assetType"
                            />
                        </NFormItem>
                        <NFormItem label="附件" path="file">
                            <div class="upload-wrap">
                                <n-upload
                                    :accept="AcceptType[model.assetType]"
                                    v-model:file-list="model.file"
                                    :default-upload="false"
                                    :max="1"
                                    :show-file-list="false"
                                    @update:file-list="fileListChange"
                                >
                                    <n-button>上传文件</n-button>
                                </n-upload>
                                <br/>
                                <div v-for="item in model.file" class="file-list-wrap">
                                    <span class="img-name">{{item.name}}</span>
                                    <n-button quaternary circle size="small" class="del-btn" @click="handleRemoveFile">
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
import {computed, markRaw, reactive, ref, shallowRef, watch} from 'vue';
import {useFormRules, useNaiveForm} from '@/hooks/common/form';
import {useDict} from '@/hooks/business/dict';
import {$t} from '@/locales';
import { addFile, editFile } from '@/service/api/resourceManagement/file';
import * as pdfjsLib from 'pdfjs-dist/legacy/build/pdf.js';
import '@vueup/vue-quill/dist/vue-quill.snow.css';

defineOptions({
    name: 'ElementOperateDrawer'
});

const AcceptType:any = {
    document:'.pdf',
    image:'.jpg,.jpeg,.png,.gif,.bmp,.webp',
    video:'.mp4'
}
interface Props {
    /** the type of operation */
    operateType: NaiveUI.TableOperateType;
    /** the edit row data */
    rowData?: Api.Business.Element | null;
    assetType?: string
}

const props = defineProps<Props>();

interface Emits {
    (e: 'submitted'): void;
}

const emit = defineEmits<Emits>();

const visible = defineModel<boolean>('visible', {
    default: false
});

const {options: fileAssetTypeOptions} = useDict('file_asset_type');

const {formRef, validate, restoreValidation} = useNaiveForm();
const {createRequiredRule} = useFormRules();

const title = computed(() => {
    const titles: Record<NaiveUI.TableOperateType, string> = {
        add: '新增文件资源',
        edit: '编辑文件资源'
    };
    return titles[props.operateType];
});

const model:any = reactive(createDefaultModel());

function createDefaultModel() {
    return {
        id:'',
        assetType:'document',
        file: []
    };
}

const rules = {
    assetType: createRequiredRule('文件类型不能为空'),
    file: createRequiredRule('文件不能为空')
};
const fileUrl = ref('');
function handleUpdateModelWhenEdit() {
    fileUrl.value = '';
    if (props.operateType === 'add') {
        Object.assign(model, createDefaultModel());
        if(props?.assetType){
            model.assetType = props.assetType;
        }
        return;
    }

    if (props.operateType === 'edit' && props.rowData) {
        Object.assign(model, props.rowData);
        fileUrl.value = props.rowData.path;
        console.log(fileUrl.value);
        if(model.assetType === 'document'){
            initPdf();
        }
    }
}

function closeDrawer() {
    visible.value = false;
}

async function handleSubmit() {
    await validate();
    const formData = new FormData();
    formData.append('assetType', model.assetType ?? '');
    formData.append('file', model.file[0].file ?? '');
    if (props.operateType === 'add') {
        const {error} = await addFile(formData);
        if (error) return;
    }
    if (props.operateType === 'edit') {
        formData.append('id', model.id ?? '');
        const {error} = await editFile(formData);
        if (error) return;
    }

    window.$message?.success($t('common.updateSuccess'));
    closeDrawer();
    emit('submitted');
}

const handleRemoveFile = ()=>{
    model.file.length = 0;
    fileUrl.value = '';
}

const fileListChange = (list:any)=>{
    console.log('list============>>>',list);
    if(list.length === 0){
        fileUrl.value = '';
    }else{
        fileUrl.value = URL.createObjectURL(list[0].file);
        if(model.assetType === 'document'){
            initPdf();
        }
    }
}

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
        console.log('正在加载 PDF:', fileUrl.value);
        console.log('PDFJS Version:', pdfjsLib.version);

        const loadingTask = pdfjsLib.getDocument(fileUrl.value);

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
watch(visible, () => {
    if (visible.value) {
        handleUpdateModelWhenEdit();
        restoreValidation();
    }
});

</script>

<style scoped lang="scss">
.upload-wrap{
    width: 100%;
    .file-list-wrap{
        width: 100%;
        padding: 8px;
        display: flex;
        align-items: center;
        background: #F3F3F5;
        position: relative;
        border-radius: 5px;
        .del-btn{
            float: right;
            position: absolute;
            right: 16px;
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
        padding: 8px 0;
        .file-card{
            width: 100%;
            min-width: 350px;
            min-height: 400px;
            background-image: url('@/assets/svg-icon/view_bg_2.svg');
            background-position: center;
            background-size: 100% 100%;
            background-repeat: no-repeat;
            padding: 3% 2.5% 2.5%;
            display: flex;
            align-items: center;
            justify-content: center;
            position: relative;
            .controls {
                display: flex;
                gap: 15px;
                align-items: center;
                position: absolute;
                right: 3%;
                bottom: 2%;
            }
            .controls button {
                font-size: 12px;
                padding: 4px 8px;
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
    }
}
</style>
