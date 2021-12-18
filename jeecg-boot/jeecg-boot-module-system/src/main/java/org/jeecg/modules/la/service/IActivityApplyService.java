package org.jeecg.modules.la.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import org.jeecg.modules.la.entity.ActivityApply;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.la.vo.ActivityApplyVo;


/**
 * @Description: 活动报名
 * @Author: jeecg-boot
 * @Date:   2021-12-07
 * @Version: V1.0
 */
public interface IActivityApplyService extends IService<ActivityApply> {
    /**
     * 查询参加过的互动
     * @param userId
     * @param activityId
     * @return
     */
    IPage<ActivityApplyVo> queryJoinActivityPageByUserIdAndAcitvityId(String userId, String activityId, int pageSize, int pageNo);

    /**
     * 查询参加过的互动
     * @param userId
     * @return
     */
    Integer querySumLearnTimeByUserId(String userId);
}
