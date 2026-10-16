package com.nexus.projetokallopolli.controller;

import com.nexus.projetokallopolli.dto.UsuarioRequest;
import com.nexus.projetokallopolli.dto.UsuarioResponse;
import com.nexus.projetokallopolli.entity.Cargo;
import com.nexus.projetokallopolli.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @GetMapping
    public List<UsuarioResponse> listar(
            @RequestParam(required = false) Cargo cargo,
            @RequestParam(required = false) Boolean ativo) {
        return service.listar(cargo, ativo);
    }

    @GetMapping("/{id}")
    public UsuarioResponse buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @GetMapping("/telefone/{telefone}")
    public UsuarioResponse buscarPorTelefone(@PathVariable String telefone) {
        return service.buscarPorTelefone(telefone);
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> criar(@Valid @RequestBody UsuarioRequest request) {
        UsuarioResponse criado = service.criar(request);
        return ResponseEntity.created(URI.create("/api/usuarios/" + criado.id())).body(criado);
    }

    @PutMapping("/{id}")
    public UsuarioResponse atualizar(@PathVariable Long id, @Valid @RequestBody UsuarioRequest request) {
        return service.atualizar(id, request);
    }

    @PatchMapping("/{id}/interacao")
    public UsuarioResponse registrarInteracao(@PathVariable Long id) {
        return service.registrarInteracao(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        service.excluir(id);
    }
}
