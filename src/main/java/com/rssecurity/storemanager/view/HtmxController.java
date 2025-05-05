package com.rssecurity.storemanager.view;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rssecurity.storemanager.dto.ProdutoDTO;
import com.rssecurity.storemanager.dto.VendaDTO;
import com.rssecurity.storemanager.service.ProdutoService;
import com.rssecurity.storemanager.service.VendaService;

import org.springframework.web.bind.annotation.PostMapping;
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
    public String getMethodName(@RequestParam String termo, @RequestParam(required = false) String tipo,
            @RequestParam(required = false) Long idCategoria, Model model) {
        List<ProdutoDTO> results;
        tipo = "nome";
        if (tipo.toLowerCase().equals("id")) {
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
    public ResponseEntity<ProdutoDTO> getProduto(@PathVariable Long id) {
        ProdutoDTO produto = produtoService.findById(id);
        return ResponseEntity.ok(produto);
    }

    @PostMapping("/pdv/finalizar")
    @ResponseBody
    public ResponseEntity<VendaDTO> postMethodName(@RequestBody VendaDTO venda) {
        return ResponseEntity.ok(vendaService.create(venda));
    }
    

}
