package org.jeecg.modules.learntime.service.impl;

import org.jeecg.modules.learntime.entity.Student;
import org.jeecg.modules.learntime.mapper.StudentMapper;
import org.jeecg.modules.learntime.service.IStudentService;
import org.jeecg.modules.system.entity.SysUserRole;
import org.jeecg.modules.system.mapper.SysUserRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 学生表
 * @Author: jeecg-boot
 * @Date:   2021-12-06
 * @Version: V1.0
 */
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements IStudentService {
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Override
    public void saveUser(Student student, String roleId) {
        // 保存用户
        this.save(student);
        // 保存角色
        SysUserRole userRole = new SysUserRole(student.getId(), roleId);
        sysUserRoleMapper.insert(userRole);
    }
}
