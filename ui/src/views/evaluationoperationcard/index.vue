<template>
    <div class="min-h-500px flex-col-stretch gap-16px overflow-hidden lt-sm:overflow-auto">
        <EvaluationSearch v-model:model="searchParams" @reset="resetSearchParams" @search="getDataByPage"/>
        <!-- <NCard title="考核操作卡管理" :bordered="false" size="small" class="sm:flex-1-hidden card-wrapper"> -->
        <NCard :title="$t('evaluationOperationCard.title')" :bordered="false" size="small" class="sm:flex-1-hidden card-wrapper">
            <template #header-extra>
                <NSpace wrap justify="end" class="lt-sm:w-200px">
                    <NButton size="small" ghost type="primary" @click="handleSopCardCopy('train')">
                        <template #icon>
                            <icon-material-symbols:content-copy-outline-rounded class="text-icon" />
                        </template>
                        <!-- 复制培训操作卡 -->
                        {{ $t('evaluationOperationCard.copyTrainCard') }}
                    </NButton>

                    <NButton size="small" ghost type="primary" @click="handleAdd">
                        <template #icon>
                            <icon-material-symbols:add class="text-icon" />
                        </template>
                        {{ $t('common.add') }}
                    </NButton>
                    <NButton size="small" ghost type="primary" @click="handleSopCardCopy('exam')" :disabled="!currentRow">
                        <template #icon>
                            <icon-material-symbols:content-copy-outline-rounded class="text-icon" />
                        </template>
                        <!-- 复制 -->
                        {{ $t('evaluationOperationCard.copy') }}
                    </NButton>
                    <NButton size="small" @click="refresh">
                        <template #icon>
                            <icon-material-symbols:refresh-rounded class="text-icon" :class="{ 'animate-spin': loading }" />
                        </template>
                        {{ $t('common.refresh') }}
                    </NButton>
                </NSpace>
            </template>
            <n-spin :show="loading" style="height: 100%">
                <div class="content-wrap">
                    <div class="table-wrap">
                        <el-table :data="tableData"
                                  style="width: 100%;position: absolute"
                                  height="100%"
                                  border
                                  preserve-expanded-content
                                  row-key="id"
                                  @expand-change="stepOneExpandChange"
                                  :expand-row-keys="stepOneExpandRowKeys"
                                  :class="themeStore.darkMode ? 'dark-table' : 'light-table'"
                                  highlight-current-row
                                  @current-change="handleCurrentChange"
                        >
                            <el-table-column type="expand">
                                <template #default="scope">
                                    <div class="stepTwo-content-wrap">
                                        <div class="stepTwo-table">
                                            <el-table :data="scope.row.childTableData"
                                                      style="width: 100%;"
                                                      height="100%"
                                                      border
                                                      preserve-expanded-content
                                                      row-key="id"
                                                      @expand-change="stepTwoExpandChange"
                                                      :expand-row-keys="stepTwoExpandRowKeys"
                                                      :class="themeStore.darkMode ? 'dark-table' : 'light-table'"
                                            >
                                                <el-table-column type="expand">
                                                    <template #default="scope">
                                                        <div class="stepThree-content-wrap">
                                                            <div class="stepThree-table">
                                                            <el-table :data="scope.row.childTableData"
                                                                      style="width: 100%;"
                                                                      height="100%"
                                                                      border
                                                                      preserve-expanded-content
                                                                      row-key="id"
                                                                      :class="themeStore.darkMode ? 'dark-table' : 'light-table'"
                                                            >
                                                                <el-table-column type="index" :label="$t('common.index')" align="center" width="70" />
                                                                <!-- <el-table-column prop="optContent" label="操作内容" align="center"/> -->
                                                                <el-table-column prop="optContent" :label="$t('evaluationOperationCard.operationContent')" align="center"/>
                                                                <!-- <el-table-column prop="optCommentary" label="操作解说" align="center" show-overflow-tooltip/> -->
                                                                <el-table-column prop="optCommentary" :label="$t('evaluationOperationCard.operationCommentary')" align="center" show-overflow-tooltip/>
                                                                <!-- <el-table-column prop="indexing" label="排序号" align="center" width="80" /> -->
                                                                <el-table-column prop="indexing" :label="$t('evaluationOperationCard.sortOrder')" align="center" width="80" />
                                                                <!-- <el-table-column label="操作" align="center" width="200"> -->
                                                                <el-table-column :label="$t('common.action')" align="center" width="200">
                                                                    <template #default="scope">
                                                                        <ButtonIcon
                                                                            text
                                                                            type="primary"
                                                                            icon="material-symbols:drive-file-rename-outline-outline"
                                                                            :tooltipContent="$t('common.edit')"
                                                                            @click="handleStepThreeEdit(scope.row.id)"
                                                                        />
                                                                        <NDivider vertical />
                                                                        <ButtonIcon
                                                                            text
                                                                            type="primary"
                                                                            icon="material-symbols:content-copy-outline-rounded"
                                                                            :tooltipContent="$t('evaluationOperationCard.copy')"
                                                                            @click="handleStepThreeCopy(scope.row.id)"
                                                                        />
                                                                        <NDivider vertical />
                                                                        <ButtonIcon
                                                                            text
                                                                            type="primary"
                                                                            icon="material-symbols:edit-arrow-up-outline-rounded"
                                                                            :tooltipContent="$t('evaluationOperationCard.insertUp')"
                                                                            @click="handleStepThreeMoveUp(scope.row.id)"
                                                                        />
                                                                        <NDivider vertical />
                                                                        <ButtonIcon
                                                                            text
                                                                            type="primary"
                                                                            icon="material-symbols:edit-arrow-down-outline-rounded"
                                                                            :tooltipContent="$t('evaluationOperationCard.insertDown')"
                                                                            @click="handleStepThreeMoveDown(scope.row.id)"
                                                                        />
                                                                        <NDivider vertical />
                                                                        <ButtonIcon
                                                                            text
                                                                            type="error"
                                                                            icon="material-symbols:delete-outline"
                                                                            :tooltipContent="$t('common.delete')"
                                                                            :popconfirmContent="$t('common.confirmDelete')"
                                                                            @positiveClick="handleStepThreeDelete(scope.row.id)"
                                                                        />
                                                                    </template>
                                                                </el-table-column>
                                                            </el-table>
                                                        </div>
                                                            <div class="stepTwo-pagination">
                                                                <n-pagination
                                                                    ref="paginationThree"
                                                                    :item-count="scope.row.childDataTotal"
                                                                    :page-sizes="[10, 20, 30, 40]"
                                                                    show-size-picker
                                                                    v-model:page="scope.row.childParams.pageNum"
                                                                    v-model:page-size="scope.row.childParams.pageSize"
                                                                    :on-update:page="(page)=>{onUpdatePageThree(page,scope.row)}"
                                                                    :on-update:page-size="(pageSize)=>{onUpdatePageSizeThree(pageSize,scope.row)}"
                                                                >
                                                                    <template #prefix>
                                                                        <!-- 共 {{ scope.row.childDataTotal }} 条 -->
                                                                        {{$t('datatable.itemCount', { total: scope.row.childDataTotal })}}
                                                                    </template>
                                                                </n-pagination>
                                                            </div>
                                                        </div>
                                                    </template>
                                                </el-table-column>
                                                <el-table-column type="index" :label="$t('common.index')" align="center" width="70" />
                                                <!-- <el-table-column prop="groupName" label="步骤名称" align="center"/> -->
                                                <el-table-column prop="groupName" :label="$t('evaluationOperationCard.stepName')" align="center"/>
                                                <!-- <el-table-column prop="groupContent" label="步骤内容" align="center" show-overflow-tooltip/> -->
                                                <el-table-column prop="groupContent" :label="$t('evaluationOperationCard.stepContent')" align="center" show-overflow-tooltip/>
                                                <!-- <el-table-column prop="groupContentDetail" label="执行标准/注意事项" align="center" show-overflow-tooltip/> -->
                                                <el-table-column prop="groupContentDetail" :label="$t('evaluationOperationCard.executionStandard')" align="center" show-overflow-tooltip/>
                                                <!-- <el-table-column label="操作" align="center" width="180"> -->
                                                <el-table-column :label="$t('common.action')" align="center" width="180">
                                                    <template #default="scope">
                                                        <!-- 新增操作步骤 -->
                                                        <ButtonIcon
                                                            text
                                                            type="primary"
                                                            icon="material-symbols:add"
                                                            :tooltipContent="$t('evaluationOperationCard.addOperationStep')"
                                                            @click="handleStepThreeAdd(scope.row)"
                                                        />
                                                        <NDivider vertical />
                                                        <ButtonIcon
                                                            text
                                                            type="primary"
                                                            icon="material-symbols:drive-file-rename-outline-outline"
                                                            :tooltipContent="$t('common.edit')"
                                                            @click="handleStepTwoEdit(scope.row.id)"
                                                        />
                                                        <NDivider vertical />
                                                        <ButtonIcon
                                                            text
                                                            type="error"
                                                            icon="material-symbols:delete-outline"
                                                            :tooltipContent="$t('common.delete')"
                                                            :popconfirmContent="$t('common.confirmDelete')"
                                                            @positiveClick="handleStepTwoDelete(scope.row.id)"
                                                        />
                                                    </template>
                                                </el-table-column>
                                            </el-table>
                                        </div>
                                        <div class="stepTwo-pagination">
                                            <n-pagination
                                                ref="paginationTwo"
                                                :item-count="scope.row.childDataTotal"
                                                :page-sizes="[10, 20, 30, 40]"
                                                show-size-picker
                                                v-model:page="scope.row.childParams.pageNum"
                                                v-model:page-size="scope.row.childParams.pageSize"
                                                :on-update:page="(page)=>{onUpdatePageTwo(page,scope.row)}"
                                                :on-update:page-size="(pageSize)=>{onUpdatePageSizeTwo(pageSize,scope.row)}"
                                            >
                                                <template #prefix>
                                                    <!-- 共 {{ scope.row.childDataTotal }} 条 -->
                                                    {{$t('datatable.itemCount', { total: scope.row.childDataTotal })}}
                                                </template>
                                            </n-pagination>
                                        </div>
                                    </div>
                                </template>
                            </el-table-column>
                            <el-table-column type="index" :label="$t('common.index')" align="center" width="70" />
                            <!-- <el-table-column prop="name" label="操作卡名称" align="center"/> -->
                            <el-table-column prop="name" :label="$t('evaluationOperationCard.cardName')" align="center"/>
                            <!-- <el-table-column prop="initialZB" label="初始位置" align="center" width="320"/> -->
                            <el-table-column prop="initialZB" :label="$t('evaluationOperationCard.initialPosition')" align="center" width="320"/>
                            <!-- <el-table-column prop="createBy" label="创建人" align="center" width="120"/> -->
                            <el-table-column prop="createBy" :label="$t('evaluationOperationCard.creator')" align="center" width="120"/>
                            <!-- <el-table-column prop="updateTime" label="创建时间" align="center" width="180"/> -->
                            <el-table-column prop="updateTime" :label="$t('evaluationOperationCard.createTime')" align="center" width="180"/>
                            <!-- <el-table-column label="操作" align="center" width="180"> -->
                            <el-table-column :label="$t('common.action')" align="center" width="180">
                                <template #default="scope">
                                    <!-- 新增步骤组 -->
                                    <ButtonIcon
                                        text
                                        type="primary"
                                        icon="material-symbols:add"
                                        :tooltipContent="$t('common.addStepGroup')"
                                        @click="handleAddStepGroupInRow(scope.row)"
                                    />
                                    <NDivider vertical />
                                    <ButtonIcon
                                        text
                                        type="primary"
                                        icon="material-symbols:drive-file-rename-outline-outline"
                                        :tooltipContent="$t('common.edit')"
                                        @click="handleStepOneEdit(scope.row.id)"
                                    />
                                    <NDivider vertical />
                                    <ButtonIcon
                                        text
                                        type="error"
                                        icon="material-symbols:delete-outline"
                                        :tooltipContent="$t('common.delete')"
                                        :popconfirmContent="$t('common.confirmDelete')"
                                        @positiveClick="handleStepOneDelete(scope.row.id)"
                                    />
                                </template>
                            </el-table-column>
                        </el-table>
                    </div>
                    <div class="table-pagination">
                        <n-pagination
                            ref="paginationOne"
                            :item-count="total"
                            :page-sizes="[10, 20, 30, 40]"
                            show-size-picker
                            v-model:page="searchParams.pageNum"
                            v-model:page-size="searchParams.pageSize"
                            :on-update:page="onUpdatePageOne"
                            :on-update:page-size="onUpdatePageSizeOne"
                        >
                            <template #prefix>
                                {{$t('datatable.itemCount', { total: total })}}
                            </template>
                        </n-pagination>
                    </div>
                </div>
            </n-spin>
        </NCard>
        <StepOneOperateDrawer
            v-model:visible="stepOneDrawerVisible"
            :operate-type="stepOneOperateType"
            :row-data="stepOneEditingData"
            @submitted="getDataByPage"
        />
        <StepTwoOperateDrawer
            v-model:visible="stepTwoDrawerVisible"
            :operate-type="stepTwoOperateType"
            :row-data="stepTwoEditingData"
            :step-one-hsc-id="stepOneCurrentRow?.id"
            @submitted="getStepTwoData"
        />
        <StepThreeOperateDrawer
            v-model:visible="stepThreeDrawerVisible"
            :operate-type="stepThreeOperateType"
            :row-data="stepThreeEditingData"
            :step-one-hsc-id="stepOneCurrentRow?.id"
            :step-two-hsc-id="stepTwoCurrentRow?.id"
            @submitted="getStepThreeData"
        />
        <n-modal v-model:show="showModal"
                 preset="card"
                 :title="modalType === 'train' ? $t('evaluationOperationCard.copyTrainCard') : $t('evaluationOperationCard.copyExamCard')"
                 isplay-directive="show"
                 class="w-600px"
        >
            <NForm ref="formRef" :model="sopCardModel" :rules="sopCardRules">
                <!-- <NFormItem label="培训操作卡" path="id" v-if="modalType === 'train'"> -->
                <NFormItem :label="$t('evaluationOperationCard.trainCard')" path="id" v-if="modalType === 'train'">
                    <NSelect v-model:value="sopCardModel.id"
                             :options="trainList"
                             label-field="name"
                             value-field="id"
                             filterable
                    />
                </NFormItem>
                <!-- <NFormItem label="操作卡名称" path="name"> -->
                <NFormItem :label="$t('evaluationOperationCard.cardName')" path="name">
                    <NInput v-model:value="sopCardModel.name" :placeholder="$t('evaluationOperationCard.inputPlaceholder')"/>
                </NFormItem>
                <!-- <NFormItem label="初始化坐标" path="initialZB">
                    <NInput v-model:value="sopCardModel.initialZB" placeholder="请输入"/>
                </NFormItem> -->
            </NForm>
            <template #footer>
                <NSpace justify="end" :size="16">
                    <NButton @click="handleCancel" :disabled="isBtnLoading">{{ $t('common.cancel') }}</NButton>
                    <NButton type="primary" @click="handleSubmit" :loading="isBtnLoading">{{ $t('common.confirm') }}</NButton>
                </NSpace>
            </template>
        </n-modal>
    </div>
</template>
<script setup lang="ts">
    import { reactive, ref } from 'vue';
    import EvaluationSearch from './modules/evaluation-search.vue';
    import {$t} from "@/locales";
    import StepOneOperateDrawer from "./modules/stepone-operate-drawer.vue";
    import {
        getSopCardList, getSopCardInfo, delSopCard, getSopCardStepGroupList, delSopCardStepGroup, getSopCardStepGroupInfo,
        getStepsByGroupId, delSopCardStepOpt, getSopCardStepOptInfo, sopCardCopy, getSopCardData
    } from '@/service/api/business/trainingoperationcard';
    import ButtonIcon from '@/components/custom/button-icon.vue';
    import {NDivider} from "naive-ui";
    import {jsonClone} from "~/packages/utils/src/klona";
    import StepTwoOperateDrawer from "./modules/steptwo-operate-drawer.vue";
    import StepThreeOperateDrawer from "./modules/stepthree-operate-drawer.vue";
    import { useThemeStore } from '@/store/modules/theme';
    import {useFormRules, useNaiveForm} from "@/hooks/common/form";

    /** 主题相关参数 */
    const themeStore = useThemeStore();

    /** 操作卡第一步相关操作 */
    const searchParams = ref({
        pageNum: 1,
        pageSize: 10,
        sopCardModel: 'exam',
        name: null
    });
    const stepOneDrawerVisible = ref(false);
    const stepOneOperateType:any = ref('add');
    const stepOneEditingData:any = ref(null);
    const resetSearchParams = ()=>{
        searchParams.value = {
            pageNum: 1,
            pageSize: 10,
            sopCardModel: 'exam',
            name: null
        }
        getDataByPage();
    }
    const handleAdd = ()=>{
        stepOneOperateType.value = 'add';
        stepOneDrawerVisible.value = true;
    }
    const loading = ref(false);
    const refresh = ()=>{
        stepOneExpandRowKeys.value = [];
        stepTwoExpandRowKeys.value = [];
        getDataByPage();
    }
    const tableData:any = ref([]);
    const total = ref(0);
    /** 过去第一步数据 */
    const getDataByPage = ()=>{
        loading.value = true;
        getSopCardList(searchParams.value).then(res=>{
            let data:any = res.data;
            if(data.code == 200){
                tableData.value = jsonClone(data.rows);
                tableData.value.forEach((item:any)=>{
                    item.childTableData = [];
                    item.childDataTotal = 0;
                    item.childParams = {
                        hscId:item.id,
                        pageNum: 1,
                        pageSize: 10
                    }
                })
                total.value = data.total;
            }
            loading.value = false;
        }).catch(()=>{
            loading.value = false;
        })
    }
    getDataByPage();
    /** 第一步的编辑 */
    const handleStepOneEdit = (id:string)=>{
        getSopCardInfo(id).then(res=>{
            stepOneOperateType.value = 'edit';
            stepOneEditingData.value = jsonClone(res.data);
            stepOneDrawerVisible.value = true;
        })
    }
    /** 第一步的删除 */
    const handleStepOneDelete = async (id:string)=>{
        const { error } = await delSopCard([id]);
        if (error) return;
        window.$message?.success($t('common.deleteSuccess'));
        getDataByPage();
    }
    /** 第一步表格展开功能 */
    const stepOneExpandRowKeys:any = ref([]);
    const stepOneExpandChange = (expandedRows:any,expanded:any)=>{
        if(expanded.length > 0){
            stepOneCurrentRow.value = expandedRows;
            stepOneExpandRowKeys.value = [expandedRows.id];
            if(expandedRows.childTableData.length === 0){
                getStepTwoData();
            }
        }else{
            stepOneExpandRowKeys.value = [];
        }
    }
    /** 第一步分页 */
    const onUpdatePageOne = (page: number)=>{
        searchParams.value.pageNum = page;
        getDataByPage();
    }
    const onUpdatePageSizeOne = (pageSize: number)=>{
        searchParams.value.pageSize = pageSize;
        getDataByPage();
    }

    /** 第二步相关操作 */
    const stepTwoDrawerVisible:any = ref(false);
    const stepTwoOperateType:any = ref('add');
    const stepTwoEditingData:any = ref(null);
    const stepOneCurrentRow:any = ref();
    /** 获取第二步数据列表 */
    const getStepTwoData = ()=>{
        getSopCardStepGroupList(stepOneCurrentRow.value.childParams).then(res=>{
            const data:any = res.data;
            if(data.code == 200){
                data.rows.forEach((item:any)=>{
                    item.childTableData = [];
                    item.childDataTotal = 0;
                    item.childParams = {
                        hscId:item.id,
                        pageNum: 1,
                        pageSize: 10
                    }
                })
                stepOneCurrentRow.value.childTableData = data.rows;
                stepOneCurrentRow.value.childDataTotal = data.total;

            }
        })
    }
    /** 第二步点击新增 */
    const handleAddStepGroupInRow = (row: any) => {
        // 如果当前行未展开，则展开它
            if (!stepOneExpandRowKeys.value.includes(row.id)) {
            stepOneExpandRowKeys.value = [row.id];
            stepOneCurrentRow.value = row;
            // 如果是首次展开，加载子数据（可选：也可以不加载，因为只是新增）
            if (!row.childTableData || row.childTableData.length === 0) {
                // 可选：提前加载已有步骤组（不影响新增）
                getStepTwoData();
            }
        } else {
            // 已展开，直接设置当前行
            stepOneCurrentRow.value = row;
        }

        // 打开新增抽屉
        stepTwoOperateType.value = 'add';
        stepTwoDrawerVisible.value = true;
    };

    /** 第二部展开功能 */
    const stepTwoExpandRowKeys:any = ref([]);
    const stepTwoCurrentRow:any = ref();
    const stepTwoExpandChange = (expandedRows:any,expanded:any)=>{
        if(expanded.length > 0){
            stepTwoCurrentRow.value = expandedRows;
            stepTwoExpandRowKeys.value = [expandedRows.id];
            if(expandedRows.childTableData.length === 0){
                getStepThreeData();
            }
        }else{
            stepTwoExpandRowKeys.value = [];
        }
    }
    /** 第二步编辑 */
    const handleStepTwoEdit = (id:any)=>{
        getSopCardStepGroupInfo(id).then(res=>{
            stepTwoOperateType.value = 'edit';
            stepTwoEditingData.value = jsonClone(res.data);
            stepTwoDrawerVisible.value = true;
        })
    }
    /** 第二步删除 */
    const handleStepTwoDelete = async (id:any)=>{
        const { error } = await delSopCardStepGroup([id]);
        if (error) return;
        window.$message?.success($t('common.deleteSuccess'));
        getStepTwoData();
    }
    /** 第二步分页 */
    const onUpdatePageTwo = (page: number)=>{
        stepOneCurrentRow.value.childParams.pageNum = page;
        getStepTwoData();
    }
    const onUpdatePageSizeTwo = (pageSize: number)=>{
        stepOneCurrentRow.value.childParams.pageSize = pageSize;
        getStepTwoData();
    }

    /** 第三步相关操作 */
    const stepThreeDrawerVisible:any = ref(false);
    const stepThreeOperateType:any = ref('add');
    const stepThreeEditingData:any = ref(null);
    /** 第三步点击新增 */
    const handleStepThreeAdd = (row: any)=>{
        if(!stepTwoExpandRowKeys.value.includes(row.id)){
            stepTwoExpandRowKeys.value = [row.id];
            stepTwoCurrentRow.value = row;
            // 如果是首次展开，加载子数据（可选：也可以不加载，因为只是新增）
            if (!row.childTableData || row.childTableData.length === 0) {
                // 可选：提前加载已有步骤组（不影响新增）
                getStepThreeData();
            }
        } else {
            // 已展开，直接设置当前行
            stepTwoCurrentRow.value = row;
        }
        stepThreeOperateType.value = 'add';
        stepThreeEditingData.value = null;
        stepThreeDrawerVisible.value = true;
    }
    /** 获取第三步数据列表 */
    const getStepThreeData = ()=>{
        getStepsByGroupId(stepTwoCurrentRow.value.childParams).then(res=>{
            const data:any = res.data;
            if(data.code == 200){
                stepTwoCurrentRow.value.childTableData = data.rows;
                stepTwoCurrentRow.value.childDataTotal = data.total;
            }
        })
    }
    /** 第三步编辑 */
    const handleStepThreeEdit = (id:string)=>{
        getSopCardStepOptInfo(id).then(res=>{
            stepThreeOperateType.value = 'edit';
            stepThreeEditingData.value = jsonClone(res.data);
            stepThreeDrawerVisible.value = true;
        })
    }
    /** 第三步删除 */
    const handleStepThreeDelete = async (id:string)=>{
        const { error } = await delSopCardStepOpt(id);
        if (error) return;
        window.$message?.success($t('common.deleteSuccess'));
        getStepThreeData();
    }
    /** 第三步复制 */
    const handleStepThreeCopy = (id:string)=>{
        getSopCardStepOptInfo(id).then((res:any)=>{
            stepThreeOperateType.value = 'copy';
            delete res.data.id;
            delete res.data.indexing;
            delete res.data.effect.animation?.id;
            delete res.data.effect.popwindow?.id;
            delete res.data.effect.voice?.id;
            stepThreeEditingData.value = jsonClone(res.data);
            stepThreeDrawerVisible.value = true;
        })
    }
    /** 第三步插入上一步 */
    const handleStepThreeMoveUp = (id:string)=>{
        // 在打开 StepThreeOperateDrawer 时，传递当前项的 indexing 值
        getSopCardStepOptInfo(id).then((res:any)=>{
            stepThreeOperateType.value = 'add';
            // 将当前项的 indexing 传递给抽屉组件
            stepThreeEditingData.value = jsonClone({ id: id, insertLocal:"0" });
            stepThreeDrawerVisible.value = true;
        })
    }
    /** 第三步插入下一步 */
    const handleStepThreeMoveDown = (id:string)=>{
        getSopCardStepOptInfo(id).then((res:any)=>{
            stepThreeOperateType.value = 'add';
            // 将当前项的 indexing 传递给抽屉组件
            stepThreeEditingData.value = jsonClone({ id: id, insertLocal:"1" });
            stepThreeDrawerVisible.value = true;
        })
    }

    /** 第三步分页 */
    const onUpdatePageThree = (page: number)=>{
        stepTwoCurrentRow.value.childParams.pageNum = page;
        getStepThreeData();
    }
    const onUpdatePageSizeThree = (pageSize: number)=>{
        stepTwoCurrentRow.value.childParams.pageSize = pageSize;
        getStepThreeData();
    }

    /** 复制操作卡相关操作 */
    const currentRow = ref();
    /** 选中操作卡数据 */
    const handleCurrentChange = (val:any)=>{
        currentRow.value = val
    }
    const showModal = ref(false);
    const createDefaultSopCardModel = ()=>{
        return {
            id:'',
            name:null,
            initialZB: null,
            sopCardModel : 'exam'
        }
    }
    const sopCardModel: any = reactive(createDefaultSopCardModel());
    const {formRef, validate, restoreValidation} = useNaiveForm();
    const {createRequiredRule} = useFormRules();
    const sopCardRules = {
        // id: createRequiredRule('请选择培训操作卡'),
        id: createRequiredRule($t('evaluationOperationCard.selectTrainCardRequired')),
        // name: createRequiredRule('操作卡名称不能为空'),
        name: createRequiredRule($t('evaluationOperationCard.cardNameRequired')),
        // initialZB: createRequiredRule('初始化坐标不能为空')
        initialZB: createRequiredRule($t('evaluationOperationCard.initialPositionRequired'))
    }
    const isBtnLoading = ref(false);
    /** 取消复制 */
    const handleCancel = ()=>{
        showModal.value = false;
    }
    /** 提交复制 */
    const handleSubmit = async ()=>{
        await validate();
        isBtnLoading.value = true;
        const {error} = await sopCardCopy(sopCardModel);
        isBtnLoading.value = false;
        if (error) return;
        window.$message?.success($t('common.updateSuccess'));
        showModal.value = false;
        getDataByPage();
    }
    const modalType = ref('');
    const trainList = ref([]);
    /** 打开复制操作卡弹窗 */
    const handleSopCardCopy = (type:string)=>{
        getSopCardData({
            sopCardModel: 'train'
        }).then(res=>{
            trainList.value = res.data;
        })
        modalType.value = type;
        showModal.value = true;
        Object.assign(sopCardModel, createDefaultSopCardModel());
        if(type === 'exam'){
            sopCardModel.id = currentRow.value.id;
        }
        restoreValidation();
    }
</script>
<style scoped lang="scss">
:deep(.n-spin-content){
    height: 100%;
}
.content-wrap{
    width: 100%;
    height: 100%;
    display: flex;
    flex-direction: column;
    .table-wrap{
        flex:1;
        position: relative;
        overflow-y: auto;
        overflow-x: hidden;
        :deep(.light-table){
            --el-table-header-bg-color: rgb(250,250,252);
            --el-table-header-text-color: #1F2225;
        }
        :deep(.dark-table){
            --el-table-bg-color:  rgb(24,24,28);
            --el-table-tr-bg-color:  rgb(24,24,28);
            --el-table-border-color: rgba(45,45,48,1);
            --el-table-text-color: #FFFFFF;
            --el-table-header-bg-color: rgb(24,24,28);
            --el-table-header-text-color: #FFFFFF;
            --el-table-current-row-bg-color: rgba(38,38,42,1);
            --el-table-row-hover-bg-color: rgba(38,38,42,1);
            --el-table-expanded-cell-bg-color: rgb(24,24,28);
        }
    }
    .table-pagination{
        margin-top: 12px;
        display: flex;
        justify-content: flex-end;
    }
    .stepTwo-content-wrap{
        padding: 8px 16px;
        .stepTwo-table{
            padding: 0 0 16px 0;
        }
        .stepTwo-pagination{
            display: flex;
            justify-content: flex-end;
        }
    }
    .stepThree-content-wrap{
        padding: 8px 16px;
        .stepThree-table{
            padding: 0 0 16px 0;
        }
    }
}
</style>
