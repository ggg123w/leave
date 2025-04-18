package com.gxuwz.zjh.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gxuwz.zjh.entity.Student;
import com.gxuwz.zjh.service.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/zjh/student")
public class StudentController {

    @Autowired
    private IStudentService iStudentService;

    // 根据是否存在模糊查询内容跳转到不同的分页查询方法
    @ResponseBody
    @GetMapping(value = "/nextPage")
    public ModelAndView nextPage(ModelAndView modelAndView, Student student, Integer pageNumber,
                                 Page page, HttpServletRequest request) {
        if(request.getSession().getAttribute("result") != null){
            request.getSession().setAttribute("result", "");
        }
        if(student.getStuId() == null){
            return findStudentAll(modelAndView, page, pageNumber, request);
        }else {
            return findStudentById(modelAndView, student, pageNumber, page, request);
        }
    }

    // 根据对应用户id查询信息
    @GetMapping(value = "/findStudentById")
    public ModelAndView findStudentById(ModelAndView modelAndView, Student student, Integer pageNumber,
                                        Page page, HttpServletRequest request) {
        if(request.getSession().getAttribute("result") != null){
            request.getSession().setAttribute("result", "");
        }
        QueryWrapper<Student> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("stu_id");
        if(student != null && student.getStuId() != null){
            wrapper.like("stu_id", student.getStuId());
            modelAndView.addObject("student", student);
        }
        if(pageNumber == null){
            page.setCurrent(1);
        }else {
            page.setCurrent((long)pageNumber);
        }
        page.setSize(6);
        IPage<Student> studentIPage = iStudentService.selectStudentPage(page, wrapper);
        int[] pagesList = new int[(int)studentIPage.getPages()];
        for(int i = 0; i < (int)studentIPage.getPages(); i++){
            pagesList[i] = i + 1;
        }
        modelAndView.addObject("pagesList", pagesList);
        modelAndView.addObject("page", page);
        System.out.println("总条数" + studentIPage.getTotal());
        System.out.println("总页数" + studentIPage.getPages());
        modelAndView.addObject("pages", (int)studentIPage.getPages());
        modelAndView.addObject("numberPages", studentIPage.getTotal());
        List<Student> studentList = studentIPage.getRecords();
        System.out.println("studentList = " + studentList);
        modelAndView.addObject("studentList", studentList);

        modelAndView.setViewName("student/student_list");
        return modelAndView;
    }

    // 查询全部信息
    @GetMapping(value = "/findStudentAll")
    public ModelAndView findStudentAll(ModelAndView modelAndView, Page page, Integer pageNumber,
                                       HttpServletRequest request) {
        QueryWrapper<Student> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("stu_id");
        if(pageNumber == null){
            page.setCurrent(1);
        }else {
            page.setCurrent((long)pageNumber);
        }
        page.setSize(6);
        IPage<Student> studentIPage = iStudentService.selectStudentPage(page, wrapper);
        HttpSession session = request.getSession();
        modelAndView.addObject("page", page);
        System.out.println("总条数" + studentIPage.getTotal());
        System.out.println("总页数" + studentIPage.getPages());
        modelAndView.addObject("pages", (int)studentIPage.getPages());
        int[] pagesList = new int[(int)studentIPage.getPages()];
        for(int i = 0; i < (int)studentIPage.getPages(); i++){
            pagesList[i] = i + 1;
        }
        modelAndView.addObject("pagesList", pagesList);
        modelAndView.addObject("numberPages", studentIPage.getTotal());
        List<Student> studentList = studentIPage.getRecords();
        System.out.println("studentList = " + studentList);
        modelAndView.addObject("studentList", studentList);

        modelAndView.setViewName("student/student_list");
        return modelAndView;
    }

    // 添加信息 / 修改信息
    @PostMapping(value = "/addEditStudent")
    public String addEditStudent(Student student, HttpServletRequest request) {
        Student student1 = iStudentService.findStudentById(student);
        System.out.println("student = " + student);
        System.out.println("student1 = " + student1);
        // 新增学生信息
        if(student1 == null){
            System.out.println("进入新增学生");
            try {
                iStudentService.addStudent(student);
                request.getSession().setAttribute("result", "addTrue");
            } catch (Exception e) {
                request.getSession().setAttribute("result", "addFalse");
            }
        }
        // 修改学生信息
        if(student1 != null){
            System.out.println("进入修改学生");
            try {
                iStudentService.updateStudentById(student);
                request.getSession().setAttribute("result", "editTrue");
            } catch (Exception e) {
                request.getSession().setAttribute("result", "editFalse");
            }
        }
        return "redirect:/zjh/student/findStudentAll";
    }

    // 删除信息
    @GetMapping(value = "/deleteStudentById")
    public String deleteStudentById(HttpServletRequest request, @RequestParam("stuId") String stuId) {
        Student student = new Student();
        student.setStuId(stuId);
        try {
            iStudentService.deleteStudentById(student);
            request.getSession().setAttribute("result", "deleteTrue");
        } catch (Exception e) {
            // 可以添加日志记录异常信息
        }
        return "redirect:/zjh/student/findStudentAll";
    }
}