package org.jeecg.modules.learntime.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.learntime.entity.Instructions;
import org.jeecg.modules.learntime.service.IInstructionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
* @Description: 功能说明
* @Author: jeecg-boot
* @Date:   2022-03-01
* @Version: V1.0
*/
@Api(tags="功能说明")
@RestController
@RequestMapping("/learntime/instructions")
@Slf4j
public class InstructionsController extends JeecgController<Instructions, IInstructionsService> {
   @Autowired
   private IInstructionsService instructionsService;

   /**
    * 分页列表查询
    *
    * @param instructions
    * @param pageNo
    * @param pageSize
    * @param req
    * @return
    */
   @AutoLog(value = "功能说明-分页列表查询")
   @ApiOperation(value="功能说明-分页列表查询", notes="功能说明-分页列表查询")
   @GetMapping(value = "/list")
   public Result<?> queryPageList(Instructions instructions,
                                  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                  HttpServletRequest req) {
       QueryWrapper<Instructions> queryWrapper = QueryGenerator.initQueryWrapper(instructions, req.getParameterMap());
       // 获取发送请求的用户
       LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
       if (user.getUserIdentity() == 1) {
           // 如果角色是学生，查询登录者的学号，只查询userIdentity为1的数据，即普通用户可查看的数据
           queryWrapper.eq("user_identity", user.getUserIdentity());
       }
       // 否则可以查看所有数据
       Page<Instructions> page = new Page<Instructions>(pageNo, pageSize);
       IPage<Instructions> pageList = instructionsService.page(page, queryWrapper);
       return Result.OK(pageList);
   }

   /**
    *   添加
    *
    * @param instructions
    * @return
    */
   @AutoLog(value = "功能说明-添加")
   @ApiOperation(value="功能说明-添加", notes="功能说明-添加")
   @PostMapping(value = "/add")
   public Result<?> add(@RequestBody Instructions instructions) {
       instructionsService.save(instructions);
       return Result.OK("添加成功！");
   }

   /**
    *  编辑
    *
    * @param instructions
    * @return
    */
   @AutoLog(value = "功能说明-编辑")
   @ApiOperation(value="功能说明-编辑", notes="功能说明-编辑")
   @PutMapping(value = "/edit")
   public Result<?> edit(@RequestBody Instructions instructions) {
       instructionsService.updateById(instructions);
       return Result.OK("编辑成功!");
   }

   /**
    *   通过id删除
    *
    * @param id
    * @return
    */
   @AutoLog(value = "功能说明-通过id删除")
   @ApiOperation(value="功能说明-通过id删除", notes="功能说明-通过id删除")
   @DeleteMapping(value = "/delete")
   public Result<?> delete(@RequestParam(name="id",required=true) String id) {
       instructionsService.removeById(id);
       return Result.OK("删除成功!");
   }

   /**
    *  批量删除
    *
    * @param ids
    * @return
    */
   @AutoLog(value = "功能说明-批量删除")
   @ApiOperation(value="功能说明-批量删除", notes="功能说明-批量删除")
   @DeleteMapping(value = "/deleteBatch")
   public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
       this.instructionsService.removeByIds(Arrays.asList(ids.split(",")));
       return Result.OK("批量删除成功!");
   }

   /**
    * 通过id查询
    *
    * @param id
    * @return
    */
   @AutoLog(value = "功能说明-通过id查询")
   @ApiOperation(value="功能说明-通过id查询", notes="功能说明-通过id查询")
   @GetMapping(value = "/queryById")
   public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
       Instructions instructions = instructionsService.getById(id);
       if(instructions==null) {
           return Result.error("未找到对应数据");
       }
       return Result.OK(instructions);
   }

   /**
   * 导出excel
   *
   * @param request
   * @param instructions
   */
   @RequestMapping(value = "/exportXls")
   public ModelAndView exportXls(HttpServletRequest request, Instructions instructions) {
       return super.exportXls(request, instructions, Instructions.class, "功能说明");
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
       return super.importExcel(request, response, Instructions.class);
   }

}
