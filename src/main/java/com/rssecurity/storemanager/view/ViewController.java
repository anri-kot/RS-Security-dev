package com.rssecurity.storemanager.view;

import com.rssecurity.storemanager.dto.CategoriaDTO;
import com.rssecurity.storemanager.dto.FornecedorDTO;
import com.rssecurity.storemanager.dto.ProdutoDTO;
import com.rssecurity.storemanager.service.CategoriaService;
import com.rssecurity.storemanager.service.FornecedorService;
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
    @Autowired
    private FornecedorService fornecedorService;

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

    @GetMapping("/fornecedores")
    public String getFornecedores(HttpServletRequest request, @RequestParam(required = false) String termo, @RequestParam(required = false) String tipo, Model model) {
        List<FornecedorDTO> fornecedores;

        if (tipo == null) {
            tipo = "none";
        }

        switch (tipo) {
            case "nome":
                fornecedores = fornecedorService.findByNomeContains(termo);
                break;

            case "id":
                fornecedores = new ArrayList<>();
                fornecedores.add(fornecedorService.findById(Long.parseLong(termo)));
                break;
        
            case "cnpj":
                fornecedores = fornecedorService.findByCnpjContains(termo);
                break;

            case "telefone":
                fornecedores = fornecedorService.findByTelefoneContains(termo);
                break;

            case "email":
                fornecedores = fornecedorService.findByEmailContains(termo);
                break;

            default:
                fornecedores = fornecedorService.findAll();
                break;
        }

        model.addAttribute("fornecedores", fornecedores);

        if (Boolean.TRUE.equals(request.getAttribute("layoutDisabled"))) {
            return "fornecedores :: content";
        }

        return "fornecedores";
    }
    
    
    @GetMapping("/auth/login")
    public String loginPage() {
        return "login";  // Retorna o nome da página Thymeleaf (exemplo)
    }

}
