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
@Alias("Course") //课程
public class Course {

    @ApiModelProperty(value = "课程名称")//添加额外描述信息
    private String courseId;

    @ApiModelProperty(value = "班号")
    private String classId;

    @ApiModelProperty(value = "课程名称")
    private String courseName;

    @ApiModelProperty(value = "学年")
    private String year;

    @ApiModelProperty(value = "学期")
    private String term;

    @ApiModelProperty(value = "学时")
    private String hour;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}
