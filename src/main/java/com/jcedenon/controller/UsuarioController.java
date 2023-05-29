package com.jcedenon.controller;

import com.jcedenon.model.Usuario;
import com.jcedenon.service.IUsuarioService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/v2/usuarios")
@RequiredArgsConstructor
@SecurityRequirement(name = "jwt")
public class UsuarioController {

    private final IUsuarioService service;

    @GetMapping
    public Mono<ResponseEntity<List<Usuario>>> findAll(){
        return service.findAll()
                .collectList()
                .map(list -> {
                    if(list.isEmpty())
                        return ResponseEntity.noContent().build();
                    else
                        return ResponseEntity.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(list);
                });
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Usuario>> findById(@PathVariable("id") String id){
        return service.findById(id)
                .map(usuario -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(usuario))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<Usuario>> save(@Valid @RequestBody Usuario usuario, final ServerHttpRequest request){
        return service.save(usuario)
                .map(e -> ResponseEntity
                        .created(URI.create(request.getURI().toString().concat("/").concat(e.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(e))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Usuario>> update(@PathVariable("id") String id, @RequestBody Usuario usuario){
        usuario.setId(id);

        Mono<Usuario> monoBody = Mono.just(usuario);
        Mono<Usuario> monoDB = service.findById(id);

        return monoDB.zipWith(monoBody, (db, b) -> {
                    db.setUsername(b.getUsername());
                    db.setPassword(b.getPassword());
                    db.setStatus(b.getStatus());
                    db.setRoles(b.getRoles());
                    return db;
                })
                .flatMap(service::update)
                .map(e -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(e))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable("id") String id){
        return service.findById(id)
                .flatMap(e -> service.deleteById(e.getId())
                            //.then(Mono.just(ResponseEntity.ok().<Void>build()));
                            .thenReturn(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
