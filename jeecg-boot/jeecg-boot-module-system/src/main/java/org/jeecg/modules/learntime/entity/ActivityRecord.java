package org.jeecg.modules.learntime.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 活动记录表
 * @Author: jeecg-boot
 * @Date:   2022-01-14
 * @Version: V1.0
 */
@Data
@TableName("activity_record")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="activity_record对象", description="活动记录表")
public class ActivityRecord implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**活动名称*/
	@Excel(name = "活动名称", width = 15)
    @ApiModelProperty(value = "活动名称")
    private java.lang.String activityName;
	/**主办方学院*/
//	@Excel(name = "主办方学院", width = 15, dictTable = "sys_depart", dicText = "depart_name", dicCode = "id")
//	@Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "id")
    @Excel(name = "主办方", width = 15)
    @ApiModelProperty(value = "主办方")
    private java.lang.String sponsor;
    /**姓名*/
    @Excel(name = "姓名", width = 15)
    @ApiModelProperty(value = "姓名")
    private java.lang.String name;
	/**学号*/
	@Excel(name = "学号", width = 15)
    @ApiModelProperty(value = "学号")
    private java.lang.String uid;
	/**所在学院*/
	@Excel(name = "学院", width = 15, dictTable = "sys_depart", dicText = "depart_name", dicCode = "id")
	@Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "id")
    @ApiModelProperty(value = "学院")
    private java.lang.String academy;
	/**班级*/
	@Excel(name = "班级", width = 15)
    @ApiModelProperty(value = "班级")
    private java.lang.String clazz;
	/**参加类型*/
	@Excel(name = "参加类型", width = 15, dicCode = "join_type")
	@Dict(dicCode = "join_type")
    @ApiModelProperty(value = "参加类型")
    private java.lang.Integer joinType;
	/**获奖情况*/
	@Excel(name = "获奖情况", width = 15)
    @ApiModelProperty(value = "获奖情况")
    private java.lang.String award;
	/**学时类型*/
	@Excel(name = "认定项目", width = 15, dicCode = "learn_time_type")
	@Dict(dicCode = "learn_time_type")
    @ApiModelProperty(value = "认定项目")
    private java.lang.Integer creditType;
	/**学时数量*/
	@Excel(name = "认定活动时", width = 15)
    @ApiModelProperty(value = "认定活动时")
    private java.lang.Double credit;
	/**填报人及联系方式*/
	@Excel(name = "填报人及联系方式", width = 20)
    @ApiModelProperty(value = "填报人及联系方式")
    private java.lang.String contact;
	/**审核人*/
	@Excel(name = "审核人", width = 15)
    @ApiModelProperty(value = "审核人")
    private java.lang.String toName;
    /**备注*/
    @Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String remark;
    /**学年*/
    @Excel(name = "归属年度(如“2020-2021学年”)", width = 30, dicCode = "year")
    @Dict(dicCode = "year")
    @ApiModelProperty(value = "归属年度(如“2020-2021学年”)")
    private java.lang.Integer year;
    /**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private java.lang.String sysOrgCode;
}
