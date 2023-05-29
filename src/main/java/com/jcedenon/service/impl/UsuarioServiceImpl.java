package com.jcedenon.service.impl;

import com.jcedenon.model.Usuario;
import com.jcedenon.repository.IGenericRepository;
import com.jcedenon.repository.IRoleRepository;
import com.jcedenon.repository.IUsuarioRepository;
import com.jcedenon.security.UserSecurity;
import com.jcedenon.service.IUsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl extends CRUDImpl<Usuario, String> implements IUsuarioService {

    private final IUsuarioRepository repo;
    private final IRoleRepository rolRepo;
    private final BCryptPasswordEncoder bcrypt;

    @Override
    protected IGenericRepository<Usuario, String> getRepository() {
        return repo;
    }

    @Override
    public Mono<Usuario> saveHash(Usuario usuario) {
        usuario.setPassword(bcrypt.encode(usuario.getPassword()));
        return repo.save(usuario);
    }

    @Override
    public Mono<UserSecurity> searchByUser(String username) {
        Mono<Usuario> monoUser = repo.findOneByUsername(username);
        List<String> roles = new ArrayList<>();

        return monoUser.flatMap( u -> {
            return Flux.fromIterable(u.getRoles())
                    .flatMap(rol -> {
                        return rolRepo.findById(rol.getId())
                                .map(r -> {
                                    roles.add(r.getName());
                                    return r;
                                });
                    }).collectList().flatMap(list -> {
                        u.setRoles(list);
                        return Mono.just(u);
                    });
        }).flatMap(u -> Mono.just(new UserSecurity(u.getUsername(), u.getPassword(), u.getStatus(), roles)));
    }
}
