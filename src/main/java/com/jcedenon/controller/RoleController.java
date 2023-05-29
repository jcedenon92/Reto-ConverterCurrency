package com.jcedenon.controller;

import com.jcedenon.model.Role;
import com.jcedenon.service.IRoleService;
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
@RequestMapping("/v2/roles")
@RequiredArgsConstructor
@SecurityRequirement(name = "jwt")
public class RoleController {

    private final IRoleService service;

    @GetMapping
    public Mono<ResponseEntity<List<Role>>> findAll(){
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
    public Mono<ResponseEntity<Role>> findById(@PathVariable("id") String id){
        return service.findById(id)
                .map(role -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(role))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<Role>> save(@Valid @RequestBody Role role, final ServerHttpRequest request){
        return service.save(role)
                .map(e -> ResponseEntity
                        .created(URI.create(request.getURI().toString().concat("/").concat(e.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(e))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Role>> update(@PathVariable("id") String id, @RequestBody Role role){
        role.setId(id);

        Mono<Role> monoBody = Mono.just(role);
        Mono<Role> monoDB = service.findById(id);

        return monoDB.zipWith(monoBody, (db, b) -> {
                    db.setName(b.getName());
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
