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

@WebServlet("/CreateCommentServlet")
public class CreateCommentServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 设置请求的字符编码为UTF-8,不然会出乱码
        req.setCharacterEncoding("UTF-8");

        // 1.接收信息

        // 获取当前登录用户（从 session 中获取）
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");  // 获取 user
        if (user == null) {
            // 如果用户没有登录（session 中没有 userId），可以重定向到登录页面
            resp.sendRedirect("login.html");
            return;
        }

        // 获取评论
        String comment = req.getParameter("comment");
        // 获取攻略ID（先尝试从URL参数获取，如果没有再从表单参数获取）
        String guideIdStr = req.getParameter("id");
        if (guideIdStr == null || guideIdStr.trim().isEmpty()) {
            guideIdStr = req.getParameter("guideId");
        }

        System.out.println("(CreateCommentServlet)Received request for guide ID: " + guideIdStr);
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
        // 查询该用户完整信息
        user = userMapper.selectUserByUsername(user.getUsername());

        // 封装攻略评论
        GuideComment guideComment = new GuideComment();
        guideComment.setUser(user);
        guideComment.setGuideId(guideId);
        guideComment.setUserId(user.getUserId());
        guideComment.setComment(comment);
        guideComment.setStatus("active");

        // 获取字符输出流,并设置content type
        resp.setContentType("text/html;charset=utf-8");
        PrintWriter writer = resp.getWriter();

        // 插入评论
        int count = guideCommentMapper.insert(guideComment);
        if (count > 0) {
            // 成功
            writer.write("<html><body>");
            writer.write("<h1>评论成功！</h1>");
            writer.write("<script>window.alert('评论成功');</script>");
            writer.write("<script>window.history.back();</script>");
            writer.write("</body></html>");
            sqlSession.commit();
        } else {
            // 失败
            writer.write("<html><body>");
            writer.write("<h1>评论失败！</h1>");
            writer.write("<script>window.alert('评论失败');</script>");
            writer.write("<script>window.history.back();</script>");
            writer.write("</body></html>");
            sqlSession.rollback();
        }
        sqlSession.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }
}
