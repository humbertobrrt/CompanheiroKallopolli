package com.nexus.projetokallopolli.service;

import com.nexus.projetokallopolli.dto.UsuarioRequest;
import com.nexus.projetokallopolli.dto.UsuarioResponse;
import com.nexus.projetokallopolli.entity.Cargo;
import com.nexus.projetokallopolli.entity.Usuario;
import com.nexus.projetokallopolli.exception.ConflitoException;
import com.nexus.projetokallopolli.exception.RecursoNaoEncontradoException;
import com.nexus.projetokallopolli.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponse> listar(Cargo cargo, Boolean ativo) {
        List<Usuario> usuarios;
        if (cargo != null) {
            usuarios = repository.findByCargo(cargo);
        } else if (ativo != null) {
            usuarios = repository.findByAtivo(ativo);
        } else {
            usuarios = repository.findAll();
        }
        return usuarios.stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public UsuarioResponse buscarPorId(Long id) {
        return toResponse(buscarEntidade(id));
    }

    @Transactional(readOnly = true)
    public UsuarioResponse buscarPorTelefone(String telefone) {
        Usuario usuario = repository.findByTelefone(telefone)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));
        return toResponse(usuario);
    }

    @Transactional
    public UsuarioResponse criar(UsuarioRequest request) {
        if (repository.existsByTelefone(request.telefone())) {
            throw new ConflitoException("Já existe usuário com esse telefone");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(request.nome());
        usuario.setTelefone(request.telefone());
        usuario.setCargo(request.cargo());
        usuario.setAtivo(request.ativo() == null ? true : request.ativo());
        usuario.setUltimaInteracao(OffsetDateTime.now());

        return toResponse(repository.save(usuario));
    }

    @Transactional
    public UsuarioResponse atualizar(Long id, UsuarioRequest request) {
        Usuario usuario = buscarEntidade(id);

        repository.findByTelefone(request.telefone())
                .filter(outro -> !outro.getId().equals(id))
                .ifPresent(outro -> { throw new ConflitoException("Já existe usuário com esse telefone"); });

        usuario.setNome(request.nome());
        usuario.setTelefone(request.telefone());
        usuario.setCargo(request.cargo());
        if (request.ativo() != null) {
            usuario.setAtivo(request.ativo());
        }

        return toResponse(repository.save(usuario));
    }

    @Transactional
    public UsuarioResponse registrarInteracao(Long id) {
        Usuario usuario = buscarEntidade(id);
        usuario.setUltimaInteracao(OffsetDateTime.now());
        return toResponse(repository.save(usuario));
    }

    @Transactional
    public void excluir(Long id) {
        Usuario usuario = buscarEntidade(id);
        repository.delete(usuario);
    }

    private Usuario buscarEntidade(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado: " + id));
    }

    private UsuarioResponse toResponse(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getTelefone(),
                usuario.getCreatedAt(),
                usuario.getUltimaInteracao(),
                usuario.getAtivo(),
                usuario.getCargo()
        );
    }
}
