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

import com.rssecurity.storemanager.dto.VendaDTO;
import com.rssecurity.storemanager.service.CategoriaService;
import com.rssecurity.storemanager.service.VendaService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/vendas")
public class VendaViewController {
    private final VendaService service;
    private final CategoriaService categoriaService;

    private final int DEFAULT_PAGE_SIZE = 10;

    public VendaViewController(VendaService service, CategoriaService categoriaService) {
        this.service = service;
        this.categoriaService = categoriaService;
    }

    @GetMapping("")
    public String getPage(HttpServletRequest request, 
            Model model, 
            @RequestParam(name = "page", defaultValue = "1") int currentPage,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) Map<String, String> params) {
        
        params.remove("page");
        params.remove("size");

        int page = currentPage - 1;

        params.values().removeIf(String::isBlank);

        if (size == null) {
            size = DEFAULT_PAGE_SIZE;
        }

        Page<VendaDTO> vendasPage;

        if (params.isEmpty()) {
            vendasPage = service.findAll(page, size);
        } else if (params.containsKey("tipo") && "idVenda".equals(params.get("tipo"))) {
            // Search by ID
            List<VendaDTO> vendas = new ArrayList<>();
            try {
                vendas.add(service.findById(Long.parseLong(params.get("idVenda"))));
            } catch (Exception ignored) {}

            model.addAttribute("vendas", vendas);
            model.addAttribute("totalPages", 1);
            model.addAttribute("currentPage", 1);
            model.addAttribute("categorias", categoriaService.findAll());

            return Boolean.TRUE.equals(request.getAttribute("layoutDisabled"))
                    ? "vendas :: content"
                    : "vendas";

        } else {
            // Search with filters
            vendasPage = service.findAllByCustomMatcher(page, size, params);
        }

        model.addAttribute("vendas", vendasPage.getContent());
        model.addAttribute("totalPages", vendasPage.getTotalPages());
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("categorias", categoriaService.findAll());

        return Boolean.TRUE.equals(request.getAttribute("layoutDisabled"))
                ? "vendas :: content"
                : "vendas";
    }
}
