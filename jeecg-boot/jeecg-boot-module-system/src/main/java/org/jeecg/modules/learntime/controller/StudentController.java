package org.jeecg.modules.learntime.controller;

import java.util.Arrays;
import java.util.Date;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.PasswordUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.base.service.BaseCommonService;
import org.jeecg.modules.learntime.entity.Student;
import org.jeecg.modules.learntime.service.IStudentService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.system.entity.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 学生表
 * @Author: jeecg-boot
 * @Date:   2021-12-06
 * @Version: V1.0
 */
@Api(tags="学生表")
@RestController
@RequestMapping("/student/student")
@Slf4j
public class StudentController extends JeecgController<Student, IStudentService> {
	@Autowired
	private IStudentService studentService;
	@Resource
	private BaseCommonService baseCommonService;
	/**
	 * 分页列表查询
	 *
	 * @param student
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "学生表-分页列表查询")
	@ApiOperation(value="学生表-分页列表查询", notes="学生表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(Student student,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Student> queryWrapper = QueryGenerator.initQueryWrapper(student, req.getParameterMap());
		Page<Student> page = new Page<Student>(pageNo, pageSize);
		IPage<Student> pageList = studentService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param student
	 * @return
	 */
	@AutoLog(value = "学生表-添加")
	@ApiOperation(value="学生表-添加", notes="学生表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody Student student) {
		// 最终返回结果
		Result<Student> result = new Result<Student>();
		try {
			// 设置创建时间
			student.setCreateTime(new Date());
			// 创建8位随机盐
			String salt = oConvertUtils.randomGen(8);
			// 设置随机盐
			student.setSalt(salt);
			// 生成密码
			String passwordEncode = PasswordUtil.encrypt(student.getUsername(), student.getPassword(), salt);
			// 设置密码
			student.setPassword(passwordEncode);
			// 学生角色的id
			String roleId = "1467848978170368002";
			studentService.saveUser(student,roleId);
			// studentService.save(student);
		}catch (Exception e){
			log.error(e.getMessage(), e);
			result.error500("操作失败");
		}

		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param student
	 * @return
	 */
	@AutoLog(value = "学生表-编辑")
	@ApiOperation(value="学生表-编辑", notes="学生表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody Student student) {
		Result<Student> result = new Result<Student>();
		try {
			Student user = studentService.getById(student.getId());
		}catch (Exception e){
			log.error(e.getMessage(), e);
			result.error500("操作失败");
		}
		studentService.updateById(student);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "学生表-通过id删除")
	@ApiOperation(value="学生表-通过id删除", notes="学生表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		studentService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "学生表-批量删除")
	@ApiOperation(value="学生表-批量删除", notes="学生表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.studentService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "学生表-通过id查询")
	@ApiOperation(value="学生表-通过id查询", notes="学生表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		Student student = studentService.getById(id);
		if(student==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(student);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param student
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Student student) {
        return super.exportXls(request, student, Student.class, "学生表");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, Student.class);
    }

}
