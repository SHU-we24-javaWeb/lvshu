package com.lvshu.web.admin;

import com.lvshu.mapper.GuideMapper;
import com.lvshu.pojo.Admin;
import com.lvshu.pojo.Guide;
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
import java.util.List;

@WebServlet("/adminGuideListServlet")
public class AdminGuideListServlet extends HttpServlet {
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
            // 获取分页参数
            int page = Integer.parseInt(req.getParameter("page"));
            int pageSize = Integer.parseInt(req.getParameter("pageSize"));
            String status = req.getParameter("status");
            int offset = (page - 1) * pageSize;

            // 查询数据
            SqlSessionFactory sqlSessionFactory = SqlSessionFactoryUtils.getSqlSessionFactory();
            SqlSession sqlSession = sqlSessionFactory.openSession();
            GuideMapper guideMapper = sqlSession.getMapper(GuideMapper.class);

            // 查询总数和列表数据
            int total = guideMapper.countByStatus(status);
            List<Guide> guides = guideMapper.selectByStatus(status, offset, pageSize);

            // 构建基础URL
            String baseUrl = "http://" + req.getServerName() + ":" + req.getServerPort() + req.getContextPath() + "/";

            // 构建返回的JSON
            StringBuilder json = new StringBuilder();
            json.append("{\"success\":true,\"total\":").append(total).append(",\"guides\":[");

            for (int i = 0; i < guides.size(); i++) {
                Guide guide = guides.get(i);
                if (i > 0) {
                    json.append(",");
                }
                json.append("{")
                    .append("\"guideId\":").append(guide.getGuideId()).append(",")
                    .append("\"title\":\"").append(guide.getTitle()).append("\",")
                    .append("\"authorId\":").append(guide.getAuthorId()).append(",")
                    .append("\"status\":\"").append(guide.getStatus()).append("\",")
                    .append("\"coverImage\":\"").append(baseUrl).append(guide.getCoverImage()).append("\",")
                    .append("\"createdAt\":\"").append(guide.getCreatedAt()).append("\"")
                    .append("}");
            }

            json.append("]}");
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