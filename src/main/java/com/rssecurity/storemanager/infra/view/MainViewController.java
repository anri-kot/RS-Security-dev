package com.rssecurity.storemanager.infra.view;

import java.util.Map;

import org.springframework.security.core.AuthenticationException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class MainViewController {

    @GetMapping("/auth/login")
    public String loginPage(HttpServletRequest request, @RequestParam(name = "error", required = false) String error, Model model) {
        if (error != null) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                String errorMessage = "Credenciais inválidas";
                AuthenticationException ex = (AuthenticationException) session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
                if (ex != null) {
                    errorMessage = ex.getMessage();
                }
                model.addAttribute("error", errorMessage);
            }
        }
        return "login";
    }

    @GetMapping({ "/", "/home" })
    public String getHome(HttpServletRequest request, Model model) {
        if (Boolean.TRUE.equals(request.getAttribute("layoutDisabled"))) {
            return "index :: content";
        }
        return "index";
    }

    @GetMapping("/user/info")
    @ResponseBody
    public Map<String, String> getUserInfo() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return Map.of("username", username);
    }

}
