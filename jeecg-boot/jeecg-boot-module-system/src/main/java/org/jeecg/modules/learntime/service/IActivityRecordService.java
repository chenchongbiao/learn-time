package org.jeecg.modules.learntime.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.learntime.entity.ActivityRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.system.entity.SysUser;

import java.util.List;

/**
 * @Description: 活动记录表
 * @Author: jeecg-boot
 * @Date:   2022-01-14
 * @Version: V1.0
 */
public interface IActivityRecordService extends IService<ActivityRecord> {
    /**
     * 通过活动id删除活动记录以及学时
     * @param id
     * @return
     */
    public Result<?> deleteActivityRecordWithCredit(String id);

    /**
     * 通过活动ids批量删除活动记录以及学生的学时
     * @param ids
     * @return
     */
    public Result<?> deleteBatchActvityRecordWithCredit(String ids);

    /**
     * 通过活动记录给学生加入学时
     * @param sysUser
     * @param activityRecord
     */
    public SysUser addCreditByActivityRecord(SysUser sysUser,ActivityRecord activityRecord);

    /**
     * 批量导入活动记录以及修改学时数量
     * @param sysUserList
     * @param activityRecordList
     */
    public void addCreditByActivityRecordBatch(List<SysUser> sysUserList,List<ActivityRecord> activityRecordList);

    /**
     * 通过部门编号以及年级查找列表
     * @param ordCode
     * @param grade
     * @param page
     * @return
     */
    public IPage<ActivityRecord> queryActivityRecordByOrgCodeWithGradePageList(String ordCode, Integer grade,Page<ActivityRecord> page);

}
