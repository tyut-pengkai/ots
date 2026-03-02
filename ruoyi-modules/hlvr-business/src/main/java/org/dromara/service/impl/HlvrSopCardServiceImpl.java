package org.dromara.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.core.service.SOPCardService;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.MessageUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.translation.annotation.Translation;
import org.dromara.domain.*;
import org.dromara.domain.vo.HlvrSopCardStepGroupVo;
import org.dromara.domain.vo.HlvrSopCardStepOptVo;
import org.dromara.domain.vo.HlvrSopCardStepResultVo;
import org.dromara.mapper.*;
import org.springframework.stereotype.Service;
import org.dromara.domain.bo.HlvrSopCardBo;
import org.dromara.domain.vo.HlvrSopCardVo;
import org.dromara.service.IHlvrSopCardService;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 操作卡Service业务层处理
 *
 * @author Lion Li
 * @date 2025-11-13
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class HlvrSopCardServiceImpl implements IHlvrSopCardService, SOPCardService {

    private final HlvrSopCardMapper baseMapper;
    private final HlvrSopCardStepGroupMapper groupMapper;
    private final HlvrSopCardStepOptMapper stepOptMapper;
    private final HlvrSopCardStepResultMapper resultMapper;
    private final HlvrQuestionMapper questionMapper;
    private final HlvrQuestionItemMapper questionItemMapper;

    private final HlvrTrainingExamDetailMapper detailMapper;
    /**
     * 查询操作卡
     *
     * @param id 主键
     * @return 操作卡
     */
    @Override
    public HlvrSopCardVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询操作卡列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 操作卡分页列表
     */
    @Override
    public TableDataInfo<HlvrSopCardVo> queryPageList(HlvrSopCardBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<HlvrSopCard> lqw = buildQueryWrapper(bo);
        Page<HlvrSopCardVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的操作卡列表
     *
     * @param bo 查询条件
     * @return 操作卡列表
     */
    @Override
    public List<HlvrSopCardVo> queryList(HlvrSopCardBo bo) {
        LambdaQueryWrapper<HlvrSopCard> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<HlvrSopCard> buildQueryWrapper(HlvrSopCardBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<HlvrSopCard> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getName()), HlvrSopCard::getName, bo.getName());
        lqw.eq(bo.getSopCardType() != null, HlvrSopCard::getSopCardType, bo.getSopCardType());
        lqw.eq(StringUtils.isNotBlank(bo.getCardMap()), HlvrSopCard::getCardMap, bo.getCardMap());
        lqw.eq(StringUtils.isNotBlank(bo.getSopCardModel()), HlvrSopCard::getSopCardModel, bo.getSopCardModel());
        return lqw;
    }

    /**
     * 新增操作卡
     *
     * @param bo 操作卡
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(HlvrSopCardBo bo) {
        HlvrSopCard add = MapstructUtils.convert(bo, HlvrSopCard.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改操作卡
     *
     * @param bo 操作卡
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(HlvrSopCardBo bo) {
        HlvrSopCard update = MapstructUtils.convert(bo, HlvrSopCard.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(HlvrSopCard entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除操作卡信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        QueryWrapper<HlvrTrainingExamDetail> delete = new QueryWrapper<>();
        delete.in("hsc_id", ids);
        List<HlvrTrainingExamDetail> details = detailMapper.selectList(delete);
        if(details != null && !details.isEmpty()){
            throw new RuntimeException(MessageUtils.message("sop.card.delete.exist.training"));
        }
        QueryWrapper<HlvrSopCardStepGroup> query = new QueryWrapper<>();
        query.in("hsc_id", ids);
        List<HlvrSopCardStepGroup> groups = groupMapper.selectList(query);
        groupMapper.delete(query);

        if(CollectionUtil.isNotEmpty(groups)) {
            Set<Long> groupsId = groups.stream().map(HlvrSopCardStepGroup::getId).collect(Collectors.toSet());
            QueryWrapper<HlvrSopCardStepOpt> queryStep = new QueryWrapper<>();
            queryStep.in("hscsg_id", groupsId);
            List<HlvrSopCardStepOpt> opts = stepOptMapper.selectList(queryStep);
            stepOptMapper.delete(queryStep);
            if(CollectionUtil.isNotEmpty(opts)) {
                Set<Long> optsId = opts.stream().map(HlvrSopCardStepOpt::getId).collect(Collectors.toSet());
                QueryWrapper<HlvrSopCardStepResult> delResult = new QueryWrapper<>();
                delResult.in("hscso_id", optsId);
                resultMapper.delete(delResult);
            }
        }
        return baseMapper.deleteByIds(ids) > 0;
    }

    @Override
    public HlvrSopCardStepGroupVo getFirstOpt(Long id) {
        QueryWrapper<HlvrSopCardStepGroup> query = new QueryWrapper<>();
        query.eq("hsc_id", id);
        query.orderByAsc("indexing");
        query.last("limit 1");
        List<HlvrSopCardStepGroupVo> groupList = groupMapper.selectVoList(query);
        HlvrSopCardStepGroupVo group = groupList.get(0);
        QueryWrapper<HlvrSopCardStepOpt> optQuery = new QueryWrapper<>();
        optQuery.eq("hscsg_id", group.getId());
        optQuery.orderByAsc("indexing");
        optQuery.last("limit 1");
        List<HlvrSopCardStepOptVo> optList = stepOptMapper.selectVoList(optQuery);
        group.setOpts(optList);
        HlvrSopCardStepOptVo optVo = optList.get(0);
        LambdaQueryWrapper<HlvrSopCardStepResult> lqw = new LambdaQueryWrapper<>();
        lqw.eq(HlvrSopCardStepResult::getHscsoId, optVo.getId());
        List<HlvrSopCardStepResultVo> effects = resultMapper.selectVoList(lqw);
        Map<String, HlvrSopCardStepResultVo> map = new HashMap<>();
        for(HlvrSopCardStepResultVo vo: effects) {
            vo.setChoose(true);
            map.put(vo.getResultType(), vo);
        }
        optVo.setEffect(map);
        return group;
    }

    @Override
    @Transactional
    public Boolean copyCard(HlvrSopCardBo bo) {
        //根据id获取操作卡信息
        HlvrSopCard card = baseMapper.selectById(bo.getId());

        if (card == null) {
            //如果卡片不存在，提前返回或抛异常
            throw new ServiceException("操作卡不存在");
        }
        //获取步骤信息集合
        LambdaQueryWrapper<HlvrSopCardStepGroup> groupQuery = new LambdaQueryWrapper<>();
        groupQuery.eq(HlvrSopCardStepGroup::getHscId, bo.getId());
        List<HlvrSopCardStepGroup> groups = groupMapper.selectList(groupQuery);
        Set<Long> ids = groups.stream().map(HlvrSopCardStepGroup::getId).collect(Collectors.toSet());


        //获取所有步骤详情
        List<HlvrSopCardStepOpt> opts = Collections.emptyList();
        Set<Long> optIds = Collections.emptySet();
        //只有当 分组id 非空时，才查询步骤选项
        if (!ids.isEmpty()) {
            LambdaQueryWrapper<HlvrSopCardStepOpt> optQuery = new LambdaQueryWrapper<>();
            optQuery.in(HlvrSopCardStepOpt::getHscsgId, ids);
            opts = stepOptMapper.selectList(optQuery);
            optIds = opts.stream().map(HlvrSopCardStepOpt::getId).collect(Collectors.toSet());
        }

        //获取所有步骤结果
        List<HlvrSopCardStepResult> results = Collections.emptyList();
        //只有当 步骤id 非空时，才查询步骤结果
        if (!optIds.isEmpty()) {
            LambdaQueryWrapper<HlvrSopCardStepResult> resultQuery = new LambdaQueryWrapper<>();
            resultQuery.in(HlvrSopCardStepResult::getHscsoId, optIds);
            results = resultMapper.selectList(resultQuery);
        }
        //插入操作卡数据
        card.setId(null);
        card.setName(bo.getName());
        card.setCreateBy(null);
        card.setCreateTime(null);

        card.setUpdateBy(null);
        card.setUpdateTime(null);
        card.setSopCardModel(bo.getSopCardModel());
        baseMapper.insert(card);
        //id映射暂存
        Map<Long, Long> groupMap= new HashMap<>();
        Map<Long, Long> optMap= new HashMap<>();
        Map<Long, Long> itemMap= new HashMap<>();
        //插入分组数据
        groups.forEach(group -> {
            long groupId = group.getId();
            group.setId(null);
            group.setHscId(card.getId());

            group.setCreateBy(null);
            group.setCreateTime(null);
            group.setUpdateBy(null);
            group.setUpdateTime(null);
            groupMapper.insert(group);
            groupMap.put(groupId, group.getId());
        });
        //插入步骤选项数据
        opts.forEach(opt -> {
            long optId = opt.getId();
            opt.setHscsgId(groupMap.get(opt.getHscsgId()));
            opt.setId(null);
            opt.setCreateBy(null);
            opt.setCreateTime(null);
            opt.setUpdateBy(null);
            opt.setUpdateTime(null);
            //如果为考核模式，则插入默认得分规则
            if("exam".equals(bo.getSopCardModel())){
                //默认得分规则,可添加到系统配置中，实现参数配置
                opt.setScoringCriteria("completeopt");
                opt.setScore(10);
            }
            stepOptMapper.insert(opt);
            optMap.put(optId, opt.getId());
        });

        //插入步骤结果数据
        results.forEach(result -> {
            result.setHscsoId(optMap.get(result.getHscsoId()));
            if (isQuestionsPopwindow(result)) {
                handleQuestions(result, itemMap, card.getId(), optMap);
            }
            result.setId(null);
            clearFields(result);
            resultMapper.insert(result);
        });
        return true;
    }
    private boolean isQuestionsPopwindow(HlvrSopCardStepResult result) {
        if (!"popwindow".equals(result.getResultType())) {
            return false;
        }
        JSONObject jsonObj = JSONUtil.parseObj(result.getResourceAttr());
        return "questions".equals(jsonObj.get("popwindowType"));
    }
    private void handleQuestions(HlvrSopCardStepResult result, Map<Long, Long> itemMap, Long cardId, Map<Long, Long> optMap) {
        JSONObject jsonObj = JSONUtil.parseObj(result.getResourceAttr());
        Long questionId = jsonObj.getLong("id");
        Long newId = itemMap.get(questionId);

        if (newId != null) {
            jsonObj.set("id", newId.toString());
        } else {
            newId = copyQuestion(questionId, cardId, optMap);
            itemMap.put(questionId, newId);
            jsonObj.set("id", newId.toString());
        }

        result.setResourceAttr(JSONUtil.toJsonStr(jsonObj));
    }
    private Long copyQuestion(Long oldQuestionId, Long cardId, Map<Long, Long> optMap) {
        HlvrQuestion question = questionMapper.selectById(oldQuestionId);
        question.setHscId(cardId);
        question.setId(null);
        clearFields(question);
        questionMapper.insert(question);
        copyQuestionItems(oldQuestionId, question.getId(), optMap);
        return question.getId();
    }
    private void copyQuestionItems(Long oldQuestionId, Long newQuestionId, Map<Long, Long> optMap) {
        LambdaQueryWrapper<HlvrQuestionItem> query = new LambdaQueryWrapper<>();
        query.eq(HlvrQuestionItem::getHqId, oldQuestionId);
        List<HlvrQuestionItem> items = questionItemMapper.selectList(query);

        items.forEach(item -> {
            item.setHqId(newQuestionId);
            item.setHscsoId(optMap.get(item.getHscsoId()));
            item.setId(null);
            clearFields(item);
        });

        if (!items.isEmpty()) {
            questionItemMapper.insertBatch(items);
        }
    }
    private void clearFields(BaseEntity entity) {
        entity.clearFields();
    }

    @Override
    public String selectEntity(Long id) {
        HlvrSopCardVo vo = baseMapper.selectVoById(id);
        return vo != null? vo.getName(): null;
    }
}
