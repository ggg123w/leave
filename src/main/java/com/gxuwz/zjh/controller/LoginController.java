package com.gxuwz.zjh.controller;

import com.gxuwz.zjh.entity.Classes;
import com.gxuwz.zjh.entity.Course;
import com.gxuwz.zjh.entity.Department;
import com.gxuwz.zjh.entity.User;
import com.gxuwz.zjh.service.IClassesService;
import com.gxuwz.zjh.service.ICourseService;
import com.gxuwz.zjh.service.IDepService;
import com.gxuwz.zjh.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

@Controller
public class LoginController {

    @Autowired
    private IUserService iUserService;
    @Autowired
    private IClassesService iClassesService;
    @Autowired
    private IDepService iDepService;
    @Autowired
    private ICourseService iCourseService;

    @GetMapping("/toLoginPage")
    public String toLoginPage(Model model){
        model.addAttribute("currentYear", Calendar.getInstance().get(Calendar.YEAR));
        return "login";
    }

    //默认跳转登录页面
    @GetMapping("/")
    public ModelAndView loginStart(ModelAndView modelAndView){
        modelAndView.setViewName("login");
        return modelAndView;
    }

    // 登录判断
    @PostMapping("/login.action")
    public ModelAndView loginIndex(User user, ModelAndView modelAndView,
                                   HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(user.getUserId() == null){
            request.setAttribute("result", "none1");
            request.getRequestDispatcher("/login.html").forward(request, response);
        }else if(user.getPassword() == null | user.getPassword() == "" ){
            request.setAttribute("result", "none2");
            request.getRequestDispatcher("/login.html").forward(request, response);
        }else {
            User user2 = iUserService.findUserById(user);
            List<Classes> classesList = iClassesService.findClassesAll();
            List<Department> deptList = iDepService.findDepAll();
            List<Course> courseList = iCourseService.findCourseAll();
            if (user2 == null) {
                request.setAttribute("result", "full");
                request.getRequestDispatcher("/login.html").forward(request, response);
            } else {
                if (user2.getPassword().equals(user.getPassword())) {
                    request.getSession().setAttribute("user", user2);
                    request.getSession().setAttribute("classesList", classesList);
                    request.getSession().setAttribute("deptList", deptList);
                    request.getSession().setAttribute("courseList", courseList);
                    modelAndView.setViewName("index");
                    return modelAndView;
                }
            }
        }

        modelAndView.setViewName("login");
        return modelAndView;
    }

    // 跳转注册页面
    @GetMapping("/registerPage")
    public String registerPage() {
        return "register";
    }

    // 跳转忘记密码页面
    @GetMapping("/forgetPasswordPage")
    public String forgetPasswordPage() {
        return "forgetPassword";
    }

    // 处理注册请求
    @PostMapping("/register")
    public String register(@RequestParam("userId") String userId, @RequestParam("password") String password, HttpServletRequest request) {
        User user = new User();
        user.setUserId(userId);
        user.setPassword(password);
        try {
            iUserService.addUser(user);
            request.setAttribute("result", "registerSuccess");
        } catch (Exception e) {
            request.setAttribute("result", "registerFail");
        }
        return "login";
    }

    // 处理忘记密码请求
    @PostMapping("/forgetPassword")
    public String forgetPassword(@RequestParam("userId") String userId, @RequestParam("newPassword") String newPassword, HttpServletRequest request) {
        User user = new User();
        user.setUserId(userId);
        User existingUser = iUserService.findUserById(user);
        if (existingUser != null) {
            existingUser.setPassword(newPassword);
            try {
                iUserService.updateUserById(existingUser);
                request.setAttribute("result", "resetSuccess");
            } catch (Exception e) {
                request.setAttribute("result", "resetFail");
            }
        } else {
            request.setAttribute("result", "userNotFound");
        }
        return "login";
    }

    //测试页面List
    @GetMapping("/formTestList")
    public ModelAndView formTestList(ModelAndView modelAndView){
        modelAndView.setViewName("test/test_list");
        return modelAndView;
    }
}