package com.lvshu.web.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lvshu.mapper.GuideMapper;
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

@WebServlet("/adminUpdateGuideStatusServlet")
public class AdminUpdateGuideStatusServlet extends HttpServlet {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 设置请求和响应的编码
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=utf-8");
        PrintWriter writer = resp.getWriter();

        // 获取当前登录用户（从 session 中获取）
        HttpSession session = req.getSession();
        Admin admin = (Admin) session.getAttribute("admin"); // 获取 user
        if (admin == null) {
            // 如果用户没有登录（session 中没有 admin），可以重定向到登录页面
            resp.sendRedirect("admin/login.html");
            return;
        }

        try {
            // 读取JSON请求体
            StringBuilder sb = new StringBuilder();
            String line;
            BufferedReader reader = req.getReader();
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            // 将JSON转换为Map
            Map<String, Object> data = objectMapper.readValue(sb.toString(), Map.class);
            Integer guideId = (Integer) data.get("guideId");
            String status = (String) data.get("status");

            // 更新数据
            SqlSessionFactory sqlSessionFactory = SqlSessionFactoryUtils.getSqlSessionFactory();
            SqlSession sqlSession = sqlSessionFactory.openSession();
            GuideMapper guideMapper = sqlSession.getMapper(GuideMapper.class);

            int result = guideMapper.updateStatus(guideId, status);
            sqlSession.commit();

            if (result > 0) {
                writer.write("{\"success\":true,\"message\":\"状态更新成功\"}");
            } else {
                writer.write("{\"success\":false,\"message\":\"状态更新失败\"}");
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