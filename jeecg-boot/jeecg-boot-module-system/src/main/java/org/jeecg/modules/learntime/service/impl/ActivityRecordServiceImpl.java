package org.jeecg.modules.learntime.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.la.vo.ActivityApplyVo;
import org.jeecg.modules.learntime.entity.ActivityRecord;
import org.jeecg.modules.learntime.mapper.ActivityRecordMapper;
import org.jeecg.modules.learntime.service.IActivityRecordService;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.mapper.SysUserMapper;
import org.jeecg.modules.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 活动记录表
 * @Author: jeecg-boot
 * @Date:   2022-01-14
 * @Version: V1.0
 */
@Service
public class ActivityRecordServiceImpl extends ServiceImpl<ActivityRecordMapper, ActivityRecord> implements IActivityRecordService {
    @Autowired
    private  ActivityRecordMapper activityRecordMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private ISysUserService sysUserService;

    @Override
    public SysUser addCreditByActivityRecord(SysUser sysUser,ActivityRecord activityRecord) {
        switch (activityRecord.getCreditType()) {
            case 0:
                // 思想道德素质
                sysUser.setThought(sysUser.getThought() + activityRecord.getCredit());
                break;
            case 1:
                // 法律素养
                sysUser.setLaw(sysUser.getLaw() + activityRecord.getCredit());
                break;
            case 2:
                // 身心素质
                sysUser.setBodyMind(sysUser.getBodyMind() + activityRecord.getCredit());
                break;
            case 3:
                // 创新创业素质
                sysUser.setInnovation(sysUser.getInnovation() + activityRecord.getCredit());
                break;
            case 4:
                // 文体素质
                sysUser.setCultureSports(sysUser.getCultureSports() + activityRecord.getCredit());
        }
        sysUser.setTotal(sysUser.getTotal() + activityRecord.getCredit());
//        // 修改用户数据
//        sysUserMapper.updateById(sysUser);
//        // 保存活动记录
//        activityRecordMapper.insert(activityRecord);
        return sysUser;
    }
    /**
     * 删除活动记录以及学时
     * @param id
     * @return
     */
    @Override
    public Result<?> deleteActivityRecordWithCredit(String id) {
        ActivityRecord activityRecord;
        // 找到对应的学时记录
        activityRecord = activityRecordMapper.selectById(id);
        // 通过用户名查找用户
        SysUser sysUser = sysUserMapper.getUserByName(activityRecord.getUid());
        // 匹配学士类型，讲用户的学时减去
        switch (activityRecord.getCreditType()) {
            case 0:
                // 思想道德素质
                sysUser.setThought(sysUser.getThought() - activityRecord.getCredit());
                break;
            case 1:
                // 法律素养
                sysUser.setLaw(sysUser.getLaw() - activityRecord.getCredit());
                break;
            case 2:
                // 身心素质
                sysUser.setBodyMind(sysUser.getBodyMind() - activityRecord.getCredit());
                break;
            case 3:
                // 创新创业素质
                sysUser.setInnovation(sysUser.getInnovation() - activityRecord.getCredit());
                break;
            default:
                // 文体素质
                sysUser.setCultureSports(sysUser.getCultureSports() - activityRecord.getCredit());
        }
        sysUser.setCultureSports(sysUser.getTotal() - activityRecord.getCredit());
        // 修改用户数据
        sysUserMapper.updateById(sysUser);
        // 删除学时记录
        this.baseMapper.deleteById(id);
        return Result.OK("删除成功!");
    }

    @Override
    public Result<?> deleteBatchActvityRecordWithCredit(String ids) {
        System.out.println(Arrays.asList(ids.split(",")));
        for(String id:Arrays.asList(ids.split(","))){
            // 取出id分别删除
            deleteActivityRecordWithCredit(id);
        }
        this.baseMapper.deleteBatchIds(Arrays.asList(ids.split(",")));
        return Result.OK("删除成功!");
    }

    /**
     * 将活动记录和修改的学生数据批量导入
     * @param sysUserList
     * @param activityRecordList
     */
    @Override
    public void addCreditByActivityRecordBatch(List<SysUser> sysUserList, List<ActivityRecord> activityRecordList) {
        this.baseMapper.insertActivityRecordBatch(activityRecordList);
//        this.baseMapper.updateSysUserCredit(sysUserList);
//        sysUserMapper.updateSysUserCredit(sysUserList);
        sysUserService.updateBatchById(sysUserList);
    }

    /**
     * 通过部门编号以及年级查找列表
     * @param ordCode
     * @param grade
     * @param page
     * @return
     */
    @Override
    public IPage<ActivityRecord> queryActivityRecordByOrgCodeWithGradePageList(String ordCode, Integer grade, Page<ActivityRecord> page) {

        List<ActivityRecord> list = baseMapper.getActivityRecordByGradeWithOrgCode(page, ordCode, grade);
        Integer total = baseMapper.getActivityRecordByOrgCodeWithGradeTotal(ordCode, grade);
        IPage<ActivityRecord> result = new Page<>(page.getCurrent(), page.getSize(),total);
        result.setRecords(list);
        return result;

    }
}
