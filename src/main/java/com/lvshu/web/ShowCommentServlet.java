package com.lvshu.web;

import com.lvshu.mapper.GuideCommentMapper;
import com.lvshu.mapper.UserMapper;
import com.lvshu.pojo.GuideComment;
import com.lvshu.pojo.User;
import com.lvshu.util.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class ShowCommentServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 设置请求的字符编码为UTF-8,不然会出乱码
        req.setCharacterEncoding("UTF-8");

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
        List<GuideComment> guideComments = guideCommentMapper.selectByGuideId(guideId);

        // 查询每个用户的具体信息 封装在数组里
        List<User> users = new ArrayList<>();
        for (GuideComment guideComment : guideComments) {
            User user = userMapper.selectById(guideComment.getUserId());
            users.add(user);
        }


        // 将信息传给前端


        // 获取字符输出流,并设置content type
        resp.setContentType("text/html;charset=utf-8");
        PrintWriter writer = resp.getWriter();

        sqlSession.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }
}
