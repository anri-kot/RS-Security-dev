package com.rssecurity.storemanager.view;

import com.rssecurity.storemanager.dto.ProdutoDTO;
import com.rssecurity.storemanager.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

@Controller
public class ViewController {

    @Autowired
    private ProdutoService produtoService;

    @GetMapping("/")
    public String getIndex(Model model) {
        return "index";
    }

    @GetMapping("/home")
    public String getHome(Model model) {
        return "index";
    }

    @GetMapping("/produto")
    public String getProdutos(HttpServletRequest request, Model model) {
        List<ProdutoDTO> produtos = produtoService.findAll();
        model.addAttribute("produtos", produtos);

        boolean isHtmxRequest = "true".equals(request.getHeader("HX-Request"));
        if (isHtmxRequest) {
            return "fragments/produto :: content";
        }

        return "produto";
    }
}
