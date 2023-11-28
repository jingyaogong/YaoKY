package com.example.demo.controller;
import cn.hutool.json.JSONObject;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class MyInterceptor implements HandlerInterceptor {
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getSession().getAttribute("user") == null) {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            PrintWriter out = null;
            try {
                JSONObject res = new JSONObject();
                res.put("success", Boolean.valueOf(false));
                res.put("message", "未登录，无权限");
                        out = response.getWriter();
                out.append(res.toString());
                response.sendRedirect("index");
                return false;
            } catch (Exception e) {
                e.printStackTrace();
                response.sendError(500);
                return false;
            }
        }
        return true;
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {}
}
