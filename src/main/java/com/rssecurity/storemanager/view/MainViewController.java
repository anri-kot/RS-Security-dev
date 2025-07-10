package com.rssecurity.storemanager.view;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class MainViewController {

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

    // @GetMapping("/produtos")
    // public String getProdutos(HttpServletRequest request, @RequestParam(required = false) String termo, 
    //     @RequestParam(required = false) String tipo, @RequestParam(required = false) Long idCategoria, Model model) {

    //     List<ProdutoDTO> produtos;
    //     if (termo == null) {
    //         termo = "";
    //     }
    //     if (tipo == null) {
    //         tipo = "";
    //     }

    //     switch (tipo.trim().toLowerCase()) {
    //         case "id" -> {
    //             produtos = new ArrayList<>();
    //             try {
    //                 Long id = Long.parseLong(termo);
    //                 produtos.add(produtoService.findById(id));
    //             } catch (Exception e) {}
    //         }
    //         case "codigo" -> {
    //             produtos = new ArrayList<>();
    //             try {
    //                 String codigo = termo.trim();
    //                 produtos.add(produtoService.findByCodigoBarras(codigo));
    //             } catch (Exception e) {}
    //         }
    //         default -> {
    //             if (idCategoria != null) {
    //             produtos = produtoService.findByNomeContainsIgnoreCaseAndCategoria_IdCategoria(termo, idCategoria);
    //             } else {
    //                 produtos = produtoService.findByNomeContains(termo);
    //             }
    //         }
    //     }
        
    //     List<CategoriaDTO> categorias = categoriaService.findAll();

    //     model.addAttribute("produtos", produtos);
    //     model.addAttribute("categorias", categorias);

    //     if (Boolean.TRUE.equals(request.getAttribute("layoutDisabled"))) {
    //         return "produtos :: content";
    //     }
        
    //     return "produtos";
    // }

    // @GetMapping("/pdv")
    // public String getPdv(HttpServletRequest request, Model model) {
    //     List<CategoriaDTO> categorias = categoriaService.findAll();
    //     model.addAttribute("categorias", categorias);

    //     if (Boolean.TRUE.equals(request.getAttribute("layoutDisabled"))) {
    //         return "pdv :: content";
    //     }

    //     return "pdv";
    // }

    // @GetMapping("/fornecedores")
    // public String getFornecedores(HttpServletRequest request, @RequestParam(required = false) String termo, @RequestParam(required = false) String tipo, Model model) {
    //     List<FornecedorDTO> fornecedores;

    //     if (tipo == null) {
    //         tipo = "none";
    //     }

    //     switch (tipo) {
    //         case "nome":
    //             fornecedores = fornecedorService.findByNomeContains(termo);
    //             break;

    //         case "id":
    //             fornecedores = new ArrayList<>();
    //             fornecedores.add(fornecedorService.findById(Long.parseLong(termo)));
    //             break;
        
    //         case "cnpj":
    //             fornecedores = fornecedorService.findByCnpjContains(termo);
    //             break;

    //         case "telefone":
    //             fornecedores = fornecedorService.findByTelefoneContains(termo);
    //             break;

    //         case "email":
    //             fornecedores = fornecedorService.findByEmailContains(termo);
    //             break;

    //         default:
    //             fornecedores = fornecedorService.findAll();
    //             break;
    //     }

    //     model.addAttribute("fornecedores", fornecedores);

    //     if (Boolean.TRUE.equals(request.getAttribute("layoutDisabled"))) {
    //         return "fornecedores :: content";
    //     }

    //     return "fornecedores";
    // }

    // @GetMapping("/vendas")
    // public String getVendas(HttpServletRequest request, 
    //         Model model, 
    //         @RequestParam(defaultValue = "0") int page,
    //         @RequestParam(defaultValue = "10") int size,
    //         @RequestParam(required = false) Map<String, String> params) {
        
    //     params.remove("page");
    //     params.remove("size");

    //     params.values().removeIf(String::isBlank);

    //     Page<VendaDTO> vendasPage;

    //     if (params.isEmpty()) {
    //         vendasPage = vendaService.findAll(page, size);
    //     } else if (params.containsKey("tipo") && "idVenda".equals(params.get("tipo"))) {
    //         // Search by ID
    //         List<VendaDTO> vendas = new ArrayList<>();
    //         try {
    //             vendas.add(vendaService.findById(Long.parseLong(params.get("idVenda"))));
    //         } catch (Exception ignored) {}

    //         model.addAttribute("vendas", vendas);
    //         model.addAttribute("totalPages", 1);
    //         model.addAttribute("currentPage", 0);
    //         model.addAttribute("categorias", categoriaService.findAll());

    //         return Boolean.TRUE.equals(request.getAttribute("layoutDisabled"))
    //                 ? "vendas :: content"
    //                 : "vendas";

    //     } else {
    //         // Search with filters
    //         vendasPage = vendaService.findAllByCustomMatcher(page, size, params);
    //     }

    //     model.addAttribute("vendas", vendasPage.getContent());
    //     model.addAttribute("totalPages", vendasPage.getTotalPages());
    //     model.addAttribute("currentPage", page);
    //     model.addAttribute("categorias", categoriaService.findAll());

    //     return Boolean.TRUE.equals(request.getAttribute("layoutDisabled"))
    //             ? "vendas :: content"
    //             : "vendas";
    // }

    // @GetMapping("/compras")
    // public String getCompras(HttpServletRequest request, Model model, @RequestParam(required = false) Map<String, String> params) {
    //     List<CompraDTO> compras;
    //     List<CategoriaDTO> categorias = categoriaService.findAll();

    //     if (params == null || params.isEmpty()) {
    //         compras = compraService.findAll();
    //     } else if (params.get("tipo").equals("idCompra")) {
    //         compras = new ArrayList<>();
    //         try {
    //             compras.add(compraService.findById(Long.parseLong(params.get("idCompra"))));
    //         } catch (Exception e) {}
    //     } else {
    //         compras = compraService.findAllByCustomMatcher(params);
    //     }

    //     model.addAttribute("compras", compras);

    //     if (Boolean.TRUE.equals(request.getAttribute("layoutDisabled"))) {
    //         return "compras :: content";
    //     }

    //     model.addAttribute("categorias", categorias);

    //     return "compras";
    // }

}
