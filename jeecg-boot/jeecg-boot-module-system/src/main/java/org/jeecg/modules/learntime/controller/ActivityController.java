package org.jeecg.modules.learntime.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.learntime.entity.Activity;
import org.jeecg.modules.learntime.service.IActivityService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 活动表
 * @Author: jeecg-boot
 * @Date:   2021-12-07
 * @Version: V1.0
 */
@Api(tags="活动表")
@RestController
@RequestMapping("/learntime/activity")
@Slf4j
public class ActivityController extends JeecgController<Activity, IActivityService> {
	@Autowired
	private IActivityService activityService;
	
	/**
	 * 分页列表查询
	 *
	 * @param activity
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "活动表-分页列表查询")
	@ApiOperation(value="活动表-分页列表查询", notes="活动表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(Activity activity,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Activity> queryWrapper = QueryGenerator.initQueryWrapper(activity, req.getParameterMap());
		Page<Activity> page = new Page<Activity>(pageNo, pageSize);
		IPage<Activity> pageList = activityService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param activity
	 * @return
	 */
	@AutoLog(value = "活动表-添加")
	@ApiOperation(value="活动表-添加", notes="活动表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody Activity activity) {
		activityService.save(activity);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param activity
	 * @return
	 */
	@AutoLog(value = "活动表-编辑")
	@ApiOperation(value="活动表-编辑", notes="活动表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody Activity activity) {
		activityService.updateById(activity);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "活动表-通过id删除")
	@ApiOperation(value="活动表-通过id删除", notes="活动表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		activityService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "活动表-批量删除")
	@ApiOperation(value="活动表-批量删除", notes="活动表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.activityService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "活动表-通过id查询")
	@ApiOperation(value="活动表-通过id查询", notes="活动表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		Activity activity = activityService.getById(id);
		if(activity==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(activity);
	}


    /**
    * 导出excel
    *
    * @param request
    * @param activity
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Activity activity) {
        return super.exportXls(request, activity, Activity.class, "活动表");
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
        return super.importExcel(request, response, Activity.class);
    }

}
