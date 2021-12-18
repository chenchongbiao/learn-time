package org.jeecg.modules.la.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ActivityApplyVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**主键*/
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
    /**活动名称*/
    @Excel(name = "活动名称", width = 15, dictTable = "activity", dicText = "name", dicCode = "id")
    @Dict(dictTable = "activity", dicText = "name", dicCode = "id")
    @ApiModelProperty(value = "活动名称")
    private java.lang.String activityId;
    /**报名人名称*/
    @Excel(name = "报名人名称", width = 15, dictTable = "sys_user", dicText = "realname", dicCode = "id")
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "id")
    @ApiModelProperty(value = "报名人名称")
    private java.lang.String userId;
    /**活动状态*/
    @Excel(name = "活动状态", width = 15, dicCode = "activity")
    @Dict(dicCode = "activity")
    @ApiModelProperty(value = "活动状态")
    private java.lang.Integer status;
    /**学时类型*/
    @Excel(name = "学时类型", width = 15)
    @ApiModelProperty(value = "学时类型")
    private java.lang.String typeName;
    /**学时数量*/
    @Excel(name = "学时数量", width = 15)
    @ApiModelProperty(value = "学时数量")
    private java.lang.Integer actTime;
    /**
     * 登录账号
     */
    @Excel(name = "登录账号", width = 15)
    private String username;

    /**
     * 真实姓名
     */
    @Excel(name = "真实姓名", width = 15)
    private String realname;

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
