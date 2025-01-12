package com.example.service.impl;

import com.example.entity.Account;
import com.example.mapper.UserMapper;
import com.example.service.AuthorizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.MailSendException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class AuthorizeServiceImpl implements AuthorizeService {

    @Value("${spring.mail.username}")
    String from;

    @Autowired
    StringRedisTemplate template;

    @Autowired
    UserMapper mapper;

    @Autowired
    MailSender mailSender;

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = mapper.findAccountByName0rEmail(username);
        if (username == null)
            throw new UsernameNotFoundException("用户名不能为空");
        if(account == null)
            throw new UsernameNotFoundException("用户名或密码错误");
        return User
                .withUsername(account.getUsername())
                .password(account.getPassword())
                .roles("user")
                .build();
    }

    @Override
    public String sendValidateEmail(String email, String sessionId,boolean hasAccount) {
        String key= "email:" + sessionId+ ":" + email + ":" + hasAccount;
        if (template.hasKey(key)){
            Long expire= Optional.of(template.getExpire(key, TimeUnit.SECONDS)).orElse(0L);
            if (expire>120)
                return "请求频繁，稍后再试！";
        }
        Account account=mapper.findAccountByName0rEmail(email);
        if(account==null&&hasAccount) return "此邮箱未被注册";
        if (account!=null&&!hasAccount) return "此邮箱已被注册！";
        Random random = new Random();
        int code = random.nextInt(899999) + 100000;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(email);
        message.setSubject("请查收你的验证邮件");
        message.setText("你的验证码是：" + code+"，有效期为3分钟，请注意查收");
        try {
            mailSender.send(message);
            template.opsForValue().set(key, String.valueOf(code),3, TimeUnit.MINUTES);
            return null;
        }catch (MailSendException e){
            e.printStackTrace();
            return "邮件发送失败，请检查邮件地址是否有效";
        }
    }

    @Override
    public String validateAndRegister(String username, String password, String email, String code, String sessionId) {
        String key="email:"+sessionId+":"+email + ":false";
        if (Boolean.TRUE.equals(template.hasKey(key))){
            String s=template.opsForValue().get(key);
            if (s==null)
                return "验证码已过期，请重新获取";
            if (s.equals(code)){
                password=encoder.encode(password);
                template.delete(key);
                if (mapper.creatAccount(username,password,email)>0){
                    return null;
                }else {
                    return "内部错误，请联系管理员";
                }
            }else {
                return "验证码错误";
            }
        }else {
            return "请先获取验证码";
        }
    }

    @Override
    public String validateOnly(String email, String code, String sessionId) {

        String key="email:"+sessionId+":"+email + ":true";
        if (Boolean.TRUE.equals(template.hasKey(key))){
            String s=template.opsForValue().get(key);
            if (s==null) return "验证码已过期，请重新获取";
            if (s.equals(code)){
                template.delete(key);
                return null;
            }else {
                return "验证码错误";
            }
        }else {
            return "请先获取验证码";
        }
    }

    @Override
    public boolean resetPassword(String password, String email) {
        password=encoder.encode(password);
        return mapper.resetPasswordByEmail(password,email)>0;
    }
}
