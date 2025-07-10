package com.rssecurity.storemanager.view;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rssecurity.storemanager.dto.CategoriaDTO;
import com.rssecurity.storemanager.service.CategoriaService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/pdv")
public class PdvViewController {
    private final CategoriaService categoriaService;
    
    public PdvViewController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping()
    public String getPdv(HttpServletRequest request, Model model) {
        List<CategoriaDTO> categorias = categoriaService.findAll();
        model.addAttribute("categorias", categorias);

        if (Boolean.TRUE.equals(request.getAttribute("layoutDisabled"))) {
            return "pdv :: content";
        }

        return "pdv";
    }
}
