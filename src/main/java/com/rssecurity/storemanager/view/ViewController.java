package com.rssecurity.storemanager.view;

import com.rssecurity.storemanager.dto.CategoriaDTO;
import com.rssecurity.storemanager.dto.ProdutoDTO;
import com.rssecurity.storemanager.service.CategoriaService;
import com.rssecurity.storemanager.service.ProdutoService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ViewController {

    @Autowired
    private ProdutoService produtoService;
    @Autowired
    private CategoriaService categoriaService;

    @GetMapping({ "/", "/home" })
    public String getHome(HttpServletRequest request, Model model) {
        if (Boolean.TRUE.equals(request.getAttribute("layoutDisabled"))) {
            return "index :: content";
        }
        
        return "index";
    }

    @GetMapping("/produtos")
    public String getProdutos(HttpServletRequest request, @RequestParam(required = false) String termo, 
        @RequestParam(required = false) String tipo, @RequestParam(required = false) Long idCategoria, Model model) {

        List<ProdutoDTO> produtos;
        if (termo == null) {
            termo = "";
        }
        
        if (tipo != null && tipo.toLowerCase().equals("id")) {
            produtos = new ArrayList<>();
            try {
                Long id = Long.parseLong(termo);
                produtos.add(produtoService.findById(id));
            } catch (Exception e) {
            }
        } else {
            if (idCategoria != null) {
                produtos = produtoService.findByNomeContainsIgnoreCaseAndCategoria_IdCategoria(termo, idCategoria);
            } else {
                produtos = produtoService.findByNomeContains(termo);
            }
        }
        
        List<CategoriaDTO> categorias = categoriaService.findAll();

        model.addAttribute("produtos", produtos);
        model.addAttribute("categorias", categorias);

        if (Boolean.TRUE.equals(request.getAttribute("layoutDisabled"))) {
            return "produtos :: content";
        }

        return "produtos";
    }

    @GetMapping("/pdv")
    public String getPdv(HttpServletRequest request, Model model) {
        List<CategoriaDTO> categorias = categoriaService.findAll();
        model.addAttribute("categorias", categorias);

        if (Boolean.TRUE.equals(request.getAttribute("layoutDisabled"))) {
            return "pdv :: content";
        }

        return "pdv";
    }
    
    @GetMapping("/auth/login")
    public String loginPage() {
        return "login";  // Retorna o nome da página Thymeleaf (exemplo)
    }

}
