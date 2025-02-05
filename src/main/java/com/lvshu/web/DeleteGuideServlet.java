package com.lvshu.web;

import com.lvshu.mapper.GuideMapper;
import com.lvshu.mapper.UserMapper;
import com.lvshu.pojo.Guide;
import com.lvshu.pojo.User;
import com.lvshu.util.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/deleteGuideServlet")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1,  // 1 MB
        maxFileSize = 1024 * 1024 * 10,       // 10 MB
        maxRequestSize = 1024 * 1024 * 100    // 100 MB
)
public class DeleteGuideServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 设置请求的字符编码为UTF-8
        req.setCharacterEncoding("UTF-8");

        // 获取当前登录用户
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            resp.sendRedirect("login.html");
            return;
        }

        // 获取攻略ID和其他信息
        int guideId = Integer.parseInt(req.getParameter("guideId"));

        // 2.调用MyBatis完成查询
        // 这里直接去官网复制粘贴过来
        // 2.1 获取SqlSessionFactory对象 优化以后用了工具类 这样只创建一个工厂
        SqlSessionFactory sqlSessionFactory = SqlSessionFactoryUtils.getSqlSessionFactory();
        // 2.2 获取SqlSession对象
        SqlSession sqlSession = sqlSessionFactory.openSession();
        // 2.3 获取Mapper
        GuideMapper guideMapper = sqlSession.getMapper(GuideMapper.class);
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

        // 获取原有攻略信息
        Guide guide = guideMapper.selectById(guideId);
        System.out.println("editSevlet guide: " + guide);
        // 获取完整user信息
        user = userMapper.selectUserByUsername(user.getUsername());
        System.out.println("editSevlet user: " + guide);
        
        // 验证是否是攻略作者
        if (guide.getAuthorId() != user.getUserId()) {
            resp.setContentType("text/html;charset=utf-8");
            PrintWriter writer = resp.getWriter();
            writer.write("<script>alert('您没有权限删除此攻略'); window.location.href='my.html';</script>");
            return;
        }


        // 更新数据库
        int count = guideMapper.deleteByPrimaryKey(guideId);

        // 返回结果
        resp.setContentType("text/html;charset=utf-8");
        PrintWriter writer = resp.getWriter();
        if (count > 0) {
            // 成功
            sqlSession.commit();
            writer.write("<script>alert('攻略删除成功'); window.history.back();</script>");
        } else {
            // 失败
            sqlSession.rollback();
            writer.write("<script>alert('攻略删除失败'); window.history.back();</script>");
        }

        sqlSession.close();
    }

    private String getSubmittedFileName(Part part) {
        for (String cd : part.getHeader("content-disposition").split(";")) {
            if (cd.trim().startsWith("filename")) {
                String fileName = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
                return new String(fileName.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            }
        }
        return null;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }
}
