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
 * @Description: 活动表
 * @Author: jeecg-boot
 * @Date:   2021-12-07
 * @Version: V1.0
 */
@Data
@TableName("activity")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="activity对象", description="活动表")
public class Activity implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**活动发布者*/
	@Excel(name = "活动发布者", width = 15, dictTable = "sys_user", dicText = "realname", dicCode = "id")
	@Dict(dictTable = "sys_user", dicText = "realname", dicCode = "id")
    @ApiModelProperty(value = "活动发布者")
    private java.lang.String adminId;
	/**学时类型*/
	@Excel(name = "学时类型", width = 15, dictTable = "learn_time_type", dicText = "name", dicCode = "id")
	@Dict(dictTable = "learn_time_type", dicText = "name", dicCode = "id")
    @ApiModelProperty(value = "学时类型")
    private java.lang.String typeId;
	/**活动名称*/
	@Excel(name = "活动名称", width = 15)
    @ApiModelProperty(value = "活动名称")
    private java.lang.String name;
	/**报名最大人数*/
	@Excel(name = "报名最大人数", width = 15)
    @ApiModelProperty(value = "报名最大人数")
    private java.lang.Integer applyMax;
	/**学时数量*/
	@Excel(name = "学时数量", width = 15)
    @ApiModelProperty(value = "学时数量")
    private java.lang.Integer activityTime;
	/**活动报名开始时间*/
	@Excel(name = "活动报名开始时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "活动报名开始时间")
    private java.util.Date startTime;
	/**活动报名结束时间*/
	@Excel(name = "活动报名结束时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "活动报名结束时间")
    private java.util.Date endTime;
	/**活动地点*/
	@Excel(name = "活动地点", width = 15)
    @ApiModelProperty(value = "活动地点")
    private java.lang.String address;
	/**活动封面图片*/
	@Excel(name = "活动封面图片", width = 15)
    @ApiModelProperty(value = "活动封面图片")
    private java.lang.String photos;
	/**已报名数量*/
	@Excel(name = "已报名数量", width = 15)
    @ApiModelProperty(value = "已报名数量")
    private java.lang.Integer applyCount;
	/**活动状态*/
	@Excel(name = "活动状态", width = 15, dicCode = "activity")
	@Dict(dicCode = "activity")
    @ApiModelProperty(value = "活动状态")
    private java.lang.Integer status;
	/**活动信息介绍*/
	@Excel(name = "活动信息介绍", width = 15)
    @ApiModelProperty(value = "活动信息介绍")
    private java.lang.String intro;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
}
