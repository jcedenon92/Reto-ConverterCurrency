package com.jcedenon.service.impl;

import com.jcedenon.model.TipoCambio;
import com.jcedenon.repository.IGenericRepository;
import com.jcedenon.repository.ITipoCambioRepository;
import com.jcedenon.service.ITipoCambioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TipoCambioServiceImpl extends CRUDImpl<TipoCambio, String> implements ITipoCambioService {

    private final ITipoCambioRepository repo;

    @Override
    protected IGenericRepository<TipoCambio, String> getRepository() {
        return repo;
    }
}
