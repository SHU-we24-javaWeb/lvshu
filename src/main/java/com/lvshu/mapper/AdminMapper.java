package com.lvshu.mapper;

import com.lvshu.pojo.Admin;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface AdminMapper {
    
    /**
     * 管理员登录验证
     */
    @Select("SELECT * FROM admin WHERE username = #{username} AND password = #{password}")
    Admin login(@Param("username") String username, @Param("password") String password);
} 