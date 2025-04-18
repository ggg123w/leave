package com.gxuwz.zjh.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.Alias;

@Data
@Alias("Student") //学生信息
public class Student {

    @ApiModelProperty(value = "学号")
    private String stuId;

    @ApiModelProperty(value = "班号")
    private String classId;

    @ApiModelProperty(value = "姓名")
    private String stuName;

    @ApiModelProperty(value = "性别")
    private int sex;

    @ApiModelProperty(value = "通讯地址")
    private String address;

    @ApiModelProperty(value = "学生电话")
    private String stuTel;

    @ApiModelProperty(value = "联系人")
    private String contact;

    @ApiModelProperty(value = "联系人电话")
    private String contactTel;

    @ApiModelProperty(value = "辅导员工号")
    private String instId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}