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

@WebServlet("/updateGuideServlet")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1,  // 1 MB
        maxFileSize = 1024 * 1024 * 10,       // 10 MB
        maxRequestSize = 1024 * 1024 * 100    // 100 MB
)
public class UpdateGuideServlet extends HttpServlet {
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
        String title = req.getParameter("title");
        String location = req.getParameter("location");
        String category = req.getParameter("category");
        String season = req.getParameter("season");
        String priceRange = req.getParameter("priceRange");
        String content = req.getParameter("content");
        String tags = req.getParameter("tags");

        // 处理封面图片
        Part coverImagePart = req.getPart("coverImage");
        String coverImage = "";
        if (coverImagePart != null && coverImagePart.getSize() > 0) {
            String savePath = getServletContext().getRealPath("/images");
            File uploadDir = new File(savePath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            
            coverImage = getSubmittedFileName(coverImagePart);
            String filePath = savePath + File.separator + coverImage;
            coverImagePart.write(filePath);
            coverImage = "images/" + coverImage;
        }

        // 处理攻略图片
        List<String> imagePaths = new ArrayList<>();
        for (Part part : req.getParts()) {
            if (part.getName().equals("images") && part.getSize() > 0) {
                String savePath = getServletContext().getRealPath("/images");
                String fileName = getSubmittedFileName(part);
                String filePath = savePath + File.separator + fileName;
                part.write(filePath);
                imagePaths.add("images/" + fileName);
            }
        }

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
        // 获取原作者信息
        User author = userMapper.selectById(guide.getAuthorId());
        System.out.println("editServlet author: " + author);
        
        // 验证是否是攻略作者
        if (author.getUserId() != user.getUserId()) {
            resp.setContentType("text/html;charset=utf-8");
            PrintWriter writer = resp.getWriter();
            writer.write("<script>alert('您没有权限编辑此攻略'); window.location.href='my.html';</script>");
            return;
        }

        // 更新攻略信息
        guide.setTitle(title);
        guide.setLocation(location);
        guide.setCategory(category);
        guide.setSeason(season);
        guide.setPriceRange(priceRange);
        guide.setContent(content);
        guide.setTags(tags);
        
        // 只有当上传了新图片时才更新图片路径
        if (!coverImage.isEmpty()) {
            guide.setCoverImage(coverImage);
        }
        if (!imagePaths.isEmpty()) {
            guide.setImagePaths(String.join(",", imagePaths));
        }

        // 更新数据库
        int count = guideMapper.update(guide);

        // 返回结果
        resp.setContentType("text/html;charset=utf-8");
        PrintWriter writer = resp.getWriter();
        if (count > 0) {
            // 成功
            sqlSession.commit();
            writer.write("<script>alert('攻略更新成功'); window.location.href='guide-detail.html?id=" + guideId + "';</script>");
        } else {
            // 失败
            sqlSession.rollback();
            writer.write("<script>alert('攻略更新失败'); window.history.back();</script>");
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
