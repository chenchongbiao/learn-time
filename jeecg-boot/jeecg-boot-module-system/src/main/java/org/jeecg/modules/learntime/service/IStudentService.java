package org.jeecg.modules.learntime.service;

import org.jeecg.modules.learntime.entity.Student;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 学生表
 * @Author: jeecg-boot
 * @Date:   2021-12-06
 * @Version: V1.0
 */
public interface IStudentService extends IService<Student> {
    /**
     * 保存用户
     * @param student 用户
     * @param roleId 选择的角色id，多个以逗号隔开
     */
    void saveUser(Student student, String roleId);
}
