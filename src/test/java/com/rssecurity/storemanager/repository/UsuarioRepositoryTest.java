package com.rssecurity.storemanager.repository;

import com.rssecurity.storemanager.model.Usuario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioRepositoryTest {
    @Mock
    private UsuarioRepository repository;

    @Test
    void deveRetornarUsuariosMockados() {
        Usuario usu1 = new Usuario(
                null,
                "adm",
                "senha",
                "adm",
                "sobrenome",
                "cpf1",
                "email1",
                "endereco1",
                "telefone1",
                new BigDecimal("1000.0"),
                true);
        Usuario usu2 = new Usuario(
                null,
                "user",
                "senha",
                "adm2",
                "sobrenome2",
                "cpf2",
                "email2",
                "endereco2",
                "telefone2",
                new BigDecimal("1000.0"),
                true);
        List<Usuario> listaMock = List.of(usu1, usu2);

        when(repository.findByNomeContainingOrSobrenomeContaining("adm", "sobre")).thenReturn(listaMock);

        List<Usuario> resultado = repository.findByNomeContainingOrSobrenomeContaining("adm", "sobre");

        assertEquals(2, resultado.size());
        assertEquals("adm", resultado.get(0).getNome());
        assertEquals("sobrenome2", resultado.get(1).getSobrenome());
    }

}