package com.example.mapper;

import com.example.entity.Account;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper {

    @Select("select * from db_account where username=#{text} or email=#{text}")
    Account findAccountByName0rEmail(String text);

    @Insert("insert into db_account(email,username,password) values(#{email},#{username},#{password})")
    int creatAccount(String username, String password, String email);

    @Update("update db_account set password= #{password} where email=#{email}")
    int resetPasswordByEmail(String password, String email);
}
