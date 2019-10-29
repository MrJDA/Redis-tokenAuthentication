package com.oocl.web.interceptor;

import com.oocl.web.annotatons.RoleNum;
import com.oocl.web.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class PermissionInterceptor implements HandlerInterceptor {
    @Autowired
    private  UserService userService;

    private Logger logger = LoggerFactory.getLogger(PermissionInterceptor.class);
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(handler instanceof HandlerMethod){
            HandlerMethod methodHandle = (HandlerMethod) handler;
            RoleNum roleNum = methodHandle.getMethod().getAnnotation(RoleNum.class);
            if(roleNum == null) roleNum = methodHandle.getMethod().getDeclaringClass().getAnnotation(RoleNum.class);
            if(roleNum != null) {
                String roleName = roleNum.role().getRoleName();
                String token = request.getHeader("token");
                boolean isPermission = userService.checkPermission(token, roleName);
                if(!isPermission)sendErrorMessage(response, "403 not have authentication", 403);
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

    private void sendErrorMessage(HttpServletResponse response, String message, int status) throws IOException, JSONException {
        PrintWriter out = response.getWriter();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message", message);
        jsonObject.put("status", status);
        out.append(jsonObject.toString());
        out.flush();
        out.close();
    }
}
