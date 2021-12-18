package org.jeecg.modules.learntime.service.impl;

import org.jeecg.modules.learntime.entity.Activity;
import org.jeecg.modules.learntime.mapper.ActivityMapper;
import org.jeecg.modules.learntime.service.IActivityService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 活动表
 * @Author: jeecg-boot
 * @Date:   2021-12-07
 * @Version: V1.0
 */
@Service
public class ActivityServiceImpl extends ServiceImpl<ActivityMapper, Activity> implements IActivityService {

}
