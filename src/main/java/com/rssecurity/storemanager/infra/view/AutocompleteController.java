package com.rssecurity.storemanager.infra.view;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.rssecurity.storemanager.fornecedor.dto.FornecedorDTO;
import com.rssecurity.storemanager.fornecedor.service.FornecedorService;
import com.rssecurity.storemanager.produto.dto.ProdutoDTO;
import com.rssecurity.storemanager.produto.service.ProdutoService;
import com.rssecurity.storemanager.usuario.dto.UsuarioResumoDTO;
import com.rssecurity.storemanager.usuario.service.UsuarioService;
import com.rssecurity.storemanager.venda.service.VendaService;

@Controller
public class AutocompleteController {

    private ProdutoService produtoService;
    private UsuarioService usuarioService;
    private FornecedorService fornecedorService;

    public AutocompleteController(ProdutoService produtoService, VendaService vendaService,
            UsuarioService usuarioService, FornecedorService fornecedorService) {
        this.produtoService = produtoService;
        this.usuarioService = usuarioService;
        this.fornecedorService = fornecedorService;
    }

    @GetMapping("/pdv/autocomplete")
    public String pdvAutocomplete(@RequestParam String termo, @RequestParam(required = false) String tipo,
            @RequestParam(required = false) Long idCategoria, Model model) {
        List<ProdutoDTO> results;

        if (tipo != null) {
            if (tipo.contains("produto")) {
                tipo = tipo.trim().toLowerCase().replace("produto", "");
            }
        } else {
            tipo = "";
        }

        switch (tipo.trim().toLowerCase()) {
            case "id" -> {
                results = new ArrayList<>();
                try {
                    Long id = Long.parseLong(termo);
                    results.add(produtoService.findById(id));
                } catch (Exception e) {}
            }
            case "codigo" -> {
                results = new ArrayList<>();
                try {
                    String codigo = termo.trim();
                    results.addAll(produtoService.findByCodigoBarrasContains(codigo));
                } catch (Exception e) {}
            }
            default -> {
                if (idCategoria != null) {
                results = produtoService.findByNomeContainsIgnoreCaseAndCategoria_IdCategoria(termo, idCategoria);
                } else {
                    results = produtoService.findByNomeContains(termo);
                }
            }
        }
        
        model.addAttribute("results", results);

        return "fragments/autocomplete :: options";
    }

    @GetMapping("/vendas/autocomplete")
    public String getVendasAutocomplete(@RequestParam String funcionario, Model model) {
        List<UsuarioResumoDTO> results = usuarioService.findByNomeContainingOrSobrenomeContainingOrUsernameContaining(funcionario)
                .stream()
                .map((usuario) -> new UsuarioResumoDTO(usuario.idUsuario(), usuario.username(), usuario.nome(), usuario.sobrenome()))
                .toList();
        model.addAttribute("results", results);

        return "fragments/autocomplete-funcionarios";
    }

    @GetMapping("/compras/autocomplete")
    public String getMethodName(@RequestParam String fornecedor, Model model) {
        List<FornecedorDTO> results = fornecedorService.findByNomeContains(fornecedor);
        model.addAttribute("results", results);
        return "fragments/autocomplete-fornecedores";
    }
    
    
}