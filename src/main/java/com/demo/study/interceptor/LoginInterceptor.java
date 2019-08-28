package com.demo.study.interceptor;
import com.demo.study.cache.JedisUtil;
import com.demo.study.entity.BaseBackBean;
import com.demo.study.util.Util;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private JedisUtil.Keys keys;
    @Autowired
    private JedisUtil.Strings strings;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getServletPath().endsWith(".do")){
            String userId = request.getHeader("userId");
            String session = request.getHeader("session");
            System.out.println("========="+userId+"..."+session);
            if (Util.isEmpty(userId)|| Util.isEmpty(session)){
                reLogin(response,"请求头出错");
                return false;
            } else {
                if (!keys.exists(userId)){
                    reLogin(response, "该用户不存在");
                    return false;
                }else {
                    String s = strings.get(userId);
                    if (s.equals(session)){
                        return true;
                    }else {
                        reLogin(response, "请重新登录");
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void reLogin(HttpServletResponse response, String s) throws Exception {
        response.reset();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        BaseBackBean backBean = new BaseBackBean();
        backBean.setStatus(100);
        backBean.setErrorMsg(s);
        PrintWriter writer = response.getWriter();
        writer.write(new Gson().toJson(backBean));
        writer.flush();
        writer.close();
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        preHandle(request, response, handler);
    }

}
