package com.jcedenon.repository;

import com.jcedenon.model.Usuario;
import reactor.core.publisher.Mono;

public interface IUsuarioRepository extends IGenericRepository<Usuario, String>{

    Mono<Usuario> findOneByUsername(String username);
}
