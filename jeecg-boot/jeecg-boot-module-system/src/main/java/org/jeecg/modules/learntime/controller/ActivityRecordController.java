package org.jeecg.modules.learntime.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.PermissionData;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.ImportExcelUtil;
import org.jeecg.modules.learntime.entity.ActivityRecord;
import org.jeecg.modules.learntime.service.IActivityRecordService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.learntime.utils.ActivityImportExcelUtil;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.ISysDepartService;
import org.jeecg.modules.system.service.ISysDictService;
import org.jeecg.modules.system.service.ISysUserService;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

/**
 * @Description: 活动记录表
 * @Author: bluesky
 * @Date: 2022-01-14
 * @Version: V1.0
 */
@Api(tags = "活动记录表")
@RestController
@RequestMapping("/activityrecord/activityRecord")
@Slf4j
public class ActivityRecordController extends JeecgController<ActivityRecord, IActivityRecordService> {
    @Autowired
    private IActivityRecordService activityRecordService;

    @Value("${jeecg.path.upload}")
    private String upLoadPath;

    @Autowired
    private ISysDictService sysDictService;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private ISysDepartService sysDepartService;

    /**
     * 分页列表查询
     *
     * @param activityRecord
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "活动记录表-分页列表查询")
    @ApiOperation(value = "活动记录表-分页列表查询", notes = "活动记录表-分页列表查询")
    @PermissionData(pageComponent = "learntime/activityrecord/ActivityRecordList")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(ActivityRecord activityRecord,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<ActivityRecord> queryWrapper = QueryGenerator.initQueryWrapper(activityRecord, req.getParameterMap());
        Page<ActivityRecord> page = new Page<ActivityRecord>(pageNo, pageSize);
        IPage<ActivityRecord> pageList = null;
        // 获取登录的用户信息
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (loginUser.getUserIdentity() == 1) {
            // 如果角色是学生，查询登录者的学号，只查询自己的数据
            queryWrapper.eq("uid", loginUser.getUsername());
        }
        if (loginUser.getUserIdentity() == 2 && !"admin".equals(loginUser.getUsername())){
            // 获取登录的用户信息
            SysUser sysUser = sysUserService.getUserByName(loginUser.getUsername());
            pageList = activityRecordService.queryActivityRecordByOrgCodeWithGradePageList(sysUser.getOrgCode(), sysUser.getGrade(),page);
        } else {
            pageList = activityRecordService.page(page, queryWrapper);
        }
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param activityRecord
     * @return
     */
    @AutoLog(value = "活动记录表-添加")
    @ApiOperation(value = "活动记录表-添加", notes = "活动记录表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody ActivityRecord activityRecord) {
        // 通过用户名查找用户
        SysUser sysUser = sysUserService.getUserByName(activityRecord.getUid());
        // 判断学号查找的用户是否为空 并且学号查找到的用户姓名不正确
        if (sysUser == null || !sysUser.getRealname().equals(activityRecord.getName())) {
            return Result.error("学生学号或者姓名填写错误");
        }
        sysUser = activityRecordService.addCreditByActivityRecord(sysUser,activityRecord);
        activityRecordService.save(activityRecord);
        sysUserService.updateById(sysUser);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param activityRecord
     * @return
     */
    @AutoLog(value = "活动记录表-编辑")
    @ApiOperation(value = "活动记录表-编辑", notes = "活动记录表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody ActivityRecord activityRecord) {
        // Todo
        /**
         * 如果修改学时类型也需要把学时计算进行修改
         */
        activityRecordService.updateById(activityRecord);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "活动记录表-通过id删除")
    @ApiOperation(value = "活动记录表-通过id删除", notes = "活动记录表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        return activityRecordService.deleteActivityRecordWithCredit(id);
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "活动记录表-批量删除")
    @ApiOperation(value = "活动记录表-批量删除", notes = "活动记录表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
//        this.activityRecordService.removeByIds(Arrays.asList(ids.split(",")));
        return this.activityRecordService.deleteBatchActvityRecordWithCredit(ids);
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "活动记录表-通过id查询")
    @ApiOperation(value = "活动记录表-通过id查询", notes = "活动记录表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        ActivityRecord activityRecord = activityRecordService.getById(id);
        if (activityRecord == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(activityRecord);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param activityRecord
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ActivityRecord activityRecord) {
        return super.exportXls(request, activityRecord, ActivityRecord.class, "广州商学院“4+X”活动学时认定登记表");
    }

    /**
     * 导出excel模板
     *
     * @param request
     * @param activityRecord
     */
    @RequestMapping(value = "/exportXlsTamp")
    public ModelAndView exportXlsTamp(HttpServletRequest request, ActivityRecord activityRecord) {
        //Step.1 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        // 创建导出的列表
        List<ActivityRecord> pageList = new ArrayList<ActivityRecord>();

        //
        ActivityRecord activityRecordTamp = new ActivityRecord();
        activityRecordTamp.setActivityName("“安全教育”学期总结年级级会");
        activityRecordTamp.setSponsor("信息技术与工程学院");
        activityRecordTamp.setUid("201906050052");
        activityRecordTamp.setName("陈崇标");
        activityRecordTamp.setAcademy("信息技术与工程学院");
        activityRecordTamp.setClazz("软工1901班");
        activityRecordTamp.setJoinType(0);
        activityRecordTamp.setAward("无");
        activityRecordTamp.setCreditType(2);
        activityRecordTamp.setCredit(2.0);
        activityRecordTamp.setContact("伍xx1552xxxxxx");
        activityRecordTamp.setYear(0);
        activityRecordTamp.setToName("伍xx");
        pageList.add(activityRecordTamp);

        //导出文件名称
        mv.addObject(NormalExcelConstants.FILE_NAME, "广州商学院“4+X”活动学时认定登记表");
        mv.addObject(NormalExcelConstants.CLASS, ActivityRecord.class);
        ExportParams exportParams = new ExportParams("广州商学院“4+X”活动学时认定登记表", "导出信息");
        exportParams.setImageBasePath(upLoadPath);
        mv.addObject(NormalExcelConstants.PARAMS, exportParams);
        mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
        return mv;
        //		 return super.exportXls(request, activityRecord, ActivityRecord.class, "活动记录表");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 获取上传对象
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        // 错误信息
        List<String> errorMessage = new ArrayList<>();
        int lineNumber;
        int successLines = 0, errorLines = 0;
        // 存放所有正确结果的列表
        List<ActivityRecord> activityRecordList = new ArrayList<ActivityRecord>();
        // 存放所有需要修改学时数据的学生
        List<SysUser> sysUserList = new ArrayList<SysUser>();
        // 存放通过用户名查找的用户
        SysUser sysUser = new SysUser();
        // 测试时返回计算结果学时后的用户数组
        List<SysUser> listUser = new ArrayList<SysUser>();

        // 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        // 创建导入错误的列表
        List<ActivityRecord> errorList = new ArrayList<ActivityRecord>();


        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile file = entity.getValue();// 获取上传文件对象
            ImportParams params = new ImportParams();
            // 表格标题所占据的行数,默认0，代表没有标题
            params.setTitleRows(1);
            // 表头所占据的行数行数,默认1，代表标题占据一行
            params.setHeadRows(1);
            params.setNeedSave(true);

            /**
             * 修改导入逻辑
             */
            try {
                // 获取导入的所有数据存入数组
                List<ActivityRecord> listActivityRecord = ExcelImportUtil.importExcel(file.getInputStream(), ActivityRecord.class, params);
                // 找出学号重复的记录
                List<String> dupList = listActivityRecord.stream().collect(Collectors.groupingBy(ActivityRecord::getUid, Collectors.counting()))
                        .entrySet().stream().filter(e -> e.getValue() > 1)
                        .map(Map.Entry::getKey).collect(Collectors.toList());

                for (int i = 0; i < listActivityRecord.size(); i++) {
                    // 获取当前记录
                    ActivityRecord activityRecord = listActivityRecord.get(i);
                    // 通过学号查找学生
                    sysUser = sysUserService.getUserByName(activityRecord.getUid());
                    // 判断学号查找的用户是否为空 或者与学号查找到的用户姓名不一样
                    if (sysUser == null || !sysUser.getRealname().equals(activityRecord.getName())) {
                        errorLines++;
                        // 数据行从第3行开始
                        lineNumber = i + 3;
                        errorMessage.add(activityRecord.getActivityName() + "导入发生异常：第" + lineNumber + "行用户学号或者姓名填写错误");
                        activityRecord.setRemark("学号或者姓名填写错误");
                        errorList.add(activityRecord);
                    } else if (activityRecord.getYear() == null){
					   // 判断学年是否填写
						errorLines++;
						// 数据行从第3行开始
						lineNumber = i + 3;
						errorMessage.add(activityRecord.getActivityName() + "发生异常：第" + lineNumber + "行学年为空");
                        activityRecord.setRemark("学年为空");
                        errorList.add(activityRecord);
                    } else if (activityRecord.getActivityName() == null) {
                        // 判断活动名是否填写
                        errorLines++;
                        // 数据行从第3行开始
                        lineNumber = i + 3;
                        errorMessage.add(activityRecord.getActivityName() + "发生异常：第" + lineNumber + "行活动名为空");
                        activityRecord.setRemark("活动名为空");
                        errorList.add(activityRecord);
                    } else if(activityRecord.getAcademy() == null){
                        // 判断活学院是否填写
                        errorLines++;
                        // 数据行从第3行开始
                        lineNumber = i + 3;
                        errorMessage.add(activityRecord.getActivityName() + "发生异常：第" + lineNumber + "行学院名为空");
                        activityRecord.setRemark("学院名为空");
                        errorList.add(activityRecord);
                    } else {
                        // 如果数据没有错误
                        // 保存活动记录并且把学时加入到学生中
                        // activityRecordService.addCreditByActivityRecord(sysUser,activityRecord);
                        // 获取登录用户
                        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
                        // 把记录的创建人和创建时间创建部门补全
                        activityRecord.setCreateBy(loginUser.getUsername());
                        activityRecord.setCreateTime(new Date());
                        activityRecord.setSysOrgCode(loginUser.getOrgCode());
                        activityRecord.setRemark("");
                        // 把学时记录计算到学生上重新对sysUser赋值
                        sysUser = activityRecordService.addCreditByActivityRecord(sysUser,activityRecord);
                        activityRecordList.add(activityRecord);
                        sysUserList.add(sysUser);
                        successLines++;
                        log.info((i + 3) + "行 =====> " +activityRecord);
                    }
                }

                // 如果数据完全正确，进行批量导入活动以及修改学生学时数据
                activityRecordService.addCreditByActivityRecordBatch(sysUserList,activityRecordList);

            } catch (Exception e) {
				errorMessage.add("发生异常：" + e.getMessage());
                log.error(e.getMessage(), e);
            } finally {
                try {
                    file.getInputStream().close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }

        // 进行到结尾successLines errorLines都为0的情况下判断可能上传的文件填写错误，导致字典转储错误
 		if (successLines == 0 && errorLines == 0 && errorMessage.size() == 1){
			errorLines++;
			errorMessage.add("发生异常：" +  "请检查学时类型、参加类型、学院、学年数据是否填写错误\n");
			errorMessage.add("学时类型填写：\n\t创新创业素质\n\t思想品德素质\n\t身心素质\n\t法律素养 \n\t 文体素质\n");
			errorMessage.add("参加类型填写：\n\t参加者\n\t工作人员\n\t观众\n");
			errorMessage.add("学院填写： 信息技术与工程学院\n");
			errorMessage.add("学年填写：2021-2022学年");
            return ImportExcelUtil.imporReturnRes(errorLines, successLines, errorMessage);
		}
        return ActivityImportExcelUtil.imporReturnXls(errorLines, successLines, errorList);
    }


    /**
     * 通过excel导入数据,导入旧数据时导入的逻辑，
     *
     * @param request
     * @param response
     * @return
     */
//    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExceldev(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 获取上传对象
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        // 错误信息
        List<String> errorMessage = new ArrayList<>();
        int lineNumber;
        int successLines = 0, errorLines = 0;
        // 存放所有正确结果的列表
        List<ActivityRecord> activityRecordList = new ArrayList<ActivityRecord>();
        // 存放所有需要修改学时数据的学生
        List<SysUser> sysUserList = new ArrayList<SysUser>();
        // 存放通过用户名查找的用户
        SysUser sysUser = new SysUser();
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        // 测试时返回计算结果学时后的用户数组
        List<SysUser> listUser = new ArrayList<SysUser>();
        // 存储错误数据的数组
        List<Integer> listError = new ArrayList<Integer>();

        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile file = entity.getValue();// 获取上传文件对象
            ImportParams params = new ImportParams();
            // 表格标题所占据的行数,默认0，代表没有标题
            params.setTitleRows(1);
            // 表头所占据的行数行数,默认1，代表标题占据一行
            params.setHeadRows(1);
            params.setNeedSave(true);

            /**
             * 修改导入逻辑
             */
            try {
                // 获取导入的所有数据存入数组
                List<ActivityRecord> listActivityRecord = ExcelImportUtil.importExcel(file.getInputStream(), ActivityRecord.class, params);
                // 找出学号重复的记录
                List<String> dupList = listActivityRecord.stream().collect(Collectors.groupingBy(ActivityRecord::getUid, Collectors.counting()))
                        .entrySet().stream().filter(e -> e.getValue() > 1)
                        .map(Map.Entry::getKey).collect(Collectors.toList());

                for (int i = 0; i < listActivityRecord.size(); i++) {
                    ActivityRecord activityRecord = listActivityRecord.get(i);

                    // 通过用户名查找用户
                    sysUser = sysUserService.getUserByName(activityRecord.getUid());

                    // 判断学号查找的用户是否为空 并且学号查找到的用户姓名不正确
//                    if (sysUser == null || !sysUser.getRealname().equals(activityRecord.getName())) {
                    if (sysUser == null) {
                        errorLines++;
                        // 数据行从第3行开始
                        lineNumber = i + 3;
//                        errorMessage.add(activityRecord.getActivityName() + "导入发生异常：第" + lineNumber + "行用户学号或者姓名填写错误");
                        errorMessage.add(activityRecord.getActivityName() + "导入发生异常：第" + lineNumber + "行用户学号不存在");
                        listError.add(lineNumber);

                        /** 旧数据有数据缺失，导入时暂时注释掉 开始*/

//                    } else if (
//                    .size() > 0) {
//						for(String dup:dupList){
//							if (sysUser.getUsername().equals(dup)) {
//								errorLines++;
//								// 数据行从第3行开始
//								int lineNumber = i + 3;
//								errorMessage.add(activityRecord.getActivityName() + "发生异常：第" + lineNumber + "行数据重复");
//							}
//						}
//					} else if (activityRecord.getYear() == null){
//					   // 判断学年是否填写
//						errorLines++;
//						// 数据行从第3行开始
//						int lineNumber = i + 3;
//						errorMessage.add(activityRecord.getActivityName() + "发生异常：第" + lineNumber + "行学年为空");
                        /** 结束 */

                    } else {
                        // 导入旧数据使用借助realname找到用户
                        // 加一层判断如果查到通过学号查找到的用户与其中一个与活动中班级对上就把正确的结果复制给活动记录
//                        List<ActivityRecord> listActivityByUid = activityRecordService.list(new QueryWrapper<ActivityRecord>().eq("uid",activityRecord.getUid()).like("clazz",activityRecord.getClazz()));
//                        List<SysUser> listUserByRealname = sysUserService.list(new QueryWrapper<SysUser>().eq("realname",activityRecord.getName()).like("clazz",activityRecord.getClazz()));


                        // 加入标志变量flag如果flag为1则通过名字查找可以匹配到正确学号，为0表示不可以
                        int flag = 0;
                        // 使用学号查找到的用户如果名字与活动记录中的不符合那么进行修复
                        if (!sysUser.getRealname().equals(activityRecord.getName())){
                            if (activityRecord.getClazz().equals(sysUser.getClazz())){
                                flag = 1;
                                activityRecord.setName(sysUser.getRealname());
                            }
                            // 通过姓名查到用户，匹配里面班级正确的数据
                            if (flag == 0){
                                List<SysUser> listUserByRealname = sysUserService.list(new QueryWrapper<SysUser>().eq("realname",activityRecord.getName()).like("clazz",activityRecord.getClazz()));
                                for(SysUser sysUserByRealname:listUserByRealname){
                                    // 如果活动中的班级与星梦号查出的用户的班级相同，把用姓名查出的用户的姓名复制给活动记录
                                    if(activityRecord.getClazz().equals(sysUserByRealname.getClazz())){
                                        // 如果活动中的班级与用姓名查出的以往活动的班级相同，把用姓名查出的活动记录的学号复制给活动记录
                                        flag = 1;
                                        activityRecord.setUid(sysUserByRealname.getUsername());
                                        break;
                                    }
                                }
                            }

                            // 通过姓名查到以往数据，匹配里面班级正确的数据
                            if (flag == 0){
                                List<ActivityRecord> listActivityByRealname = activityRecordService.list(new QueryWrapper<ActivityRecord>().eq("name",activityRecord.getName()).like("clazz",activityRecord.getClazz()));
                                for(ActivityRecord activityRecordByRealname:listActivityByRealname){
                                    // 如果活动中的班级与用学号查出的用户的班级相同，把用学号查出的用户的姓名复制给活动记录
                                    if(activityRecord.getClazz().equals(activityRecordByRealname.getClazz())){
                                        // 如果活动中的班级与用姓名查出的以往活动的班级相同，把用姓名查出的活动记录的学号复制给活动记录
                                        flag = 1;
                                        activityRecord.setUid(activityRecordByRealname.getUid());
                                        break;
                                    }
                                }
                            }


                            // 通过前面姓名查到对不上的数据，此时通过学号来查找
                            if (flag == 0){
                                List<ActivityRecord> listActivityByUid = activityRecordService.list(new QueryWrapper<ActivityRecord>().eq("uid",activityRecord.getUid()).like("clazz",activityRecord.getClazz()));
                                // 通过学号查到以往数据，匹配里面班级正确的数据
                                for(ActivityRecord activityRecordByUid:listActivityByUid){
                                    if(activityRecord.getClazz().equals(activityRecordByUid.getClazz())){
                                        // 如果活动中的班级与用学号查出的以往活动的班级相同，把用学号查出的活动记录的姓名复制给活动记录
                                        flag = 1;
                                        activityRecord.setName(activityRecordByUid.getName());
                                        break;
                                    }
                                }
                            }

                        }else{
                            flag = 1;
                        }



                        if (flag == 0){
                            // 通过学号和姓名查找都错误那么判断为数据错误
                            errorLines++;
                            // 数据行从第3行开始
                            lineNumber = i + 3;
                            errorMessage.add(activityRecord.getActivityName() + "导入发生异常：第" + lineNumber + "行用户学号或者姓名填写错误");
                            listError.add(lineNumber);
                        }else{
                            // 如果数据没有错误
                            // 保存活动记录并且把学时加入到学生中
//                            activityRecordService.addCreditByActivityRecord(sysUser,activityRecord);
                            activityRecord.setCreateBy(loginUser.getUsername());
                            activityRecord.setCreateTime(new Date());
                            activityRecord.setSysOrgCode(loginUser.getOrgCode());

                            // 把学时记录计算到学生上重新对sysUser赋值
                            sysUser = activityRecordService.addCreditByActivityRecord(sysUser,activityRecord);
                            activityRecordList.add(activityRecord);
                            sysUserList.add(sysUser);
                            successLines++;
//                            System.out.println((i + 3) + "行 =====> " +activityRecord);
                            log.info(String.valueOf(i+3));
                        }
                    }
                }

                // 进行批量导入活动以及修改学生学时数据
                activityRecordService.addCreditByActivityRecordBatch(sysUserList,activityRecordList);

                for(int error: listError){
                    System.out.println(error);
                }


                // 第一遍循环只做验证不做导入，如果第一遍循环没有检查出错误，第二次循环把数据导入
                // errorLines为0 errorMessage为0的情况下判断则代表验证数据是正确的
//                if (errorLines == 0 && errorMessage.size() == 0){
//                    for(ActivityRecord activityRecord:listActivityRecord){
//                        // 保存活动记录并且把学时加入到学生中
//                        activityRecordService.addCreditByActivityRecord(sysUser,activityRecord);
//                        successLines++;
//                        System.out.println(activityRecord);
//                    }
//                }
            } catch (Exception e) {
                errorMessage.add("发生异常：" + e.getMessage());
                log.error(e.getMessage(), e);
            } finally {
                try {
                    file.getInputStream().close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }

        // 进行到结尾successLines errorLines都为0的情况下判断可能上传的文件填写错误，导致字典转储错误
        if (successLines == 0 && errorLines == 0 && errorMessage.size() >= 0){
            errorLines++;
            errorMessage.add("发生异常：" +  "请检查学时类型、参加类型、学院、学年数据是否填写错误\n");
            errorMessage.add("学时类型填写：\n\t创新创业素质 \n\t思想品德素质 \n\t 身心素质 \n\t 法律素养 \n\t 文体素质\n");
            errorMessage.add("参加类型填写：\n\t参加者 \n\t工作人员 \n\t 观众 \n");
            errorMessage.add("学院填写： 信息技术与工程学院\n");
            errorMessage.add("学年填写：2021-2022学年");
        }
        return ImportExcelUtil.imporReturnRes(errorLines, successLines, errorMessage);
    }

}
