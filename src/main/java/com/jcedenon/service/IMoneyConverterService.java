package com.jcedenon.service;

import com.jcedenon.model.Moneda;
import com.jcedenon.model.MoneyConverter;
import reactor.core.publisher.Mono;

public interface IMoneyConverterService extends ICRUD<MoneyConverter, String>{

    Mono<MoneyConverter> saveTransactional(MoneyConverter moneyConverter);
}
