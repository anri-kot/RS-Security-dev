package com.rssecurity.storemanager.view;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.rssecurity.storemanager.dto.FornecedorDTO;
import com.rssecurity.storemanager.service.FornecedorService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/fornecedores")
public class FornecedorViewController {
    private final FornecedorService service;
    
    private final int DEFAULT_PAGE_SIZE = 15;

    public FornecedorViewController(FornecedorService service) {
        this.service = service;
    }

    @GetMapping()
    public String getFornecedores(HttpServletRequest request,
            @RequestParam(name = "page", defaultValue = "1") int currentPage,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String termo,
            @RequestParam(required = false) String tipo,
            Model model) {
        
        int page = currentPage - 1;
        int pageSize = size != null ? size : DEFAULT_PAGE_SIZE;

        Page<FornecedorDTO> fornecedoresPage = handleSearch(termo, tipo, page, pageSize);

        model.addAttribute("fornecedores", fornecedoresPage);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", fornecedoresPage.getTotalPages());
        model.addAttribute("target", "fornecedores");

        return Boolean.TRUE.equals(request.getAttribute("layoutDisabled"))
                ? "fornecedores :: content"
                : "fornecedores";
    }

    private Page<FornecedorDTO> handleSearch(String termo, String tipo, int page, int size) {
        if (termo == null || tipo == null) {
            return service.findAll(page, size);
        }

        String queue = termo.trim();
        return switch (tipo) {
            case "nome" -> {
                yield service.findByNomeContains(queue, page, size);
            }
            case "id" -> {
                try {
                    FornecedorDTO fornecedor = service.findById(Long.valueOf(queue));
                    yield new PageImpl<>(List.of(fornecedor));
                } catch (Exception ignored) {
                    yield new PageImpl<>(List.of());
                }
            }
            case "cnpj" -> {
                yield service.findByCnpjContains(termo, page, size);
            }
            case "telefone" -> {
                yield service.findByTelefoneContains(termo, page, size);
            }

            case "email" -> {
                yield service.findByEmailContains(termo, page, size);
            }
            default -> {
                yield service.findAll(page, size);
            }
        };
    }
}
