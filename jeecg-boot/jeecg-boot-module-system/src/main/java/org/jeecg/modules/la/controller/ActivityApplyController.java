package org.jeecg.modules.la.controller;

import java.util.Arrays;
import java.util.HashMap;
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
import org.jeecg.modules.la.entity.ActivityApply;
import org.jeecg.modules.la.service.IActivityApplyService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.la.vo.ActivityApplyVo;
import org.jeecg.modules.learntime.entity.Activity;
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
 * @Description: 活动报名
 * @Author: jeecg-boot
 * @Date:   2021-12-07
 * @Version: V1.0
 */
@Api(tags="活动报名")
@RestController
@RequestMapping("/la/activityApply")
@Slf4j
public class ActivityApplyController extends JeecgController<ActivityApply, IActivityApplyService> {
	@Autowired
	private IActivityApplyService activityApplyService;
	
	/**
	 * 分页列表查询
	 *
	 * @param activityApply
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "活动报名-分页列表查询")
	@ApiOperation(value="活动报名-分页列表查询", notes="活动报名-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(ActivityApply activityApply,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ActivityApply> queryWrapper = QueryGenerator.initQueryWrapper(activityApply, req.getParameterMap());
		Page<ActivityApply> page = new Page<ActivityApply>(pageNo, pageSize);
		IPage<ActivityApply> pageList = activityApplyService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	 /**
	  * 分页列表查询
	  *
	  * @param activityApply
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "活动信息-分页列表查询活动信息")
	 @ApiOperation(value="活动信息-分页列表查询", notes="活动信息-分页列表查询")
	 @GetMapping(value = "/getlist")
	 public Result<?> getPageList(ActivityApply activityApply,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									HttpServletRequest req) {
		 IPage<ActivityApplyVo> pageList= activityApplyService.queryJoinActivityPageByUserIdAndAcitvityId(activityApply.getUserId(),activityApply.getActivityId(),pageSize,pageNo);
		 return Result.OK(pageList);
	 }

	/**
	 *   添加
	 *
	 * @param activityApply
	 * @return
	 */
	@AutoLog(value = "活动报名-添加")
	@ApiOperation(value="活动报名-添加", notes="活动报名-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ActivityApply activityApply) {
		activityApplyService.save(activityApply);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param activityApply
	 * @return
	 */
	@AutoLog(value = "活动报名-编辑")
	@ApiOperation(value="活动报名-编辑", notes="活动报名-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody ActivityApply activityApply) {
		activityApplyService.updateById(activityApply);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "活动报名-通过id删除")
	@ApiOperation(value="活动报名-通过id删除", notes="活动报名-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		activityApplyService.removeById(id);
		return Result.OK("删除成功!");
	}

	 /**
	  * 通过用户id查询
	  *
	  * @param userId
	  * @return
	  */
	 @AutoLog(value = "学时统计-通过用户id查询")
	 @ApiOperation(value="学时统计-通过用户id查询", notes="学时统计-通过用户id查询")
	 @GetMapping(value = "/querySumLearnTimeByUserId")
	 public Result<?> querySumLearnTimeByUserId(@RequestParam(name="userId",required=true) String userId) {
		 Integer result = activityApplyService.querySumLearnTimeByUserId(userId);
		 return Result.ok(result);
	 }

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "活动报名-批量删除")
	@ApiOperation(value="活动报名-批量删除", notes="活动报名-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.activityApplyService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "活动报名-通过id查询")
	@ApiOperation(value="活动报名-通过id查询", notes="活动报名-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		ActivityApply activityApply = activityApplyService.getById(id);
		if(activityApply==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(activityApply);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param activityApply
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ActivityApply activityApply) {
        return super.exportXls(request, activityApply, ActivityApply.class, "活动报名");
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
        return super.importExcel(request, response, ActivityApply.class);
    }

}
