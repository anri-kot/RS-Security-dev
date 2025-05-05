package com.rssecurity.storemanager.view;

import com.rssecurity.storemanager.dto.ProdutoDTO;
import com.rssecurity.storemanager.service.ProdutoService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ViewController {

    @Autowired
    private ProdutoService produtoService;

    @GetMapping({ "/", "/home" })
    public String getHome(HttpServletRequest request, Model model) {
        if (Boolean.TRUE.equals(request.getAttribute("layoutDisabled"))) {
            return "index :: content";
        }
        
        return "index";
    }

    @GetMapping("/produtos")
    public String getProdutos(HttpServletRequest request, Model model) {
        List<ProdutoDTO> produtos = produtoService.findAll();
        model.addAttribute("produtos", produtos);

        if (Boolean.TRUE.equals(request.getAttribute("layoutDisabled"))) {
            return "produtos :: content";
        }

        return "produtos";
    }

    @GetMapping("/pdv")
    public String getPdv(HttpServletRequest request, Model model) {
        if (Boolean.TRUE.equals(request.getAttribute("layoutDisabled"))) {
            return "pdv :: content";
        }

        return "pdv";
    }
}
