package com.lvshu.web.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lvshu.mapper.GuideCommentMapper;
import com.lvshu.util.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@WebServlet("/adminUpdateCommentStatusServlet")
public class AdminUpdateCommentStatusServlet extends HttpServlet {
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

            // 将JSON转换为Map
            Map<String, Object> data = objectMapper.readValue(sb.toString(), Map.class);
            Integer commentId = (Integer) data.get("commentId");
            String status = (String) data.get("status");

            // 更新数据
            SqlSessionFactory sqlSessionFactory = SqlSessionFactoryUtils.getSqlSessionFactory();
            SqlSession sqlSession = sqlSessionFactory.openSession();
            GuideCommentMapper commentMapper = sqlSession.getMapper(GuideCommentMapper.class);

            int result = commentMapper.updateStatus(commentId, status);
            sqlSession.commit();

            if (result > 0) {
                writer.write("{\"success\":true,\"message\":\"评论状态更新成功\"}");
            } else {
                writer.write("{\"success\":false,\"message\":\"评论状态更新失败\"}");
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