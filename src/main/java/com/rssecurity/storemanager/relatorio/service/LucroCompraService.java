package com.rssecurity.storemanager.relatorio.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.rssecurity.storemanager.relatorio.dto.CustoCompraDTO;
import com.rssecurity.storemanager.relatorio.mapper.CustoCompraMapper;
import com.rssecurity.storemanager.relatorio.repository.CustoCompraRepository;

@Service
public class LucroCompraService {
    private final CustoCompraRepository repository;

    public LucroCompraService(CustoCompraRepository repository) {
        this.repository = repository;
    }

    public List<CustoCompraDTO> findAllById(Iterable<Long> ids) {
        return repository.findAllById(ids).stream()
                .map(CustoCompraMapper::toDTO).
                toList();
    }
}
