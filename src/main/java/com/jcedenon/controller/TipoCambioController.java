package com.jcedenon.controller;

import com.jcedenon.model.TipoCambio;
import com.jcedenon.service.ITipoCambioService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/v2/tipos_cambio")
@RequiredArgsConstructor
@SecurityRequirement(name = "jwt")
public class TipoCambioController {

    private final ITipoCambioService service;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public Mono<ResponseEntity<List<TipoCambio>>> findAll(){
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

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/{id}")
    public Mono<ResponseEntity<TipoCambio>> findById(@PathVariable("id") String id){
        return service.findById(id)
                .map(tipoCambio -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(tipoCambio))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<TipoCambio>> save(@Valid @RequestBody TipoCambio tipoCambio, final ServerHttpRequest request){
        return service.save(tipoCambio)
                .map(e -> ResponseEntity
                        .created(URI.create(request.getURI().toString().concat("/").concat(e.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(e))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<TipoCambio>> update(@PathVariable("id") String id, @RequestBody TipoCambio tipoCambio){
        tipoCambio.setId(id);

        Mono<TipoCambio> monoBody = Mono.just(tipoCambio);
        Mono<TipoCambio> monoDB = service.findById(id);

        return monoDB.zipWith(monoBody, (db, b) -> {
                    db.setMonedaOrigen(b.getMonedaOrigen());
                    db.setMonedaDestino(b.getMonedaDestino());
                    db.setValorCambio(b.getValorCambio());
                    db.setDescripcion(b.getDescripcion());
                    db.setFecha(b.getFecha());
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
