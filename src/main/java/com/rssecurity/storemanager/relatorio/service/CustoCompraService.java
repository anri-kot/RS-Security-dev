package com.rssecurity.storemanager.relatorio.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.rssecurity.storemanager.infra.exception.ResourceNotFoundException;
import com.rssecurity.storemanager.relatorio.dto.CustoCompraDTO;
import com.rssecurity.storemanager.relatorio.mapper.CustoCompraMapper;
import com.rssecurity.storemanager.relatorio.repository.CustoCompraRepository;

@Service
public class CustoCompraService {
    private final CustoCompraRepository repository;

    public CustoCompraService(CustoCompraRepository repository) {
        this.repository = repository;
    }

    public CustoCompraDTO findById(Long id) {
        return CustoCompraMapper.toDTO(
                repository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Relatório de compra não encontrado.")));
    }

    public List<CustoCompraDTO> findAllById(Iterable<Long> ids) {
        return repository.findAllById(ids).stream()
                .map(CustoCompraMapper::toDTO).toList();
    }

    public BigDecimal calculateCustoTotalBetween(LocalDate dataInicio, LocalDate dataFim) {
        return repository.calcularCustoTotalBetween(dataInicio.atStartOfDay(), dataFim.atTime(LocalTime.MAX));
    }
}
