package com.rssecurity.storemanager.service;

import com.rssecurity.storemanager.mapper.UsuarioMapper;
import com.rssecurity.storemanager.repository.UsuarioRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;

class UsuarioServiceTest {
    @InjectMocks
    private UsuarioService service;
    @Mock
    private UsuarioRepository repository;
    @Mock
    private UsuarioMapper mapper;

}