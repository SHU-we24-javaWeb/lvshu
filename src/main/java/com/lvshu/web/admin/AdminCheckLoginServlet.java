package com.lvshu.web.admin;

import com.lvshu.pojo.Admin;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/checkLoginServlet")
public class AdminCheckLoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 设置编码
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=utf-8");
        PrintWriter writer = resp.getWriter();

        // 获取当前登录用户（从 session 中获取）
        HttpSession session = req.getSession();
        Admin admin = (Admin) session.getAttribute("admin"); // 获取 admin

        // 构建返回的JSON
        StringBuilder json = new StringBuilder();

        if (admin != null) {
            // 如果用户已经登录，返回成功的 JSON 数据
            json.append("{")
                    .append("\"loggedIn\":true,")  // 标记用户已经登录
                    .append("\"admin\":{")
                    .append("\"username\":").append("\"").append(admin.getUsername()).append("\"")
                    .append("}}");
        } else {
            // 如果用户没有登录，返回未登录的状态
            json.append("{")
                    .append("\"loggedIn\":false")  // 标记用户未登录
                    .append("}");
        }

        writer.write(json.toString());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }
}
