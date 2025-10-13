package com.rssecurity.storemanager.service;

import com.rssecurity.storemanager.usuario.mapper.UsuarioMapper;
import com.rssecurity.storemanager.usuario.repository.UsuarioRepository;
import com.rssecurity.storemanager.usuario.service.UsuarioService;

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