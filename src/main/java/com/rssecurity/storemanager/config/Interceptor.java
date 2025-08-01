package com.rssecurity.storemanager.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class Interceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler) throws Exception {
        if ("true".equals(request.getHeader("HX-Request"))) {
            request.setAttribute("layoutDisabled", true);
        }
        return true;
    }
}
