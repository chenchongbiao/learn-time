package org.jeecg.modules.learntime.entity;

import java.io.Serializable;
import java.util.Date;

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
 * @Description: 学生表
 * @Author: jeecg-boot
 * @Date:   2021-12-06
 * @Version: V1.0
 */
@Data
@TableName("student")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="student对象", description="学生表")
public class Student implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;
	/**登录账号(学生学号)*/
	@Excel(name = "登录账号(学生学号)", width = 15)
    @ApiModelProperty(value = "登录账号(学生学号)")
    private String username;
	/**登录密码*/
	@Excel(name = "登录密码", width = 15)
    @ApiModelProperty(value = "登录密码")
    private String password;
	/**md5密码盐*/
	@Excel(name = "md5密码盐", width = 15)
    @ApiModelProperty(value = "md5密码盐")
    private String salt;
	/**学生姓名*/
	@Excel(name = "学生姓名", width = 15)
    @ApiModelProperty(value = "学生姓名")
    private String realname;
	/**学院*/
	@Excel(name = "学院", width = 15, dictTable = "college", dicText = "name", dicCode = "id")
	@Dict(dictTable = "college", dicText = "name", dicCode = "id")
    @ApiModelProperty(value = "学院")
    private String college;
	/**专业*/
	@Excel(name = "专业", width = 15, dictTable = "major", dicText = "name", dicCode = "id")
	@Dict(dictTable = "major", dicText = "name", dicCode = "id")
    @ApiModelProperty(value = "专业")
    private String major;
	/**年级*/
	@Excel(name = "年级", width = 15, dictTable = "grade", dicText = "name", dicCode = "name")
	@Dict(dictTable = "grade", dicText = "name", dicCode = "name")
    @ApiModelProperty(value = "年级")
    private String grade;
	/**班级*/
	@Excel(name = "班级", width = 15)
    @ApiModelProperty(value = "班级")
    private Integer clbum;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private Date createTime;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private Date updateTime;
}
