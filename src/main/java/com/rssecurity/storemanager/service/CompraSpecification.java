package com.rssecurity.storemanager.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import com.rssecurity.storemanager.model.Compra;

public class CompraSpecification {
    
    public static Specification<Compra> withFilters(Map<String, String> filters) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filters.containsKey("observacao")) {
                predicates.add(cb.like(cb.lower(root.get("observacao")), "%" + filters.get("observacao").toLowerCase() + "%"));
            }

            if (filters.containsKey("termo")) {
                if (filters.get("tipo").contains("fornecedor")) {
                    predicates.add(cb.like(root.get("fornecedor").get("nome"), "%" + filters.get("termo") + "%"));
                } else if (filters.get("tipo").contains("produtoNome")) {
                    predicates.add(cb.like(root.get("itens").get("produto").get("nome"), "%" + filters.get("termo") + "%"));
                } else if (filters.get("tipo").contains("produtoId")) {
                    predicates.add(cb.equal(root.get("itens").get("produto").get("idProduto"), filters.get("termo")));
                }
            }

            if (filters.containsKey("dataInicio") && filters.containsKey("dataFim")) {
                LocalDateTime inicio = LocalDate.parse(filters.get("dataInicio")).atStartOfDay();
                LocalDateTime fim = LocalDate.parse(filters.get("dataFim")).atTime(LocalTime.MAX);
                predicates.add(cb.between(root.get("data"), inicio, fim));
            } else if (filters.containsKey("dataInicio")) {
                LocalDateTime inicio = LocalDate.parse(filters.get("dataInicio")).atStartOfDay();
                predicates.add(cb.greaterThanOrEqualTo(root.get("data"), inicio));
            } else if (filters.containsKey("dataFim")) {
                LocalDateTime fim = LocalDate.parse(filters.get("dataFim")).atTime(LocalTime.MAX);
                predicates.add(cb.lessThanOrEqualTo(root.get("data"), fim));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

}
