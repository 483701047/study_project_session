package com.example.controller;

import com.example.entity.RestBean;
import com.example.service.AuthorizeService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.Pattern;
import org.apache.ibatis.annotations.Param;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.internal.constraintvalidators.hv.CodePointLengthValidator;
import org.hibernate.validator.internal.constraintvalidators.hv.LengthValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/auth")
public class AuthorizeController {

    private final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private final String USERNAME_REGEX = "^[\\u4e00-\\u9fa5a-zA-Z0-9]+$";

    @Autowired
    AuthorizeService service;

    @PostMapping("/valid_register_email")
    public RestBean<String> validateRegisterEmail(@Pattern(regexp = EMAIL_REGEX) @RequestParam("email") String email,
                                          HttpSession session){
        String s= service.sendValidateEmail(email, session.getId(), false);
        if (s==null){
            return RestBean.success("邮件已发送，请注意查收");
        }else
            return RestBean.failure(400, s);
    }

    @PostMapping("/valid_reset_email")
    public RestBean<String> validateResetEmail(@Pattern(regexp = EMAIL_REGEX) @RequestParam("email") String email,
                                          HttpSession session){
        String s= service.sendValidateEmail(email, session.getId(), true);
        if (s==null){
            return RestBean.success("邮件已发送，请注意查收");
        }else
            return RestBean.failure(400, s);
    }

    @PostMapping("/register")
    public RestBean<String> registerUser(@Pattern(regexp = USERNAME_REGEX) @Length(min = 3, max = 8) @RequestParam("username") String username,
                                         @Length(min = 6, max = 18)@RequestParam("password") String password,
                                         @Pattern(regexp = EMAIL_REGEX) @RequestParam("email") String email,
                                         @Length(min = 6, max = 6)@RequestParam("code") String code,
                                         HttpSession session){
        String s= service.validateAndRegister(username, password, email,code,session.getId());
        if (s==null){
            return RestBean.success("注册成功");
        }else {
            return RestBean.failure(400, s);
        }
    }

    @PostMapping("/start_reset")
    public RestBean<String> startReset(@Pattern(regexp = EMAIL_REGEX) @RequestParam("email") String email,
                                       @Length(min = 6, max = 6) @RequestParam("code") String code,
                                       HttpSession session){

        String s= service.validateOnly(email, code, session.getId());
        if (s==null){
            session.setAttribute("reset_password", email);
            return RestBean.success();
        }else {
            return RestBean.failure(400, s);
        }
    }

    @PostMapping("/do_reset")
    public RestBean<String> resetPassword(@Length(min = 6, max = 18) @RequestParam("password") String password,
                                          HttpSession session){

        String email= (String) session.getAttribute("reset_password");
        if (email==null){
            return RestBean.failure(400, "请先完成邮箱验证");
        }else if(service.resetPassword(password, email)){
            session.removeAttribute("reset_password");
            return RestBean.success("密码重置成功");
        }else {
            return RestBean.failure(500,"内部错误，请联系管理员");
        }
    }
}
