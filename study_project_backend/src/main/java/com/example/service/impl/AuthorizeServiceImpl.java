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
    public boolean sendValidateEmail(String email, String sessionId) {
        String key= "email:" + sessionId+ ":" + email;
        if (template.hasKey(key)){
            Long expire= Optional.of(template.getExpire(key, TimeUnit.SECONDS)).orElse(0L);
            if (expire>120)
                return false;
        }
        Random random = new Random();
        int code = random.nextInt(899999) + 100000;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(email);
        message.setSubject("请查收你的验证邮件");
        message.setText("你的验证码是：" + code);
        try {
            mailSender.send(message);
            template.opsForValue().set(key, String.valueOf(code),3, TimeUnit.MINUTES);
            return true;
        }catch (MailSendException e){
            e.printStackTrace();
            return false;
        }
    }
}
