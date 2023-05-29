package com.jcedenon.handler;

import com.jcedenon.model.Moneda;
import com.jcedenon.service.IMonedaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Component
@RequiredArgsConstructor
public class MonedaHandler {

    private final IMonedaService service;

    /**
     * Enfoque funcional para obtener todos los registros del document moneda
     * @param req
     * @return
     */
    public Mono<ServerResponse> findAll(ServerRequest req){
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.findAll(), Moneda.class);
    }

    /**
     * Enfoque funcional para obtener un registro del document moneda por id
     * @param req
     * @return
     */
    public Mono<ServerResponse> findById(ServerRequest req){
        String id = req.pathVariable("id");

        return service.findById(id)
                .flatMap(moneda -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Mono.just(moneda), Moneda.class)
                )
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    /**
     * Enfoque funcional para crear un registro en el document moneda
     * @param req
     * @return
     */
    public Mono<ServerResponse> create(ServerRequest req) {
        Mono<Moneda> monoMoneda = req.bodyToMono(Moneda.class);

        return monoMoneda
                .flatMap(service::save)
                .flatMap(moneda -> ServerResponse
                        .created(URI.create(req.uri().toString().concat("/").concat(moneda.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(moneda))
                );
    }

    /**
     * Enfoque funcional para actualizar un registro en el document moneda
     * @param req
     * @return
     */
    public Mono<ServerResponse> update(ServerRequest req) {
        String id = req.pathVariable("id");

        Mono<Moneda> monoMoneda = req.bodyToMono(Moneda.class);
        Mono<Moneda> monoDB = service.findById(id);

        return monoDB
                .zipWith(monoMoneda, (db, mon) -> {
                    db.setNombre(mon.getNombre());
                    db.setSimbolo(mon.getSimbolo());
                    db.setCodigo(mon.getCodigo());
                    db.setPais(mon.getPais());
                    return db;
                })
                .flatMap(service::update)
                .flatMap(moneda -> ServerResponse
                        .created(URI.create(req.uri().toString().concat("/").concat(moneda.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(moneda))
                )
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    /**
     * Enfoque funcional para eliminar un registro en el document moneda
     * @param req
     * @return
     */
    public Mono<ServerResponse> delete(ServerRequest req) {
        String id = req.pathVariable("id");

        return service.findById(id)
                .flatMap(moneda -> service.deleteById(moneda.getId())
                        .then(ServerResponse.noContent().build())
                )
                .switchIfEmpty(ServerResponse.notFound().build());
    }
}
