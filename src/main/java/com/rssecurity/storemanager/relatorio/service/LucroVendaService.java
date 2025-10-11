package com.rssecurity.storemanager.relatorio.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.rssecurity.storemanager.infra.exception.ResourceNotFoundException;
import com.rssecurity.storemanager.relatorio.dto.LucroVendaDTO;
import com.rssecurity.storemanager.relatorio.mapper.LucroVendaMapper;
import com.rssecurity.storemanager.relatorio.model.LucroVenda;
import com.rssecurity.storemanager.relatorio.repository.LucroVendaRepository;

@Service
public class LucroVendaService {
    private final LucroVendaRepository repository;

    public LucroVendaService(LucroVendaRepository repository) {
        this.repository = repository;
    }

    public LucroVendaDTO findById(Long id) {
        LucroVenda lv = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venda não encontrada."));
        return LucroVendaMapper.toDTO(lv);
    }

    public List<LucroVendaDTO> findAll() {
        return repository.findAll().stream()
                .map(LucroVendaMapper::toDTO)
                .toList();
    }

    public List<LucroVendaDTO> findAllById(Iterable<Long> ids) {
        return repository.findAllById(ids).stream()
                .map(LucroVendaMapper::toDTO).
                toList();
    }

    public BigDecimal calculateLucroTotal(LocalDate dataInicio, LocalDate dataFim) {
        return repository.calcularLucroTotal(dataInicio.atStartOfDay(), dataFim.atTime(LocalTime.MAX));
    }

    public BigDecimal calculateCustoTotal(LocalDate dataInicio, LocalDate dataFim) {
        return repository.calcularCustoTotal(dataInicio.atStartOfDay(), dataFim.atTime(LocalTime.MAX));
    }
}
