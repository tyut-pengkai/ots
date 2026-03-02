package org.dromara.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.MessageUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.satoken.utils.LoginHelper;
import org.dromara.domain.HlvrQuestionItem;
import org.dromara.domain.HlvrSopCard;
import org.dromara.domain.HlvrSopCardStepGroup;
import org.dromara.domain.HlvrSopCardStepOpt;
import org.dromara.domain.HlvrSopCardStepOptHistory;
import org.dromara.domain.HlvrSopCardStepResult;
import org.dromara.domain.bo.HlvrQuestionItemBo;
import org.dromara.domain.bo.HlvrSopCardStepOptHistoryBo;
import org.dromara.domain.vo.HlvrQuestionItemVo;
import org.dromara.domain.vo.HlvrSopCardStepOptHistoryVo;
import org.dromara.domain.vo.HlvrSopCardStepOptVo;
import org.dromara.mapper.HlvrQuestionItemMapper;
import org.dromara.mapper.HlvrSopCardMapper;
import org.dromara.mapper.HlvrSopCardStepGroupMapper;
import org.dromara.mapper.HlvrSopCardStepOptHistoryMapper;
import org.dromara.mapper.HlvrSopCardStepOptMapper;
import org.dromara.mapper.HlvrSopCardStepResultMapper;
import org.springframework.stereotype.Service;
import org.dromara.domain.bo.HlvrQuestionBo;
import org.dromara.domain.vo.HlvrQuestionVo;
import org.dromara.domain.HlvrQuestion;
import org.dromara.mapper.HlvrQuestionMapper;
import org.dromara.service.IHlvrQuestionService;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 问题Service业务层处理
 *
 * @author admin
 * @date 2025-11-18
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class HlvrQuestionServiceImpl implements IHlvrQuestionService {

    private final HlvrQuestionMapper baseMapper;
    private final HlvrQuestionItemMapper itemMapper;

    private final HlvrSopCardStepOptMapper stepOptMapper;
    private final HlvrSopCardStepOptHistoryMapper historyMapper;

    private final HlvrSopCardMapper sopCardMapper;
    private final HlvrSopCardStepGroupMapper stepGroupMapper;
    private final HlvrSopCardStepResultMapper stepResultMapper;
    /**
     * 查询问题
     *
     * @param id 主键
     * @return 问题
     */
    @Override
    public HlvrQuestionVo queryById(Long id){
        HlvrQuestionVo result = baseMapper.selectVoById(id);
        QueryWrapper<HlvrQuestionItem> query = new QueryWrapper<>();
        query.eq("hq_id", id);
        result.setQuestionItemBos(itemMapper.selectVoList(query));
        return result;
    }

    /**
     * 分页查询问题列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 问题分页列表
     */
    @Override
    public TableDataInfo<HlvrQuestionVo> queryPageList(HlvrQuestionBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<HlvrQuestion> lqw = buildQueryWrapper(bo);
        Page<HlvrQuestionVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的问题列表
     *
     * @param bo 查询条件
     * @return 问题列表
     */
    @Override
    public List<HlvrQuestionVo> queryList(HlvrQuestionBo bo) {
        LambdaQueryWrapper<HlvrQuestion> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<HlvrQuestion> buildQueryWrapper(HlvrQuestionBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<HlvrQuestion> lqw = Wrappers.lambdaQuery();
        lqw.orderByAsc(HlvrQuestion::getId);
        lqw.eq(bo.getHscId() != null, HlvrQuestion::getHscId, bo.getHscId());
        lqw.like(StringUtils.isNotBlank(bo.getQuestionName()), HlvrQuestion::getQuestionName, bo.getQuestionName());
        lqw.eq(StringUtils.isNotBlank(bo.getQuestionDescription()), HlvrQuestion::getQuestionDescription, bo.getQuestionDescription());
        lqw.eq(StringUtils.isNotBlank(bo.getQuestionType()), HlvrQuestion::getQuestionType, bo.getQuestionType());
        return lqw;
    }

    /**
     * 新增问题
     *
     * @param bo 问题
     * @return 是否新增成功
     */
    @Override
    @Transactional
    public Boolean insertByBo(HlvrQuestionBo bo) {
        HlvrQuestion add = MapstructUtils.convert(bo, HlvrQuestion.class);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
            List<HlvrQuestionItemBo> items = bo.getQuestionItemBos();
            items.forEach(item -> item.setHqId(bo.getId()));
            itemMapper.insertBatch(MapstructUtils.convert(items, HlvrQuestionItem.class));
        }
        return flag;
    }

    /**
     * 修改问题
     *
     * @param bo 问题
     * @return 是否修改成功
     */
    @Override
    @Transactional
    public Boolean updateByBo(HlvrQuestionBo bo) {
        HlvrQuestion update = MapstructUtils.convert(bo, HlvrQuestion.class);
        boolean flag = baseMapper.updateById(update) > 0;
        if (flag) {
            List<HlvrQuestionItemBo> items = bo.getQuestionItemBos();
            items.forEach(item -> {
                item.setHqId(bo.getId());
                item.setId(null);
            });
            QueryWrapper<HlvrQuestionItem> delete = new QueryWrapper<>();
            delete.eq("hq_id", bo.getId());
            itemMapper.delete(delete);
            itemMapper.insertBatch(MapstructUtils.convert(items, HlvrQuestionItem.class));
        }
        return flag;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(HlvrQuestion entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除问题信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(!ids.isEmpty()){

            LambdaQueryWrapper<HlvrSopCardStepResult>queryWrapper = Wrappers.lambdaQuery();
            // 构建 OR 条件：只要 resource_attr 中包含任意一个 id 即命中
            queryWrapper.and(wrapper -> {
                for (Long id : ids) {
                    // 核心逻辑：
                    // 1. 提取 JSON 中的 id 字段：resource_attr->'$.id'
                    // 2. 判断是否包含目标值：JSON_CONTAINS(..., '目标值')
                    // 3. 注意：因为 JSON 里存的是字符串 "2016...", 所以传入的值也必须带双引号 "\"2016...\""
                    String jsonValue = "\"" + id + "\"";

                    // SQL 片段: JSON_CONTAINS(resource_attr->'$.id', '"2016695388834390018"')
                    wrapper.or().apply("JSON_CONTAINS(resource_attr->'$.id', {0})", jsonValue);
                }
            });

            // 【关键修改】：查询出具体的记录列表，而不是只查数量
            // 限制查询条数（例如最多展示 5 条），避免关联数据过多导致报错信息过长或内存溢出
            queryWrapper.last("LIMIT 5");

            List<HlvrSopCardStepResult> conflictList = stepResultMapper.selectList(queryWrapper);

            if (!conflictList.isEmpty()) {
                // 2. 收集所有涉及的步骤 ID (hscso_id)
                Set<Long> stepOptIds = new HashSet<>();
                for (HlvrSopCardStepResult result : conflictList) {
                    if (result.getHscsoId() != null) {
                        stepOptIds.add(result.getHscsoId());
                    }
                }
                // 3. 第一步链式查询：根据 stepOptIds 查 Step Opt 表，获取 opt_content 和 hscsg_id
                Map<Long, HlvrSopCardStepOpt> stepOptMap = new HashMap<>();
                if (!stepOptIds.isEmpty()) {
                    List<HlvrSopCardStepOpt> steps = stepOptMapper.selectBatchIds(stepOptIds);
                    for (HlvrSopCardStepOpt step : steps) {
                        stepOptMap.put(step.getId(), step);
                    }
                }
                // 4. 第二步链式查询：收集所有的分组 ID (hscsg_id)，查 Step Group 表
                Set<Long> groupIds = new HashSet<>();
                for (HlvrSopCardStepOpt step : stepOptMap.values()) {
                    if (step.getHscsgId() != null) {
                        groupIds.add(step.getHscsgId());
                    }
                }
                Map<Long, HlvrSopCardStepGroup> groupMap = new HashMap<>();
                if (!groupIds.isEmpty()) {
                    List<HlvrSopCardStepGroup> groups = stepGroupMapper.selectBatchIds(groupIds);
                    for (HlvrSopCardStepGroup group : groups) {
                        groupMap.put(group.getId(), group);
                    }
                }
                // 5. 第三步链式查询：收集所有的卡片 ID (hsc_id)，查 SOP Card 表
                Set<Long> cardIds = new HashSet<>();
                for (HlvrSopCardStepGroup group : groupMap.values()) {
                    if (group.getHscId() != null) {
                        cardIds.add(group.getHscId());
                    }
                }
                Map<Long, HlvrSopCard> cardMap = new HashMap<>();
                if (!cardIds.isEmpty()) {
                    List<HlvrSopCard> cards = sopCardMapper.selectBatchIds(cardIds);
                    for (HlvrSopCard card : cards) {
                        cardMap.put(card.getId(), card);
                    }
                }
                // 6. 组装详细的报错信息
                //"删除失败，检测到以下 SOP 流程正在引用该资源，请先解除关联："
                StringBuilder errorMsg = new StringBuilder(MessageUtils.message("error.sop.delete.conflict.header"));

                int index = 0;
                for (HlvrSopCardStepResult result : conflictList) {
                    index++;
                    Long stepId = result.getHscsoId();

                    // 获取步骤信息
                    HlvrSopCardStepOpt step = stepOptMap.get(stepId);
                    String stepContent = (step != null && step.getOptContent() != null) ? step.getOptContent() : "未知步骤";
                    // 截取过长的步骤内容
                    if (stepContent.length() > 30) {
                        stepContent = stepContent.substring(0, 30) + "...";
                    }

                    // 获取分组信息
                    String groupName = "未知分组";
                    if (step != null && step.getHscsgId() != null) {
                        HlvrSopCardStepGroup group = groupMap.get(step.getHscsgId());
                        if (group != null && group.getGroupName() != null) {
                            groupName = group.getGroupName();
                        }
                    }

                    // 获取操作卡信息
                    String cardName = "未知操作卡";
                    if (step != null && step.getHscsgId() != null) {
                        HlvrSopCardStepGroup group = groupMap.get(step.getHscsgId());
                        if (group != null && group.getHscId() != null) {
                            HlvrSopCard card = cardMap.get(group.getHscId());
                            if (card != null && card.getName() != null) {
                                cardName = card.getName();
                            }
                        }
                    }
                    //[%d] 操作卡：<%s>\n    分组：<%s>\n    步骤：%s\n\n",

                    errorMsg.append(String.format("[%d] %s：<%s>\n    %s：<%s>\n    %s：%s\n\n",
                        index, MessageUtils.message("label.sop.card"),cardName, MessageUtils.message("label.sop.group"),groupName, MessageUtils.message("label.sop.step"),stepContent));
                }

                if (conflictList.size() >= 5) {
                    // 假设总数可能更多，提示用户
                    //errorMsg.append("... 以及其他关联项，请检查完整列表。");
                    errorMsg.append(MessageUtils.message("error.sop.delete.conflict.footer.more"));
                } else {
                    //errorMsg.append("请进入对应的 SOP 操作卡，删除或修改上述步骤后重试。");
                    errorMsg.append(MessageUtils.message("error.sop.delete.conflict.footer.normal"));
                }

                log.error("删除校验失败：{}", errorMsg.toString());
                throw new RuntimeException(errorMsg.toString());
            }


        }
        QueryWrapper<HlvrQuestionItem> delete = new QueryWrapper<>();
        delete.in("hq_id", ids);
        itemMapper.delete(delete);
        return baseMapper.deleteByIds(ids) > 0;
    }

    @Override
    @Transactional
    public Boolean submitAnswer(HlvrSopCardStepOptHistoryBo bo) {
        if(bo.getAnswer() == null) {
            log.error("没有有效作答");
            throw new RuntimeException("请选择答案");
        }
        HlvrQuestionItemVo answer = itemMapper.selectVoById(bo.getAnswer());
        HlvrQuestionVo question = baseMapper.selectVoById(answer.getHqId());
        historyMapper.update(null, new LambdaUpdateWrapper<HlvrSopCardStepOptHistory>()
            .set(HlvrSopCardStepOptHistory::getAnswer, bo.getAnswer())
            .set(answer.getIsAnswer().equals("1"), HlvrSopCardStepOptHistory::getScore, stepOptMapper.selectVoById(bo.getHscsoId()).getScore())
            .set(question.getQuestionType().equals("step"), HlvrSopCardStepOptHistory::getNextStep, answer.getHscsoId())
            .eq(HlvrSopCardStepOptHistory::getBatchId, bo.getBatchId())
            .eq(HlvrSopCardStepOptHistory::getHscsoId, bo.getHscsoId()));

        return true;
    }
}
