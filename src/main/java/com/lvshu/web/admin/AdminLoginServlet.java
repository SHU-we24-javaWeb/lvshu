package com.lvshu.web.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lvshu.mapper.AdminMapper;
import com.lvshu.pojo.Admin;
import com.lvshu.util.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@WebServlet("/adminLoginServlet")
public class AdminLoginServlet extends HttpServlet {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 设置请求和响应的编码
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=utf-8");
        PrintWriter writer = resp.getWriter();

        try {
            // 读取JSON请求体
            StringBuilder sb = new StringBuilder();
            String line;
            BufferedReader reader = req.getReader();
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            // 将JSON字符串转换为Map
            Map<String, String> loginData = objectMapper.readValue(sb.toString(), Map.class);
            String username = loginData.get("username");
            String password = loginData.get("password");

            // 调用MyBatis完成查询
            SqlSessionFactory sqlSessionFactory = SqlSessionFactoryUtils.getSqlSessionFactory();
            SqlSession sqlSession = sqlSessionFactory.openSession();
            AdminMapper adminMapper = sqlSession.getMapper(AdminMapper.class);
            
            // 查询管理员
            Admin admin = adminMapper.login(username, password);

            // 判断admin是否为null
            if (admin != null) {
                // 登录成功，将admin存入session
                HttpSession session = req.getSession();
                session.setAttribute("admin", admin);
                
                writer.write("{\"success\":true,\"message\":\"登录成功\"}");
            } else {
                // 登录失败
                writer.write("{\"success\":false,\"message\":\"用户名或密码错误\"}");
            }

            sqlSession.close();
        } catch (Exception e) {
            writer.write("{\"success\":false,\"message\":\"" + e.getMessage() + "\"}");
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }
} 