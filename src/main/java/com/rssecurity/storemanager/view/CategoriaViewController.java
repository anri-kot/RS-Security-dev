package com.rssecurity.storemanager.view;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.rssecurity.storemanager.dto.CategoriaDTO;
import com.rssecurity.storemanager.service.CategoriaService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/categorias")
public class CategoriaViewController {
    private final CategoriaService service;

    private final int DEFAULT_PAGE_SIZE = 15;

    public CategoriaViewController(CategoriaService service) {
        this.service = service;
    }
    
    @GetMapping()
    public String getPage(HttpServletRequest request,
            @RequestParam(name = "page", defaultValue = "1") int currentPage,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String termo,
            @RequestParam(required = false) String tipo,
            Model model) {

        int page = currentPage - 1;
        int pageSize = size != null ? size : DEFAULT_PAGE_SIZE;

        Page<CategoriaDTO> categoriasPage = handleSearch(tipo, termo, page, pageSize);

        model.addAttribute("categorias", categoriasPage);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", categoriasPage.getTotalPages());
        model.addAttribute("target", "categorias");

        return Boolean.TRUE.equals(request.getAttribute("layoutDisabled"))
                ? "categorias :: content"
                : "categorias";
    }

    private Page<CategoriaDTO> handleSearch(String tipo, String termo, int page, int size) {
        if ((termo == null || termo.isBlank())) {
            return service.findAll(page, size);
        }

        String query = termo.trim();

        return switch (tipo) {
            case "id" -> {
                try {
                    CategoriaDTO Categoria = service.findById(Long.parseLong(query));
                    yield new PageImpl<>(List.of(Categoria));
                } catch (Exception e) {
                    yield new PageImpl<>(List.of());
                }
            }
            default -> {
                yield service.findByNomeContains(query, page, size);
            }
        };
    }
}
