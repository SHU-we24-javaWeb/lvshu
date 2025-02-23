package com.lvshu.web.admin;

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
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/adminUserListServlet")
public class AdminUserListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 设置编码
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=utf-8");
        PrintWriter writer = resp.getWriter();

        try {
            // 获取分页参数
            int page = Integer.parseInt(req.getParameter("page"));
            int pageSize = Integer.parseInt(req.getParameter("pageSize"));
            int offset = (page - 1) * pageSize;

            // 查询数据
            SqlSessionFactory sqlSessionFactory = SqlSessionFactoryUtils.getSqlSessionFactory();
            SqlSession sqlSession = sqlSessionFactory.openSession();
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

            // 查询总数和列表数据
            int total = userMapper.countTotal();
            List<User> users = userMapper.selectByPage(offset, pageSize);

            // 构建返回的JSON
            StringBuilder json = new StringBuilder();
            json.append("{\"success\":true,\"total\":").append(total).append(",\"users\":[");

            String baseUrl = "http://" + req.getServerName() + ":" + req.getServerPort() + req.getContextPath() + "/";

            for (int i = 0; i < users.size(); i++) {
                User user = users.get(i);
                if (i > 0) {
                    json.append(",");
                }
                json.append("{")
                    .append("\"userId\":").append(user.getUserId()).append(",")
                    .append("\"username\":\"").append(user.getUsername()).append("\",")
                    .append("\"gender\":\"").append(user.getGender() != null ? user.getGender() : "").append("\",")
                    .append("\"dateOfBirth\":\"").append(user.getDateOfBirth() != null ? user.getDateOfBirth() : "").append("\",")
                    .append("\"nativePlace\":\"").append(user.getNativePlace() != null ? user.getNativePlace() : "").append("\",")
                    .append("\"mobilePhone\":\"").append(user.getMobilePhone() != null ? user.getMobilePhone() : "").append("\",")
                    .append("\"email\":\"").append(user.getEmail() != null ? user.getEmail() : "").append("\",")
                    .append("\"major\":\"").append(user.getMajor() != null ? user.getMajor() : "").append("\",")
                    .append("\"status\":\"").append(user.getStatus()).append("\",")
                    .append("\"createdAt\":\"").append(user.getCreatedAt() != null ? user.getCreatedAt() : "").append("\",")
                    .append("\"signature\":\"").append(user.getSignature() != null ? user.getSignature() : "").append("\",")
                    .append("\"avatar\":\"").append(user.getAvatar() != null ? baseUrl + user.getAvatar() : "").append("\"")
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