package com.jcedenon.controller;

import com.jcedenon.model.MoneyConverter;
import com.jcedenon.service.IMoneyConverterService;
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
@RequestMapping("/v2/money_converters")
@RequiredArgsConstructor
@SecurityRequirement(name = "jwt")
public class MoneyConverterController {

    private final IMoneyConverterService service;

    @GetMapping
    public Mono<ResponseEntity<List<MoneyConverter>>> findAll(){
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
    public Mono<ResponseEntity<MoneyConverter>> findById(@PathVariable("id") String id){
        return service.findById(id)
                .map(moneyConverter -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(moneyConverter))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /*@PostMapping
    public Mono<ResponseEntity<MoneyConverter>> save(@Valid @RequestBody MoneyConverter moneyConverter, final ServerHttpRequest request){
        return service.save(moneyConverter)
                .map(e -> ResponseEntity
                        .created(URI.create(request.getURI().toString().concat("/").concat(e.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(e))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }*/

    //POST para guardar un nuevo registro y hacer el calculo del monto cambiado
    @PostMapping
    public Mono<ResponseEntity<MoneyConverter>> save(@Valid @RequestBody MoneyConverter moneyConverter, final ServerHttpRequest request){
        Mono<MoneyConverter> obj = service.saveTransactional(moneyConverter);
        return obj.map(e -> ResponseEntity
                .created(URI.create(request.getURI().toString().concat("/").concat(e.getId())))
                .contentType(MediaType.APPLICATION_JSON)
                .body(e))
                .defaultIfEmpty(ResponseEntity.notFound().build());
        /*return service.save(moneyConverter)
                .map(e -> ResponseEntity
                        .created(URI.create(request.getURI().toString().concat("/").concat(e.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(e))
                .defaultIfEmpty(ResponseEntity.notFound().build());*/
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<MoneyConverter>> update(@PathVariable("id") String id, @RequestBody MoneyConverter moneyConverter){
        moneyConverter.setId(id);

        Mono<MoneyConverter> monoBody = Mono.just(moneyConverter);
        Mono<MoneyConverter> monoDB = service.findById(id);

        return monoDB.zipWith(monoBody, (db, b) -> {
                    db.setTipoCambio(b.getTipoCambio());
                    db.setMontoOrigen(b.getMontoOrigen());
                    db.setMontoCambiado(b.getMontoCambiado());
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
