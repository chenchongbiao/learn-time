package org.jeecg.modules.learntime.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.learntime.entity.ActivityRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.system.entity.SysAnnouncement;
import org.jeecg.modules.system.entity.SysUser;

import java.util.List;

/**
 * @Description: 活动记录表
 * @Author: jeecg-boot
 * @Date:   2022-01-14
 * @Version: V1.0
 */
public interface ActivityRecordMapper extends BaseMapper<ActivityRecord> {
    /**
     * 批量导入活动记录
     * @param activityRecordList
     * @return
     */
    int insertActivityRecordBatch(List<ActivityRecord> activityRecordList);

    /**
     * 批量修改用户的学时
     * @param sysUserList
     * @return
     */
    int updateSysUserCredit(@Param("sysUserList") List<SysUser> sysUserList);

    /**
     *
     * @param page
     * @param orgCode
     * @param grade
     * @return
     */
    List<ActivityRecord> getActivityRecordByGradeWithOrgCode(IPage page, @Param("orgCode")String orgCode, @Param("grade")Integer grade);

    public Integer getActivityRecordByOrgCodeWithGradeTotal(@Param("orgCode") String orgCode, @Param("grade")Integer grade);
}
