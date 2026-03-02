<template>
    <div class="editor">
        <quill-editor
            ref="quillEditorRef"
            v-model:content="content"
            :options="options"
            :style="styles"
            contentType="html"
            @textChange="(e) => $emit('update:value', content)"
        />
    </div>
</template>

<script lang="ts" setup>
import { ref, computed, watch, nextTick, onMounted } from 'vue';
import { QuillEditor, Quill } from '@vueup/vue-quill';
import '@vueup/vue-quill/dist/vue-quill.snow.css';
import ImageResize from 'quill-image-resize';

// 注册插件
Quill.register('modules/imageResize', ImageResize);
const quillEditorRef:any = ref(null);
const props = defineProps({
    /* 编辑器的内容 */
    value: {
        type: String,
    },
    /* 高度 */
    height: {
        type: Number,
        default: null,
    },
    /* 最小高度 */
    minHeight: {
        type: Number,
        default: null,
    },
    /* 只读 */
    readOnly: {
        type: Boolean,
        default: false,
    },
    placeholder: {
        type: String,
        default: '请输入内容',
    }
});

const options:any = ref({
    theme: "snow",
    bounds: document.body,
    debug: "warn",
    modules: {
        // 工具栏配置
        toolbar: [
            ["bold", "italic", "underline", "strike"],       // 加粗 斜体 下划线 删除线
            ["blockquote", "code-block"],                    // 引用  代码块
            [{list: "ordered"}, {list: "bullet"}],       // 有序、无序列表
            [{indent: "-1"}, {indent: "+1"}],            // 缩进
            [{size: ["small", false, "large", "huge"]}],   // 字体大小
            [{header: [1, 2, 3, 4, 5, 6, false]}],         // 标题
            [{color: []}, {background: []}],             // 字体颜色、字体背景颜色
            [{align: []}],                                 // 对齐方式
            ["clean"],                                       // 清除文本格式
            ["link", "image", "video"]                       // 链接、图片、视频
        ],
        imageResize: { // 使用 imageResize 模块的配置选项（如果有的话）
            displaySize: true // 是否显示尺寸输入框，默认为 true（如果有的话）
        },
    },
    placeholder: props.placeholder,
    readOnly: props.readOnly
});

const styles = computed(() => {
    let style:any = {};
    if (props.minHeight) {
        style.minHeight = `${props.minHeight}px`;
    }
    if (props.height) {
        style.height = `${props.height}px`;
    }
    return style;
})

const content:any = ref(null);
watch(() => props.value, (v) => {
    if (v !== content.value) {
        if(v){
            content.value = v
        }else{
            nextTick(()=>{
                quillEditorRef.value.setHTML('');
            })
        }

    }
}, {immediate: true});

const titleConfig:any = {
    "ql-bold": "加粗" ,
    "ql-color": "颜色",
    "ql-font": "字体",
    "ql-code": "插入代码",
    "ql-italic": "斜体",
    "ql-link": "添加链接",
    "ql-background": "背景颜色",
    "ql-size": "字体大小",
    "ql-strike": "删除线",
    "ql-script": "上标/下标",
    "ql-underline": "下划线",
    "ql-blockquote": "引用",
    "ql-header": "标题",
    "ql-indent": "缩进",
    "ql-list": "列表",
    "ql-align": "文本对齐",
    "ql-direction": "文本方向",
    "ql-code-block": "代码块",
    "ql-formula": "公式",
    "ql-image": "图片",
    "ql-video": "视频",
    "ql-clean": "清除字体样式",
    "ql-upload": "文件"
};

nextTick(()=>{
    const oToolBar:any = document.querySelector(".ql-toolbar"),
        aButton:any = oToolBar.querySelectorAll("button"),
        aSelect:any = oToolBar.querySelectorAll("select");
    aButton.forEach(function(item:any) {
        if (item.className === "ql-script") {
            item.value === "sub" ? (item.title = "下标") : (item.title = "上标");
        } else if (item.className === "ql-indent") {
            item.value === "+1"
                ? (item.title = "向右缩进")
                : (item.title = "向左缩进");
        } else {
            item.title = titleConfig[item.classList[0]];
        }
    });
    aSelect.forEach(function(item:any) {
        item.parentNode.title = titleConfig[item.classList[0]];
    });
})
</script>

<style>
.editor, .ql-toolbar {
    white-space: pre-wrap !important;
    line-height: normal !important;
}

.quill-img {
    display: none;
}

.ql-snow .ql-tooltip[data-mode="link"]::before {
    content: "请输入链接地址:";
}

.ql-snow .ql-tooltip.ql-editing a.ql-action::after {
    border-right: 0px;
    content: "保存";
    padding-right: 0px;
}

.ql-snow .ql-tooltip[data-mode="video"]::before {
    content: "请输入视频地址:";
}

.ql-snow .ql-picker.ql-size .ql-picker-label::before,
.ql-snow .ql-picker.ql-size .ql-picker-item::before {
    content: "14px";
}

.ql-snow .ql-picker.ql-size .ql-picker-label[data-value="small"]::before,
.ql-snow .ql-picker.ql-size .ql-picker-item[data-value="small"]::before {
    content: "10px";
}

.ql-snow .ql-picker.ql-size .ql-picker-label[data-value="large"]::before,
.ql-snow .ql-picker.ql-size .ql-picker-item[data-value="large"]::before {
    content: "18px";
}

.ql-snow .ql-picker.ql-size .ql-picker-label[data-value="huge"]::before,
.ql-snow .ql-picker.ql-size .ql-picker-item[data-value="huge"]::before {
    content: "32px";
}

.ql-snow .ql-picker.ql-header .ql-picker-label::before,
.ql-snow .ql-picker.ql-header .ql-picker-item::before {
    content: "文本";
}

.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="1"]::before,
.ql-snow .ql-picker.ql-header .ql-picker-item[data-value="1"]::before {
    content: "标题1";
}

.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="2"]::before,
.ql-snow .ql-picker.ql-header .ql-picker-item[data-value="2"]::before {
    content: "标题2";
}

.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="3"]::before,
.ql-snow .ql-picker.ql-header .ql-picker-item[data-value="3"]::before {
    content: "标题3";
}

.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="4"]::before,
.ql-snow .ql-picker.ql-header .ql-picker-item[data-value="4"]::before {
    content: "标题4";
}

.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="5"]::before,
.ql-snow .ql-picker.ql-header .ql-picker-item[data-value="5"]::before {
    content: "标题5";
}

.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="6"]::before,
.ql-snow .ql-picker.ql-header .ql-picker-item[data-value="6"]::before {
    content: "标题6";
}

.ql-snow .ql-picker.ql-font .ql-picker-label::before,
.ql-snow .ql-picker.ql-font .ql-picker-item::before {
    content: "标准字体";
}

.ql-snow .ql-picker.ql-font .ql-picker-label[data-value="serif"]::before,
.ql-snow .ql-picker.ql-font .ql-picker-item[data-value="serif"]::before {
    content: "衬线字体";
}

.ql-snow .ql-picker.ql-font .ql-picker-label[data-value="monospace"]::before,
.ql-snow .ql-picker.ql-font .ql-picker-item[data-value="monospace"]::before {
    content: "等宽字体";
}
</style>
