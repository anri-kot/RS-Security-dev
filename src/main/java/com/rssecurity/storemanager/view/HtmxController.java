package com.rssecurity.storemanager.view;

import java.util.ArrayList;
import java.util.List;

import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rssecurity.storemanager.dto.ProdutoDTO;
import com.rssecurity.storemanager.dto.VendaDTO;
import com.rssecurity.storemanager.exception.ConflictException;
import com.rssecurity.storemanager.service.ProdutoService;
import com.rssecurity.storemanager.service.VendaService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
public class HtmxController {
    private ProdutoService produtoService;
    private VendaService vendaService;

    public HtmxController(ProdutoService produtoService, VendaService vendaService) {
        this.produtoService = produtoService;
        this.vendaService = vendaService;
    }

    @GetMapping("/pdv/autocomplete")
    public String pdvAutocomplete(@RequestParam String termo, @RequestParam(required = false) String tipo,
            @RequestParam(required = false) Long idCategoria, Model model) {
        List<ProdutoDTO> results;
        
        if (tipo != null && tipo.toLowerCase().equals("id")) {
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

    @GetMapping("/pdv/produto/{id}")
    @ResponseBody
    public ResponseEntity<ProdutoDTO> pdvProduto(@PathVariable Long id) {
        ProdutoDTO produto = produtoService.findById(id);
        return ResponseEntity.ok(produto);
    }

    @PostMapping("/pdv/finalizar")
    @ResponseBody
    public ResponseEntity<VendaDTO> pdvVenda(@RequestBody VendaDTO venda, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        return ResponseEntity.ok(vendaService.create(venda, username));
    }

    @PutMapping("/produtos/update/{id}")
    @ResponseBody
    public ResponseEntity<?> createProduto(@RequestBody ProdutoDTO produto, @PathVariable Long id) throws ConflictException {
        if (!id.equals(produto.idProduto())) {
            throw new ConflictException("ID do produto e ID especificado no caminho não correspondem. IDs: " + id + " e " + produto.idProduto());
        }
        produtoService.update(id, produto);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/produtos/create")
    @ResponseBody
    public ResponseEntity<?> postMethodName(@RequestBody ProdutoDTO produto) throws BadRequestException {
        if (produto.idProduto() != null) {
            throw new BadRequestException("ID do produto não deve ser definido.");
        }

        produtoService.create(produto);
        
        return ResponseEntity.noContent().build();
    }
    
}
