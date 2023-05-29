package com.jcedenon.service.impl;

import com.jcedenon.model.Moneda;
import com.jcedenon.repository.IGenericRepository;
import com.jcedenon.repository.IMonedaRepository;
import com.jcedenon.service.IMonedaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MonedaServiceImpl extends CRUDImpl<Moneda, String> implements IMonedaService {

    private final IMonedaRepository repo;

    @Override
    protected IGenericRepository<Moneda, String> getRepository() {
        return repo;
    }
}
