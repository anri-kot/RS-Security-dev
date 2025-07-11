package com.rssecurity.storemanager.view;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.rssecurity.storemanager.dto.ProdutoDTO;
import com.rssecurity.storemanager.exception.ResourceNotFoundException;
import com.rssecurity.storemanager.service.CategoriaService;
import com.rssecurity.storemanager.service.ProdutoService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/produtos")
public class ProdutoViewController {
    private final ProdutoService service;
    private final CategoriaService categoriaService;

    private final int DEFAULT_PAGE_SIZE = 15;

    public ProdutoViewController(ProdutoService service, CategoriaService categoriaService) {
        this.service = service;
        this.categoriaService = categoriaService;
    }
    
    @GetMapping()
    public String getPage(HttpServletRequest request,
            @RequestParam(name = "page", defaultValue = "1") int currentPage,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String termo,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) Long idCategoria,
            Model model) {

        int page = currentPage - 1;
        int pageSize = size != null ? size : DEFAULT_PAGE_SIZE;

        model.addAttribute("categorias", categoriaService.findAll());

        Page<ProdutoDTO> produtosPage = handleSearch(tipo, termo, idCategoria, page, pageSize);

        model.addAttribute("produtos", produtosPage);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", produtosPage.getTotalPages());

        return Boolean.TRUE.equals(request.getAttribute("layoutDisabled"))
                ? "produtos :: content"
                : "produtos";
    }

    private Page<ProdutoDTO> handleSearch(String tipo, String termo, Long idCategoria, int page, int size) {
        if ((termo == null || termo.isBlank()) && idCategoria == null) {
            return service.findAll(page, size);
        }

        String query = termo.trim();

        return switch (tipo) {
            case "id" -> {
                try {
                    ProdutoDTO produto = service.findById(Long.parseLong(query));
                    yield new PageImpl<>(List.of(produto));
                } catch (Exception e) {
                    yield new PageImpl<>(List.of());
                }
            }
            case "codigo" -> {
                try {
                    ProdutoDTO produto = service.findByCodigoBarras(query);
                    yield new PageImpl<>(List.of(produto));
                } catch (ResourceNotFoundException ignore) {
                    yield new PageImpl<>(List.of());
                }
            }
            default -> {
                if (idCategoria != null) {
                    yield service.findByNomeContainsIgnoreCaseAndCategoria_IdCategoria(query, idCategoria, page, size);
                } else {
                    yield service.findByNomeContains(query.toLowerCase(), page, size);
                }
            }
        };
    }
}
