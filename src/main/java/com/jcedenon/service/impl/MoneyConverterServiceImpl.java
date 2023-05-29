package com.jcedenon.service.impl;

import com.jcedenon.model.MoneyConverter;
import com.jcedenon.repository.IGenericRepository;
import com.jcedenon.repository.IMoneyConverterRepository;
import com.jcedenon.repository.ITipoCambioRepository;
import com.jcedenon.service.IMoneyConverterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class MoneyConverterServiceImpl extends CRUDImpl<MoneyConverter, String> implements IMoneyConverterService {

    private final IMoneyConverterRepository repo;

    private final ITipoCambioRepository tipoCambioRepo;

    @Override
    protected IGenericRepository<MoneyConverter, String> getRepository() {
        return repo;
    }

    //Transaccion para calcular el montoCambiado
    @Override
    @Transactional
    public Mono<MoneyConverter> saveTransactional(MoneyConverter moneyConverter) {
        //Obtener el valorCambio de la clase TipoCambio
        return tipoCambioRepo.findById(moneyConverter.getTipoCambio().getId())
                .flatMap(tipoCambio -> {
                    moneyConverter.setMontoCambiado(moneyConverter.getMontoOrigen() * tipoCambio.getValorCambio());
                    return repo.save(moneyConverter);
                });
    }
}
