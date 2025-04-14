package com.gxuwz.zjh.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gxuwz.zjh.entity.Classes;
import com.gxuwz.zjh.entity.User;
import com.gxuwz.zjh.service.IClassesService;
import com.gxuwz.zjh.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/zjh/classes")
public class ClassesController {

    @Autowired
    private IClassesService iClassesService;

    //根据是否存在模糊查询内容跳转到不同的分页查询方法


    @ResponseBody
    @GetMapping(value = "/nextPage")
    public ModelAndView nextPage(ModelAndView modelAndView, Classes classes, Integer pageNumber,
                                 Page page, HttpServletRequest request) {
        if(request.getSession().getAttribute("result") != null){
            request.getSession().setAttribute("result", "");
        }//清理或重置会话中的"result"属性，以确保每次请求都能以干净的状态开始。
        if(classes.getClassId() == null){
            return findClassesAll(modelAndView, page, pageNumber, request);//如果没有查询id，就查询全部
        }else {
            return findClassesById(modelAndView, classes, pageNumber, page, request);//如果查询id，就查询
        }
    }

    // 根据对应id查询信息


    @GetMapping(value = "/findClassesById")
    public ModelAndView findClassesById(ModelAndView modelAndView, Classes classes, Integer pageNumber,
                                     Page page, HttpServletRequest request) {
        if(request.getSession().getAttribute("result") != null){
            request.getSession().setAttribute("result", "");
        }
        // 可以通过 wrapper 进行筛选!!!
        QueryWrapper<Classes> wrapper = new QueryWrapper<>();//MyBatis Plus提供的查询构造器，用于构建动态查询条件。
        wrapper.orderByAsc("class_id");//指定查询结果按class_id字段升序排列。
        String[] keysWord = null;
        // 对User进行模糊查询!!!
        if(classes != null && classes.getClassId() != null){
            wrapper.like("class_id", classes.getClassId());
            modelAndView.addObject("classes", classes);//将classes对象添加到模型中，供前端使用。
        }

        // Current,页码 + Size,每页条数
        if(pageNumber == null){
            page.setCurrent(1);
        }else {
            page.setCurrent((long)pageNumber);
        }
        // 默认每页6行数据！
        page.setSize(6);
        // 调用分页查询方法
        IPage<Classes> classesIPage = iClassesService.selectClassesPage(page, wrapper);
        // 存放一个数组用来让foreach遍历
        int[] pagesList = new int[(int)classesIPage.getPages()];
        for(int i=0; i< (int)classesIPage.getPages(); i++){
            pagesList[i] = i+1;
        }
        modelAndView.addObject("pagesList", pagesList);
        // 存放page，内有当前页数
        modelAndView.addObject("page", page);
        System.out.println("总条数"+classesIPage.getTotal());
        System.out.println("总页数"+classesIPage.getPages());
        // 存放总页数
        modelAndView.addObject("pages", (int)classesIPage.getPages());//添加总页数。
        modelAndView.addObject("numberPages", classesIPage.getTotal());//添加总记录数
        List<Classes> classesList = classesIPage.getRecords();
        System.out.println("classesList = "+classesList);
        modelAndView.addObject("classesList", classesList);

        modelAndView.setViewName("classes/classes_list");
        return modelAndView;
    }

    //查询全部用户信息

    @GetMapping(value = "/findClassesAll")
    public ModelAndView findClassesAll(ModelAndView modelAndView, Page page, Integer pageNumber,
                                    HttpServletRequest request) {
        // 可以通过 wrapper 进行筛选
        QueryWrapper<Classes> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("class_id");
        // Current,页码 + Size,每页条数
        if(pageNumber == null){
            page.setCurrent(1);
        }else {
            page.setCurrent((long)pageNumber);
        }
        // 默认每页6行数据！
        page.setSize(6);
        // 调用分页查询方法！!
        IPage<Classes> classesIPage = iClassesService.selectClassesPage(page, wrapper);
        HttpSession session = request.getSession();
        // 存放page，内有当前页数
        modelAndView.addObject("page", page);
        System.out.println("总条数"+classesIPage.getTotal());
        System.out.println("总页数"+classesIPage.getPages());
        // 存放总页数
        modelAndView.addObject("pages", (int)classesIPage.getPages());
        // 存放一个数组用来让foreach遍历
        int[] pagesList = new int[(int)classesIPage.getPages()];
        for(int i=0; i< (int)classesIPage.getPages(); i++){
            pagesList[i] = i+1;
        }
        modelAndView.addObject("pagesList", pagesList);
        modelAndView.addObject("numberPages", classesIPage.getTotal());
        List<Classes> classesList = classesIPage.getRecords();
        System.out.println("classesList = "+classesList);
        modelAndView.addObject("classesList", classesList);

        modelAndView.setViewName("classes/classes_list");
        return modelAndView;
    }

    //添加用户信息 / 修改用户信息

    @PostMapping(value = "/addEditClasses")
    public String addEditClasses(Classes classes, HttpServletRequest request) {
        Classes classes1 = iClassesService.findClassesById(classes);

        // 新增用户信息
        if(classes1 == null){
            System.out.println("进入新增用户");
            try {
                iClassesService.addClasses(classes);
                request.getSession().setAttribute("result", "addTrue");
            }catch (Exception e){
                request.getSession().setAttribute("result", "addFalse");
            }
        }
        // 修改用户信息
        if(classes1 != null){
            System.out.println("进入修改用户");
            try {
                iClassesService.updateClassesById(classes);
                request.getSession().setAttribute("result", "editTrue");
            }catch (Exception e){
                request.getSession().setAttribute("result", "editFalse");
            }
        }
        return "redirect:/zjh/classes/findClassesAll";
    }

//删除用户信息

    @GetMapping(value = "/deleteClassesById")
    public String deleteClassesById(HttpServletRequest request, String classId) {
        Classes classes = new Classes();
        classes.setClassId(classId);
        try {
            iClassesService.deleteClassesById(classes);
            request.getSession().setAttribute("result", "deleteTrue");
        }catch (Exception e){

        }
        return "redirect:/zjh/classes/findClassesAll";
    }

}
