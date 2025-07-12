package com.rssecurity.storemanager.view;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.rssecurity.storemanager.dto.UsuarioDTO;
import com.rssecurity.storemanager.service.UsuarioService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/usuarios")
public class UsuarioViewController {
    private final UsuarioService service;

    private final int DEFAULT_PAGE_SIZE = 15;

    public UsuarioViewController(UsuarioService service) {
        this.service = service;
    }

    @GetMapping
    public String getPage(
            HttpServletRequest request,
            Model model,
            @RequestParam(name = "page", defaultValue = "1") int currentPage,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String termo,
            @RequestParam(required = false) String tipo
    ) {

        int page = currentPage - 1;
        int pageSize = size != null ? size : DEFAULT_PAGE_SIZE;

        Page<UsuarioDTO> usuariosPage = handleSearch(termo, tipo, page, pageSize);

        model.addAttribute("usuarios", usuariosPage);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", usuariosPage.getTotalPages());

        return Boolean.TRUE.equals(request.getAttribute("layoutDisabled"))
                ? "usuarios :: content"
                : "usuarios";
    }

    private Page<UsuarioDTO> handleSearch(String termo, String tipo, int page, int size) {
        tipo = tipo != null ? tipo : "nome";
        if (termo == null) {
            return service.findAll(page, size);
        }

        String queue = termo.trim();

        return switch (tipo) {
            case "id" -> {
                try {
                    UsuarioDTO usuario = service.findById(Long.valueOf(termo));
                    yield new PageImpl<>(List.of(usuario));
                } catch (Exception e) {
                    yield new PageImpl<>(List.of());
                }
            }
            case "nome" -> {
                yield service.findByNomeContainingOrSobrenomeContaining(queue, page, size);
            }
            case "username" -> {
                yield service.findByUsernameContaining(queue, page, size);
            }
            case "cpf" -> {
                yield service.findByCpfContaining(queue, page, size);
            }
            case "telefone" -> {
                yield service.findByTelefoneContaining(queue, page, size);
            }
            case "endereco" -> {
                yield service.findByEnderecoContaining(queue, page, size);
            }
            case "email" -> {
                yield service.findByEmailContaining(queue, page, size);
            }
            default -> {
                yield service.findAll(page, size);
            }
        };
    }
}
