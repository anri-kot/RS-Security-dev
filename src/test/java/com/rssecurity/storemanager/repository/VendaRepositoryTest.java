package com.rssecurity.storemanager.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import com.rssecurity.storemanager.venda.repository.VendaRepository;

@DataJpaTest
@ActiveProfiles("test")
public class VendaRepositoryTest {

    @Autowired
    private VendaRepository repository;

    @Autowired
    private TestEntityManager em;
}