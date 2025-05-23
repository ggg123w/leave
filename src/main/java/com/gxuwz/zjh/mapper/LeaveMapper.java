package com.gxuwz.zjh.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gxuwz.zjh.entity.Leave;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Mapper
@Repository
public interface LeaveMapper {

    // 根据 id 查询对应用户信息
    Leave findLeaveById(Leave leave);

    List<Leave> findLeaveByStuNo(Leave leave);

    // 根据辅导员工号查询该辅导员负责审批的请假信息
    List<Leave> findLeavesByInstId(@Param("instId") String instId);

    // 查询全部用户信息
    List<Leave> findLeaveAll();

    //自定义sql 分页
    IPage<Leave> selectLeavePage(Page<Leave> page, @Param(Constants.WRAPPER) Wrapper<Leave> wrapper);

    // 添加用户信息
    void addLeave(Leave leave);

    // 修改用户信息
    void updateLeaveById(Leave leave);

    // 删除用户信息
    void deleteLeaveById(Leave leave);

}
