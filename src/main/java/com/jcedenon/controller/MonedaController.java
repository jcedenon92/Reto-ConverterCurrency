package com.jcedenon.controller;

import com.jcedenon.model.Moneda;
import com.jcedenon.service.IMonedaService;
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
@RequestMapping("/v2/monedas")
@RequiredArgsConstructor
@SecurityRequirement(name = "jwt")
@CrossOrigin(origins = "http://localhost:4200")
public class MonedaController {

    private final IMonedaService service;

    @GetMapping
    public Mono<ResponseEntity<List<Moneda>>> findAll(){
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
    public Mono<ResponseEntity<Moneda>> findById(@PathVariable("id") String id){
        return service.findById(id)
                .map(moneda -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(moneda))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<Moneda>> save(@Valid @RequestBody Moneda moneda, final ServerHttpRequest request){
        return service.save(moneda)
                .map(e -> ResponseEntity
                        .created(URI.create(request.getURI().toString().concat("/").concat(e.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(e))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Moneda>> update(@PathVariable("id") String id, @RequestBody Moneda moneda){
        moneda.setId(id);

        Mono<Moneda> monoBody = Mono.just(moneda);
        Mono<Moneda> monoDB = service.findById(id);

        return monoDB.zipWith(monoBody, (db, b) -> {
                    db.setNombre(b.getNombre());
                    db.setSimbolo(b.getSimbolo());
                    db.setCodigo(b.getCodigo());
                    db.setPais(b.getPais());
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
