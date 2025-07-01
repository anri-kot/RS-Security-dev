package com.rssecurity.storemanager.view;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ImportarViewController {

    @GetMapping("/importar")
    public String getImportar(HttpServletRequest request, Model model) {
        if (Boolean.TRUE.equals(request.getAttribute("layoutDisabled"))) {
            return "importar :: content";
        }
        return "importar";
    }

}
