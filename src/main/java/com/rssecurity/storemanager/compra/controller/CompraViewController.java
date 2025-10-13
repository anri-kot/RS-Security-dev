package com.rssecurity.storemanager.compra.controller;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.rssecurity.storemanager.categoria.service.CategoriaService;
import com.rssecurity.storemanager.compra.dto.CompraDTO;
import com.rssecurity.storemanager.compra.dto.CompraViewDTO;
import com.rssecurity.storemanager.compra.service.CompraService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/compras")
public class CompraViewController {
    private final CompraService service;
    private final CategoriaService categoriaService;

    private final int DEFAULT_PAGE_SIZE = 10;

    public CompraViewController(CompraService service, CategoriaService categoriaService) {
        this.service = service;
        this.categoriaService = categoriaService;
    }

    @GetMapping()
    public String getPage(HttpServletRequest request,
            Model model,
            @RequestParam(name = "page", defaultValue = "1") int currentPage,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) Map<String, String> params) {

        params.remove("page");
        params.remove("size");

        params.values().removeIf(String::isBlank);

        if (size == null) {
            size = DEFAULT_PAGE_SIZE;
        }

        model.addAttribute("view", getCompraView(currentPage, params, size));

        return Boolean.TRUE.equals(request.getAttribute("layoutDisabled"))
                ? "compras :: content"
                : "compras";
    }

    private CompraViewDTO getCompraView(int currentPage, Map<String, String> params, int size) {
        int page = currentPage - 1;
        Page<CompraDTO> comprasPage;
        CompraViewDTO view = null;

        if (params.isEmpty()) {
            comprasPage = service.findAll(page, size);
        } else if ("idCompra".equals(params.get("tipo"))) {
            // Search by ID
            try {
                comprasPage = new PageImpl<>(List.of(
                        service.findById(
                                Long.parseLong(params.get("termo")))));
            } catch (Exception ignore) {
                comprasPage = new PageImpl<>(List.of());
            }
        } else {
            // Search with filter
            comprasPage = service.findAllByCustomMatcher(page, size, params);
        }
        view = new CompraViewDTO(comprasPage.getContent(), categoriaService.findAll(), currentPage, comprasPage.getTotalPages(),
                    size);

        return view;
    }
}
