package com.nexus.projetokallopolli.dto;

import com.nexus.projetokallopolli.entity.Cargo;
import java.time.OffsetDateTime;

public record UsuarioResponse(
        Long id,
        String nome,
        String telefone,
        OffsetDateTime createdAt,
        OffsetDateTime ultimaInteracao,
        Boolean ativo,
        Cargo cargo
) {}
