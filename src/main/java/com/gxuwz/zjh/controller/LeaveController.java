package com.gxuwz.zjh.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gxuwz.zjh.entity.Leave;
import com.gxuwz.zjh.service.ILeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/zjh/leave")
public class LeaveController {

    @Autowired
    private ILeaveService iLeaveService;

    // 根据是否存在模糊查询内容跳转到不同的分页查询方法
    @ResponseBody
    @GetMapping(value = "/nextPage")
    public ModelAndView nextPage(ModelAndView modelAndView, Leave leave, Integer pageNumber,
                                 Page page, HttpServletRequest request) {
        if(request.getSession().getAttribute("result") != null){
            request.getSession().setAttribute("result", "");
        }
        if(leave.getLeaveId() == null){
            return findLeaveAll(modelAndView, page, pageNumber, request);
        }else {
            return findLeaveById(modelAndView, leave, pageNumber, page, request);
        }
    }

    // 根据请假id查询信息
    @GetMapping(value = "/findLeaveById")
    public ModelAndView findLeaveById(ModelAndView modelAndView, Leave leave, Integer pageNumber,
                                      Page page, HttpServletRequest request) {
        if(request.getSession().getAttribute("result") != null){
            request.getSession().setAttribute("result", "");
        }
        QueryWrapper<Leave> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("leave_id");
        if(leave != null && leave.getLeaveId() != null){
            wrapper.like("leave_id", leave.getLeaveId());
            modelAndView.addObject("leave", leave);
        }
        if(pageNumber == null){
            page.setCurrent(1);
        }else {
            page.setCurrent((long)pageNumber);
        }
        page.setSize(6);
        IPage<Leave> leaveIPage = iLeaveService.selectLeavePage(page, wrapper);
        int[] pagesList = new int[(int)leaveIPage.getPages()];
        for(int i = 0; i < (int)leaveIPage.getPages(); i++){
            pagesList[i] = i + 1;
        }
        modelAndView.addObject("pagesList", pagesList);
        modelAndView.addObject("page", page);
        System.out.println("总条数" + leaveIPage.getTotal());
        System.out.println("总页数" + leaveIPage.getPages());
        modelAndView.addObject("pages", (int)leaveIPage.getPages());
        modelAndView.addObject("numberPages", leaveIPage.getTotal());
        List<Leave> leaveList = leaveIPage.getRecords();
        System.out.println("leaveList = " + leaveList);
        modelAndView.addObject("leaveList", leaveList);

        modelAndView.setViewName("leave/leave_list");
        return modelAndView;
    }

    // 查询全部请假信息
    @GetMapping(value = "/findLeaveAll")
    public ModelAndView findLeaveAll(ModelAndView modelAndView, Page page, Integer pageNumber,
                                     HttpServletRequest request) {
        QueryWrapper<Leave> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("leave_id");
        if(pageNumber == null){
            page.setCurrent(1);
        }else {
            page.setCurrent((long)pageNumber);
        }
        page.setSize(6);
        IPage<Leave> leaveIPage = iLeaveService.selectLeavePage(page, wrapper);
        HttpSession session = request.getSession();
        modelAndView.addObject("page", page);
        System.out.println("总条数" + leaveIPage.getTotal());
        System.out.println("总页数" + leaveIPage.getPages());
        modelAndView.addObject("pages", (int)leaveIPage.getPages());
        int[] pagesList = new int[(int)leaveIPage.getPages()];
        for(int i = 0; i < (int)leaveIPage.getPages(); i++){
            pagesList[i] = i + 1;
        }
        modelAndView.addObject("pagesList", pagesList);
        modelAndView.addObject("numberPages", leaveIPage.getTotal());
        List<Leave> leaveList = leaveIPage.getRecords();
        System.out.println("leaveList = " + leaveList);
        modelAndView.addObject("leaveList", leaveList);

        modelAndView.setViewName("leave/leave_list");
        return modelAndView;
    }

    // 添加请假信息 / 修改请假信息
    @PostMapping(value = "/addEditLeave")
    public String addEditLeave(Leave leave, HttpServletRequest request) {
        Leave leave1 = iLeaveService.findLeaveById(leave);
        System.out.println("leave = " + leave);
        System.out.println("leave1 = " + leave1);
        // 新增请假信息
        if(leave1 == null){
            System.out.println("进入新增请假");
            try {
                iLeaveService.addLeave(leave);
                request.getSession().setAttribute("result", "addTrue");
            } catch (Exception e) {
                request.getSession().setAttribute("result", "addFalse");
            }
        }
        // 修改请假信息
        if(leave1 != null){
            System.out.println("进入修改请假");
            try {
                iLeaveService.updateLeaveById(leave);
                request.getSession().setAttribute("result", "editTrue");
            } catch (Exception e) {
                request.getSession().setAttribute("result", "editFalse");
            }
        }
        return "redirect:/zjh/leave/findLeaveAll";
    }

    // 删除请假信息
    @GetMapping(value = "/deleteLeaveById")
    public String deleteLeaveById(HttpServletRequest request, @RequestParam("leaveId") String leaveId) {
        Leave leave = new Leave();
        leave.setLeaveId(leaveId);
        try {
            iLeaveService.deleteLeaveById(leave);
            request.getSession().setAttribute("result", "deleteTrue");
        } catch (Exception e) {
            // 可以添加日志记录异常信息
        }
        return "redirect:/zjh/leave/findLeaveAll";
    }
}