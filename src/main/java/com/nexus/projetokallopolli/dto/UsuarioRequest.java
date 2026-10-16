package com.nexus.projetokallopolli.dto;

import com.nexus.projetokallopolli.entity.Cargo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UsuarioRequest(
        @NotBlank(message = "Nome é obrigatório")
        String nome,

        @NotBlank(message = "Telefone é obrigatório")
        @Size(max = 20, message = "Telefone deve ter no máximo 20 caracteres")
        String telefone,

        @NotNull(message = "Cargo é obrigatório")
        Cargo cargo,

        Boolean ativo
) {}
