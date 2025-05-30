package com.rssecurity.storemanager.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import com.rssecurity.storemanager.model.Venda;

public class VendaSpecification {

    public static Specification<Venda> withFilters(Map<String, String> filters) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filters.containsKey("observacao")) {
                predicates.add(cb.like(cb.lower(root.get("observacao")), "%" + filters.get("observacao").toLowerCase() + "%"));
            }

            if (filters.containsKey("usuario")) {
                predicates.add(cb.like(root.get("usuario").get("username"), "%" + filters.get("usuario") + "%"));
            }

            if (filters.containsKey("metodoPagamento")) {
                predicates.add(cb.equal(root.get("metodoPagamento"), filters.get("metodoPagamento")));
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
