package com.lvshu.web.admin;

import com.lvshu.mapper.GuideMapper;
import com.lvshu.mapper.GuideCommentMapper;
import com.lvshu.mapper.UserMapper;
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
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/adminStatisticsServlet")
public class AdminStatisticsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 设置编码
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
            // 查询数据
            SqlSessionFactory sqlSessionFactory = SqlSessionFactoryUtils.getSqlSessionFactory();
            SqlSession sqlSession = sqlSessionFactory.openSession();
            
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            GuideMapper guideMapper = sqlSession.getMapper(GuideMapper.class);
            GuideCommentMapper commentMapper = sqlSession.getMapper(GuideCommentMapper.class);

            // 获取各种统计数据
            int userCount = userMapper.countTotal();
            int guideCount = guideMapper.countTotal();
            int viewCount =guideMapper.countAllViewCount();
            int commentCount = commentMapper.countTotal();

            // 构建返回的JSON
            StringBuilder json = new StringBuilder();
            json.append("{")
                .append("\"success\":true,")
                .append("\"data\":{")
                .append("\"userCount\":").append(userCount).append(",")
                .append("\"guideCount\":").append(guideCount).append(",")
                .append("\"viewCount\":").append(viewCount).append(",")
                .append("\"commentCount\":").append(commentCount)
                .append("}}");
            
            writer.write(json.toString());
            sqlSession.close();
        } catch (Exception e) {
            writer.write("{\"success\":false,\"message\":\"" + e.getMessage() + "\"}");
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }
} 