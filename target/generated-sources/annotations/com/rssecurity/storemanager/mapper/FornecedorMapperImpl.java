package com.rssecurity.storemanager.mapper;

import com.rssecurity.storemanager.dto.FornecedorDTO;
import com.rssecurity.storemanager.model.Fornecedor;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-12T18:49:21-0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.14 (Arch Linux)"
)
@Component
public class FornecedorMapperImpl implements FornecedorMapper {

    @Override
    public FornecedorDTO toDTO(Fornecedor fornecedor) {
        if ( fornecedor == null ) {
            return null;
        }

        Long idFornecedor = null;
        String nome = null;
        String cnpj = null;
        String telefone = null;
        String email = null;

        idFornecedor = fornecedor.getIdFornecedor();
        nome = fornecedor.getnome();
        cnpj = fornecedor.getCnpj();
        telefone = fornecedor.getTelefone();
        email = fornecedor.getEmail();

        FornecedorDTO fornecedorDTO = new FornecedorDTO( idFornecedor, nome, cnpj, telefone, email );

        return fornecedorDTO;
    }

    @Override
    public Fornecedor toEntity(FornecedorDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Fornecedor fornecedor = new Fornecedor();

        fornecedor.setIdFornecedor( dto.idFornecedor() );
        fornecedor.setnome( dto.nome() );
        fornecedor.setCnpj( dto.cnpj() );
        fornecedor.setTelefone( dto.telefone() );
        fornecedor.setEmail( dto.email() );

        return fornecedor;
    }
}
