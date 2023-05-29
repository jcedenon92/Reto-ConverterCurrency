package com.jcedenon.service.impl;

import com.jcedenon.model.Role;
import com.jcedenon.repository.IGenericRepository;
import com.jcedenon.repository.IRoleRepository;
import com.jcedenon.service.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl extends CRUDImpl<Role, String> implements IRoleService {

    private final IRoleRepository repo;

    @Override
    protected IGenericRepository<Role, String> getRepository() {
        return repo;
    }
}
