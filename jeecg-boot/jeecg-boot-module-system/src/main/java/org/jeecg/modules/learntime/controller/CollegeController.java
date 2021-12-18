package org.jeecg.modules.learntime.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.learntime.entity.College;
import org.jeecg.modules.learntime.service.ICollegeService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 学院管理
 * @Author: jeecg-boot
 * @Date:   2021-12-06
 * @Version: V1.0
 */
@Api(tags="学院管理")
@RestController
@RequestMapping("/college/college")
@Slf4j
public class CollegeController extends JeecgController<College, ICollegeService> {
	@Autowired
	private ICollegeService collegeService;
	
	/**
	 * 分页列表查询
	 *
	 * @param college
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "学院管理-分页列表查询")
	@ApiOperation(value="学院管理-分页列表查询", notes="学院管理-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(College college,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<College> queryWrapper = QueryGenerator.initQueryWrapper(college, req.getParameterMap());
		Page<College> page = new Page<College>(pageNo, pageSize);
		IPage<College> pageList = collegeService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param college
	 * @return
	 */
	@AutoLog(value = "学院管理-添加")
	@ApiOperation(value="学院管理-添加", notes="学院管理-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody College college) {
		collegeService.save(college);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param college
	 * @return
	 */
	@AutoLog(value = "学院管理-编辑")
	@ApiOperation(value="学院管理-编辑", notes="学院管理-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody College college) {
		collegeService.updateById(college);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "学院管理-通过id删除")
	@ApiOperation(value="学院管理-通过id删除", notes="学院管理-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		collegeService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "学院管理-批量删除")
	@ApiOperation(value="学院管理-批量删除", notes="学院管理-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.collegeService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "学院管理-通过id查询")
	@ApiOperation(value="学院管理-通过id查询", notes="学院管理-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		College college = collegeService.getById(id);
		if(college==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(college);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param college
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, College college) {
        return super.exportXls(request, college, College.class, "学院管理");
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
        return super.importExcel(request, response, College.class);
    }

}
