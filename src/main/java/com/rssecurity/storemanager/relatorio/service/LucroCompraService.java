package com.rssecurity.storemanager.relatorio.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.rssecurity.storemanager.relatorio.dto.LucroCompraDTO;
import com.rssecurity.storemanager.relatorio.mapper.LucroCompraMapper;
import com.rssecurity.storemanager.relatorio.repository.LucroCompraRepository;

@Service
public class LucroCompraService {
    private final LucroCompraRepository repository;

    public LucroCompraService(LucroCompraRepository repository) {
        this.repository = repository;
    }

    public List<LucroCompraDTO> findAllById(Iterable<Long> ids) {
        return repository.findAllById(ids).stream()
                .map(LucroCompraMapper::toDTO).
                toList();
    }
}
