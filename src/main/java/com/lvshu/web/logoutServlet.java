package com.lvshu.web;

import com.lvshu.mapper.UserMapper;
import com.lvshu.pojo.User;
import com.lvshu.util.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

@WebServlet("/logoutServlet")
public class logoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 设置请求的字符编码为UTF-8,不然会出乱码
        req.setCharacterEncoding("UTF-8");

        // 获取字符输出流，并设置content type
        resp.setContentType("application/json;charset=utf-8");

        // 获取当前session对象，如果没有会话则返回null
        HttpSession session = req.getSession(false);

        if (session != null) {
            session.removeAttribute("user");
        }

        // 获取输出流并构造 JSON 格式响应
        PrintWriter writer = resp.getWriter();
        String jsonResponse = "{\"success\": true, \"message\": \"登出成功\"}"; // 成功登出返回的 JSON
        writer.write(jsonResponse);
        writer.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }

}
