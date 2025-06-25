package com.rssecurity.storemanager.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rssecurity.storemanager.dto.FornecedorDTO;
import com.rssecurity.storemanager.dto.ProdutoDTO;
import com.rssecurity.storemanager.dto.UsuarioResumoDTO;
import com.rssecurity.storemanager.dto.VendaDTO;
import com.rssecurity.storemanager.service.FornecedorService;
import com.rssecurity.storemanager.service.ProdutoService;
import com.rssecurity.storemanager.service.UsuarioService;
import com.rssecurity.storemanager.service.VendaService;

@Controller
public class AutocompleteController {

    @Autowired
    private ProdutoService produtoService;
    @Autowired
    private VendaService vendaService;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private FornecedorService fornecedorService;

    @GetMapping("/pdv/autocomplete")
    public String pdvAutocomplete(@RequestParam String termo, @RequestParam(required = false) String tipo,
            @RequestParam(required = false) Long idCategoria, Model model) {
        List<ProdutoDTO> results;
        
        if (tipo != null && tipo.toLowerCase().contains("id")) {
            results = new ArrayList<>();
            try {
                Long id = Long.parseLong(termo);
                results.add(produtoService.findById(id));
            } catch (Exception e) {
            }
        } else {
            if (idCategoria != null) {
                results = produtoService.findByNomeContainsIgnoreCaseAndCategoria_IdCategoria(termo, idCategoria);
            } else {
                results = produtoService.findByNomeContains(termo);
            }
        }
        model.addAttribute("results", results);

        return "fragments/autocomplete :: options";
    }

    @PostMapping("/pdv/finalizar")
    @ResponseBody
    public ResponseEntity<String> pdvVenda(@RequestBody VendaDTO venda, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        VendaDTO created = vendaService.create(venda, username);

        return ResponseEntity.ok().body(created.observacao());
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