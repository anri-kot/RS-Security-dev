package com.rssecurity.storemanager.usuario.dto;

import java.util.List;

import com.rssecurity.storemanager.infra.view.dto.PageableView;

public record UsuarioViewDTO(
    List<UsuarioDTO> usuarios,

    int currentPage,
    int totalPages,
    int size
) implements PageableView {

}
