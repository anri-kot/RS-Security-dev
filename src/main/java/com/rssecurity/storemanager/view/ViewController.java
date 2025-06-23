package com.rssecurity.storemanager.view;

import com.rssecurity.storemanager.dto.CategoriaDTO;
import com.rssecurity.storemanager.dto.CompraDTO;
import com.rssecurity.storemanager.dto.FornecedorDTO;
import com.rssecurity.storemanager.dto.ProdutoDTO;
import com.rssecurity.storemanager.dto.UsuarioResumoDTO;
import com.rssecurity.storemanager.dto.VendaDTO;
import com.rssecurity.storemanager.service.CategoriaService;
import com.rssecurity.storemanager.service.CompraService;
import com.rssecurity.storemanager.service.FornecedorService;
import com.rssecurity.storemanager.service.ProdutoService;
import com.rssecurity.storemanager.service.UsuarioService;
import com.rssecurity.storemanager.service.VendaService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class ViewController {

    private final ProdutoService produtoService;
    private final CategoriaService categoriaService;
    private final FornecedorService fornecedorService;
    private final VendaService vendaService;
    private final CompraService compraService;
    private final UsuarioService usuarioService;

    public ViewController(ProdutoService produtoService, CategoriaService categoriaService,
            FornecedorService fornecedorService, VendaService vendaService, CompraService compraService,
            UsuarioService usuarioService) {
        this.produtoService = produtoService;
        this.categoriaService = categoriaService;
        this.fornecedorService = fornecedorService;
        this.vendaService = vendaService;
        this.compraService = compraService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/auth/login")
    public String loginPage() {
        return "login";
    }

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

    @GetMapping("/vendas")
    public String getVendas(HttpServletRequest request, Model model, @RequestParam(required = false) Map<String, String> params) {
        List<VendaDTO> vendas;
        List<CategoriaDTO> categorias = categoriaService.findAll();
        List<UsuarioResumoDTO> funcionarios = usuarioService.findAll().stream()
            .map(funcionario -> {
                return new UsuarioResumoDTO(funcionario.idUsuario(), funcionario.username(), funcionario.nome(), funcionario.sobrenome());
            })
            .toList();

        if (params == null || params.isEmpty()) {
            vendas = vendaService.findAll();
        } else if (params.get("tipo").equals("idVenda")) {
            vendas = new ArrayList<>();
            try {
                vendas.add(vendaService.findById(Long.parseLong(params.get("idVenda"))));
            } catch (Exception e) {}
        } else {
            vendas = vendaService.findAllByCustomMatcher(params);
        }

        model.addAttribute("vendas", vendas);

        if (Boolean.TRUE.equals(request.getAttribute("layoutDisabled"))) {
            return "vendas :: content";
        }

        model.addAttribute("funcionarios", funcionarios);
        model.addAttribute("categorias", categorias);

        return "vendas";
    }

    @GetMapping("/compras")
    public String getCompras(HttpServletRequest request, Model model, @RequestParam(required = false) Map<String, String> params) {
        List<CompraDTO> compras = compraService.findAll();
        List<FornecedorDTO> fornecedores = fornecedorService.findAll();
        List<CategoriaDTO> categorias = categoriaService.findAll();

        model.addAttribute("compras", compras);
        model.addAttribute("categorias", categorias);
        model.addAttribute("fornecedores", fornecedores);

        if (Boolean.TRUE.equals(request.getAttribute("layoutDisabled"))) {
            return "vendas :: content";
        }

        return "compras";
    }
    

}
