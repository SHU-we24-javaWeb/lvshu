package com.lvshu.web;

import com.lvshu.mapper.GuideCommentMapper;
import com.lvshu.mapper.UserMapper;
import com.lvshu.pojo.GuideComment;
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
import java.util.ArrayList;
import java.util.List;

@WebServlet("/ShowCommentServlet")
public class ShowCommentServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 设置响应类型和编码
        resp.setContentType("application/json;charset=utf-8");
        PrintWriter writer = resp.getWriter();

        // 1.接收信息
        // 获取攻略ID（先尝试从URL参数获取，如果没有再从表单参数获取）
        String guideIdStr = req.getParameter("id");
        if (guideIdStr == null || guideIdStr.trim().isEmpty()) {
            guideIdStr = req.getParameter("guideId");
        }

        System.out.println("(ShowCommentServlet)Received request for guide ID: " + guideIdStr);
        int guideId;
        try {
            guideId = Integer.parseInt(guideIdStr);
        } catch (NumberFormatException e) {
            resp.setContentType("application/json;charset=utf-8");
            resp.getWriter().write("{\"success\":false,\"message\":\"无效的攻略ID\"}");
            return;
        }

        // 2.调用MyBatis完成查询
        // 这里直接去官网复制粘贴过来
        // 2.1 获取SqlSessionFactory对象 优化以后用了工具类 这样只创建一个工厂
        SqlSessionFactory sqlSessionFactory = SqlSessionFactoryUtils.getSqlSessionFactory();
        // 2.2 获取SqlSession对象
        SqlSession sqlSession = sqlSessionFactory.openSession();
        // 2.3 获取Mapper
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        GuideCommentMapper guideCommentMapper = sqlSession.getMapper(GuideCommentMapper.class);
        // 查询该攻略的全部评论
        List<GuideComment> comments = guideCommentMapper.selectByGuideId(guideId);

        // 查询每个用户的具体信息 封装在数组里
        List<User> users = new ArrayList<>();
        for (GuideComment guideComment : comments) {
            User user = userMapper.selectById(guideComment.getUserId());
            users.add(user);
        }

        // 构建JSON响应
        StringBuilder json = new StringBuilder();
        json.append("{")
                .append("\"success\":true,")
                .append("\"comments\":[");

        for (int i = 0; i < comments.size(); i++) {
            if (i > 0) {
                json.append(",");
            }
            GuideComment comment = comments.get(i);
            User user = comment.getUser();

            json.append("{")
                    .append("\"commentId\":").append(comment.getCommentId()).append(",")
                    .append("\"comment\":\"").append(escapeJson(comment.getComment())).append("\",")
                    .append("\"createdAt\":\"").append(comment.getCreatedAt().getTime()).append("\",")
                    .append("\"user\":{")
                    .append("\"userId\":").append(user.getUserId()).append(",")
                    .append("\"username\":\"").append(escapeJson(user.getUsername())).append("\",")
                    .append("\"avatar\":\"").append(escapeJson(user.getAvatar())).append("\",")
                    .append("\"signature\":\"").append(escapeJson(user.getSignature())).append("\"")
                    .append("}")
                    .append("}");
        }

        json.append("]}");
        System.out.println(json.toString());
        writer.write(json.toString());

        // 释放资源
        sqlSession.close();

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }

    private String escapeJson(String text) {
        if (text == null)
            return "";
        return text.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
