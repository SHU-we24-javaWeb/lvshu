package com.lvshu.web;

import com.lvshu.mapper.UserMapper;
import com.lvshu.pojo.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

@WebServlet("/registerServlet")
public class registerServlet  extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 设置请求的字符编码为UTF-8,不然会出乱码
        req.setCharacterEncoding("UTF-8");

        // 1.接收用户名和密码
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        // 封装user对象
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        // 2.调用MyBatis完成查询
        // 这里直接去官网复制粘贴过来
        // 2.1 获取SqlSessionFactory对象
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        // 2.2 获取SqlSession对象
        SqlSession sqlSession = sqlSessionFactory.openSession();
        // 2.3 获取Mapper
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        // 2.4 调用方法
        // 传入用户名和密码
        User user1 = userMapper.selectUserByUsername(username);


        // 获取字符输出流,并设置content type
        resp.setContentType("text/html;charset=utf-8");
        PrintWriter writer = resp.getWriter();
        // 3.判断user1是否为null:有没有重复的用户名
        if (user1 == null) {
            // 用户名不存在，添加用户
            userMapper.insertUser(user);
            // 提交事务
            sqlSession.commit();
            // 释放资源
            sqlSession.close();

            // 登陆成功，跳转到主页或其他页面
            writer.write("<html><body>");
            writer.write("<h1>注册成功！</h1>");
            writer.write("<script>window.alert(\"注册成功，去登陆咯\");</script>");
            writer.write("<script>window.location.href='login.html';</script>"); // 登录成功后跳转
            writer.write("</body></html>");
        } else {
            // 用户名存在，给出提示信息
            // 注册失败，弹出用户名重复提示框
            writer.write("<html><body>");
            writer.write("<h1>注册失败！</h1>");
            writer.write("<script>alert('用户名已存在！'); window.history.back();</script>"); // 弹窗并返回上一步
            writer.write("</body></html>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }

    public String encodeString(String str) {
        return new String(str.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    }

}
