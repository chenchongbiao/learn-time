package org.jeecg.modules.la.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.la.entity.ActivityApply;
import org.jeecg.modules.la.mapper.ActivityApplyMapper;
import org.jeecg.modules.la.service.IActivityApplyService;
import org.jeecg.modules.la.vo.ActivityApplyVo;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description: 活动报名
 * @Author: jeecg-boot
 * @Date:   2021-12-07
 * @Version: V1.0
 */
@Service
public class ActivityApplyServiceImpl extends ServiceImpl<ActivityApplyMapper, ActivityApply> implements IActivityApplyService {

    /**
     * 根据用户id和活动id查询活动记录
     */
    @Override
    public IPage<ActivityApplyVo> queryJoinActivityPageByUserIdAndAcitvityId(String userId, String activityId, int pageSize, int pageNo) {
        IPage<ActivityApplyVo> pageList = null;
        Page<ActivityApplyVo> page = new Page<ActivityApplyVo>(pageNo,pageSize);
        pageList = this.baseMapper.getActivitiyApplyListByUserIdAndActId(page,userId,activityId);
        return pageList;
    }
    /**
     * 根据用户id计算总学时
     */
    @Override
    public Integer querySumLearnTimeByUserId(String userId) {
        Integer sumLearnTime = this.baseMapper.getSumLearnTimeByUserId(userId);
        System.out.println(sumLearnTime);
        return sumLearnTime;
    }
}
