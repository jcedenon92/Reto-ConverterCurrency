package com.jcedenon.service;

import com.jcedenon.model.Usuario;
import com.jcedenon.security.UserSecurity;
import reactor.core.publisher.Mono;

public interface IUsuarioService extends ICRUD<Usuario, String>{

    Mono<Usuario> saveHash(Usuario usuario);

    Mono<UserSecurity> searchByUser(String username);
}
