package com.oocl.web.interceptor;

import com.oocl.web.annotatons.AuthToken;
import com.oocl.web.util.JedisUtil;
import com.oocl.web.util.TokenUtil;
import io.netty.util.internal.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class AuthInterceptor implements HandlerInterceptor {

    private Logger logger = LoggerFactory.getLogger(AuthInterceptor.class);
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(handler instanceof HandlerMethod){
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            //获取方法上的注解
            AuthToken authToken = handlerMethod.getMethod().getAnnotation(AuthToken.class);
            //如果方法上的注解为空就获取类上的注解
            if(authToken == null){
                authToken = handlerMethod.getMethod().getDeclaringClass().getAnnotation(AuthToken.class);
            }
            if(authToken!=null){
               String token = request.getHeader("token");
               logger.info(token);
               if(StringUtil.isNullOrEmpty(token)){
                   sendErrorMessage(response, "用户身份过期，请重新登录", 401);
                   return false;
               }
               //解析token获取相应的用户信息
               Map<String, Object> claims = TokenUtil.parseToken(token);
               String userName = claims.get("userName").toString();
               if(userName == null) {
                   sendErrorMessage(response, "警告!用户信息错误", 401);
                   return false;
               }
               Object redisToken = JedisUtil.get(userName);
               if(redisToken == null || !(redisToken.toString()).equals(token)) {
                   sendErrorMessage(response, "警告!用户信息错误", 401);
                   return false;
               }
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
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message", message);
        jsonObject.put("status", status);
        out.append(jsonObject.toString());
        out.flush();
        out.close();
    }
}
