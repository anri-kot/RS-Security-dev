package com.rssecurity.storemanager.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.rssecurity.storemanager.dto.CompraDTO;
import com.rssecurity.storemanager.service.CategoriaService;
import com.rssecurity.storemanager.service.CompraService;

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

        int page = currentPage - 1;

        Page<CompraDTO> comprasPage;

        if (params.isEmpty()) {
            comprasPage = service.findAll(page, size);
        } else if ("idCompra".equals(params.get("tipo"))) {
            // Search by ID
            List<CompraDTO> compras = new ArrayList<>();
            try {
                compras.add(service.findById(Long.parseLong(params.get("termo"))));
            } catch (Exception ignore) {}

            model.addAttribute("compras", compras);
            model.addAttribute("totalPages", 1);
            model.addAttribute("currentPage", 1);
            model.addAttribute("categorias", categoriaService.findAll());
            model.addAttribute("target", "compras");

            return Boolean.TRUE.equals(request.getAttribute("layoutDisabled"))
                    ? "compras :: content"
                    : "compras";
        } else {
            // Search with filter
            comprasPage = service.findAllByCustomMatcher(page, size, params);
        }

        model.addAttribute("compras", comprasPage);
        model.addAttribute("totalPages", comprasPage.getTotalPages());
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("categorias", categoriaService.findAll());
        model.addAttribute("target", "compras");

        return Boolean.TRUE.equals(request.getAttribute("layoutDisabled"))
                ? "compras :: content"
                : "compras";
    }
}
