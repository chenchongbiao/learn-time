package org.jeecg.modules.la.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.la.entity.ActivityApply;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.la.vo.ActivityApplyVo;

/**
 * @Description: 活动报名
 * @Author: jeecg-boot
 * @Date:   2021-12-07
 * @Version: V1.0
 */
public interface ActivityApplyMapper extends BaseMapper<ActivityApply> {
    /**
     * @功能： 获取所有信息
     * @param userId
     * @param activityId
     * @return
     */
    IPage<ActivityApplyVo> getActivitiyApplyListByUserIdAndActId(Page<ActivityApplyVo> page, @Param("userId") String userId, @Param("activityId") String activityId);

    /**
     * @功能： 统计用户的学时
     * @param userId
     * @return
     */
    Integer getSumLearnTimeByUserId(@Param("userId") String userId);
}
