package org.jeecg.modules.learntime.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.learntime.entity.Major;
import org.jeecg.modules.learntime.service.IMajorService;

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
 * @Description: 专业
 * @Author: jeecg-boot
 * @Date:   2021-12-06
 * @Version: V1.0
 */
@Api(tags="专业表")
@RestController
@RequestMapping("/major/major")
@Slf4j
public class MajorController extends JeecgController<Major, IMajorService> {
	@Autowired
	private IMajorService majorService;
	
	/**
	 * 分页列表查询
	 *
	 * @param major
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "专业-分页列表查询")
	@ApiOperation(value="专业-分页列表查询", notes="专业-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(Major major,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Major> queryWrapper = QueryGenerator.initQueryWrapper(major, req.getParameterMap());
		Page<Major> page = new Page<Major>(pageNo, pageSize);
		IPage<Major> pageList = majorService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param major
	 * @return
	 */
	@AutoLog(value = "专业-添加")
	@ApiOperation(value="专业-添加", notes="专业-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody Major major) {
		majorService.save(major);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param major
	 * @return
	 */
	@AutoLog(value = "专业-编辑")
	@ApiOperation(value="专业-编辑", notes="专业-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody Major major) {
		majorService.updateById(major);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "专业-通过id删除")
	@ApiOperation(value="专业-通过id删除", notes="专业-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		majorService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "专业-批量删除")
	@ApiOperation(value="专业-批量删除", notes="专业-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.majorService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "专业-通过id查询")
	@ApiOperation(value="专业-通过id查询", notes="专业-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		Major major = majorService.getById(id);
		if(major==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(major);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param major
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Major major) {
        return super.exportXls(request, major, Major.class, "专业");
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
        return super.importExcel(request, response, Major.class);
    }

}
