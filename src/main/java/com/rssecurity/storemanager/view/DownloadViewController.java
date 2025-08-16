package com.rssecurity.storemanager.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/exportar")
public class DownloadViewController {
    @GetMapping
    public String getPage(HttpServletRequest request) {
        return Boolean.TRUE.equals(request.getAttribute("layoutDisabled"))
            ? "exportar :: content"
            : "exportar";
    }
}