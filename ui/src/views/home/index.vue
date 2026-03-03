<script setup lang="ts">
import { computed, onMounted, ref, watch } from 'vue';
import { useRouter } from 'vue-router';
import dayjs from 'dayjs';
import { fetchHomeStats } from '@/service/api/business/home';
import { useEcharts } from '@/hooks/common/echarts';

const router = useRouter();
const loading = ref(false);
const stats = ref<Api.Business.HomeStats | null>(null);
const rangeType = ref<'today' | 'week' | 'month' | 'custom'>('week');
const customRange = ref<[number, number] | null>(null);

const coreCards = computed(() => {
    if (!stats.value) return [];
    return [
        { title: '培训考核总次数', value: stats.value.core.totalBatchCount, to: '/business/training-history' },
        { title: '操作卡总数', value: stats.value.core.totalCardCount, to: '/business/trainingoperationcard' },
        { title: '题库总规模', value: stats.value.core.totalQuestionCount, to: '/business/question' },
        { title: '参训覆盖部门数', value: stats.value.core.coveredDeptCount, to: '/system/dept' },
        { title: '模型/资源总数', value: stats.value.core.totalAssetCount, to: '/business/element' }
    ];
});

const quickActions = [
    { label: '培训配置', path: '/trainingManagement/trainingConfig' },
    { label: '考核配置', path: '/examManagement/examConfig' },
    { label: '培训历史', path: '/trainingManagement/trainingHistory' },
    { label: '考核历史', path: '/examManagement/examHistory' }
];

const { domRef: batchTrendRef, updateOptions: updateBatchTrend } = useEcharts(() => ({
    tooltip: { trigger: 'axis' },
    xAxis: { type: 'category', data: [] },
    yAxis: { type: 'value' },
    series: [{ name: '批次趋势', type: 'line', smooth: true, data: [] }]
}));

const { domRef: questionPieRef, updateOptions: updateQuestionPie } = useEcharts(() => ({
    tooltip: { trigger: 'item' },
    legend: { bottom: 0 },
    series: [{ type: 'pie', radius: ['40%', '70%'], data: [] }]
}));

const { domRef: cardTypePieRef, updateOptions: updateCardTypePie } = useEcharts(() => ({
    tooltip: { trigger: 'item' },
    legend: { bottom: 0 },
    series: [{ type: 'pie', radius: ['35%', '70%'], data: [] }]
}));

const { domRef: assetPieRef, updateOptions: updateAssetPie } = useEcharts(() => ({
    tooltip: { trigger: 'item' },
    legend: { bottom: 0 },
    series: [{ type: 'pie', radius: ['35%', '70%'], data: [] }]
}));

const { domRef: deptBarRef, updateOptions: updateDeptBar } = useEcharts(() => ({
    tooltip: { trigger: 'axis' },
    xAxis: { type: 'value' },
    yAxis: { type: 'category', data: [] },
    series: [{ type: 'bar', data: [] }]
}));

const { domRef: passTrendRef, updateOptions: updatePassTrend } = useEcharts(() => ({
    tooltip: { trigger: 'axis' },
    xAxis: { type: 'category', data: [] },
    yAxis: { type: 'value', max: 100 },
    series: [{ name: '通过率', type: 'line', smooth: true, areaStyle: {}, data: [] }]
}));

const { domRef: loginTrendRef, updateOptions: updateLoginTrend } = useEcharts(() => ({
    tooltip: { trigger: 'axis' },
    xAxis: { type: 'category', data: [] },
    yAxis: { type: 'value' },
    series: [{ name: '登录次数', type: 'bar', data: [] }]
}));

function resolveDateRange() {
    const end = dayjs();
    if (rangeType.value === 'today') {
        return { beginDate: end.format('YYYY-MM-DD'), endDate: end.format('YYYY-MM-DD') };
    }
    if (rangeType.value === 'week') {
        return { beginDate: end.subtract(6, 'day').format('YYYY-MM-DD'), endDate: end.format('YYYY-MM-DD') };
    }
    if (rangeType.value === 'month') {
        return { beginDate: end.subtract(29, 'day').format('YYYY-MM-DD'), endDate: end.format('YYYY-MM-DD') };
    }
    if (customRange.value) {
        return {
            beginDate: dayjs(customRange.value[0]).format('YYYY-MM-DD'),
            endDate: dayjs(customRange.value[1]).format('YYYY-MM-DD')
        };
    }
    return { beginDate: end.subtract(6, 'day').format('YYYY-MM-DD'), endDate: end.format('YYYY-MM-DD') };
}

function renderCharts() {
    if (!stats.value) return;
    updateBatchTrend(opt => {
        opt.xAxis.data = stats.value!.core.batchTrend7d.map(i => i.label);
        opt.series[0].data = stats.value!.core.batchTrend7d.map(i => i.value);
        return opt;
    });
    updateQuestionPie(opt => {
        opt.series[0].data = stats.value!.core.questionTypeDistribution;
        return opt;
    });
    updateCardTypePie(opt => {
        opt.series[0].data = stats.value!.core.cardTypeDistribution;
        return opt;
    });
    updateAssetPie(opt => {
        opt.series[0].data = stats.value!.core.assetTypeDistribution;
        return opt;
    });
    updateDeptBar(opt => {
        opt.yAxis.data = stats.value!.business.deptTop5.map(i => i.name).reverse();
        opt.series[0].data = stats.value!.business.deptTop5.map(i => i.value).reverse();
        return opt;
    });
    updatePassTrend(opt => {
        opt.xAxis.data = stats.value!.business.passRateTrend7d.map(i => i.label);
        opt.series[0].data = stats.value!.business.passRateTrend7d.map(i => i.value);
        return opt;
    });
    updateLoginTrend(opt => {
        opt.xAxis.data = stats.value!.system.loginTrend24h.map(i => i.label);
        opt.series[0].data = stats.value!.system.loginTrend24h.map(i => i.value);
        return opt;
    });
}

async function loadStats() {
    loading.value = true;
    const { data } = await fetchHomeStats(resolveDateRange());
    stats.value = data;
    renderCharts();
    loading.value = false;
}

watch([rangeType, customRange], loadStats);
onMounted(loadStats);
</script>

<template>
    <NSpace vertical :size="16">
        <NCard :bordered="false" title="首页统计看板">
            <NSpace align="center" justify="space-between" class="mb-12px" wrap>
                <NRadioGroup v-model:value="rangeType" size="small">
                    <NRadioButton value="today">今日</NRadioButton>
                    <NRadioButton value="week">本周</NRadioButton>
                    <NRadioButton value="month">本月</NRadioButton>
                    <NRadioButton value="custom">自定义</NRadioButton>
                </NRadioGroup>
                <NDatePicker
                    v-if="rangeType === 'custom'"
                    v-model:value="customRange"
                    type="daterange"
                    clearable
                    size="small"
                    class="w-300px"
                />
            </NSpace>

            <NGrid cols="1 s:2 m:3 l:5" x-gap="12" y-gap="12" responsive="screen">
                <NGi v-for="item in coreCards" :key="item.title">
                    <NCard size="small" hoverable @click="router.push(item.to)">
                        <div class="text-14px text-#666">{{ item.title }}</div>
                        <div class="mt-8px text-26px font-bold">{{ item.value }}</div>
                    </NCard>
                </NGi>
            </NGrid>

            <NSpace class="mt-12px" wrap>
                <NButton
                    v-for="action in quickActions"
                    :key="action.path"
                    size="small"
                    @click="router.push(action.path)"
                >
                    {{ action.label }}
                </NButton>
            </NSpace>
        </NCard>

        <NGrid cols="1 m:2" x-gap="16" y-gap="16" responsive="screen">
            <NGi>
                <NCard :bordered="false" title="近7天培训/考核批次趋势" :loading="loading">
                    <div ref="batchTrendRef" class="h-320px" />
                </NCard>
            </NGi>
            <NGi>
                <NCard :bordered="false" title="题型占比" :loading="loading">
                    <div ref="questionPieRef" class="h-320px" />
                </NCard>
            </NGi>
            <NGi>
                <NCard :bordered="false" title="操作卡类型占比" :loading="loading">
                    <div ref="cardTypePieRef" class="h-280px" />
                </NCard>
            </NGi>
            <NGi>
                <NCard :bordered="false" title="模型/资源占比" :loading="loading">
                    <div ref="assetPieRef" class="h-280px" />
                </NCard>
            </NGi>
        </NGrid>

        <NGrid cols="1 m:2" x-gap="16" y-gap="16" responsive="screen">
            <NGi>
                <NCard :bordered="false" title="业务模块统计" :loading="loading">
                    <div v-if="stats" class="line-height-30px">
                        <div>
                            培训配置新增（今日/本周/本月）：{{ stats.business.trainConfig.today }}/{{
                                stats.business.trainConfig.week
                            }}/{{ stats.business.trainConfig.month }}
                        </div>
                        <div>
                            考核配置新增（今日/本周/本月）：{{ stats.business.examConfig.today }}/{{
                                stats.business.examConfig.week
                            }}/{{ stats.business.examConfig.month }}
                        </div>
                        <div>
                            今日完成批次：{{ stats.business.completedBatchToday }}/{{
                                stats.business.totalBatchToday
                            }}（{{ stats.business.completedRateToday }}%）
                        </div>
                        <div>
                            本周完成批次：{{ stats.business.completedBatchWeek }}/{{
                                stats.business.totalBatchWeek
                            }}（{{ stats.business.completedRateWeek }}%）
                        </div>
                        <div>
                            本月完成批次：{{ stats.business.completedBatchMonth }}/{{
                                stats.business.totalBatchMonth
                            }}（{{ stats.business.completedRateMonth }}%）
                        </div>
                        <div>考核平均通过率：{{ stats.business.avgPassRate }}%</div>
                    </div>
                    <div ref="passTrendRef" class="mt-8px h-220px" />
                </NCard>
            </NGi>
            <NGi>
                <NCard :bordered="false" title="各部门培训参与度 TOP5" :loading="loading">
                    <div ref="deptBarRef" class="h-320px" />
                </NCard>
            </NGi>
        </NGrid>

        <NGrid cols="1 m:2" x-gap="16" y-gap="16" responsive="screen">
            <NGi>
                <NCard :bordered="false" title="系统运营数据" :loading="loading">
                    <NGrid v-if="stats" cols="1 s:2" x-gap="12" y-gap="12" responsive="screen">
                        <NGi>
                            用户总数：{{ stats.system.totalUsers }}（今日新增 {{ stats.system.newUsersToday }}，本周新增
                            {{ stats.system.newUsersWeek }}）
                        </NGi>
                        <NGi>角色/权限：{{ stats.system.totalRoleCount }}/{{ stats.system.totalMenuCount }}</NGi>
                        <NGi>部门/岗位：{{ stats.system.totalDeptCount }}/{{ stats.system.totalPostCount }}</NGi>
                        <NGi>
                            今日登录：{{ stats.system.loginCountToday }}（成功率
                            {{ stats.system.loginSuccessRateToday }}%）
                        </NGi>
                    </NGrid>
                </NCard>
            </NGi>
            <NGi>
                <NCard :bordered="false" title="近24小时登录趋势" :loading="loading">
                    <div ref="loginTrendRef" class="h-240px" />
                </NCard>
            </NGi>
        </NGrid>
    </NSpace>
</template>

<style scoped></style>
