package com.jcedenon.model;

import com.jcedenon.auditing.Auditable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "tipos_cambio")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TipoCambio extends Auditable {

    @Id
    @EqualsAndHashCode.Include
    private String id;

    @Field(name = "moneda_origen")
    private Moneda monedaOrigen;//USD

    @Field(name = "moneda_destino")
    private Moneda monedaDestino;//MXN

    @Field(name = "valor_cambio")
    private Double valorCambio; //1 USD = 20.00 MXN

    private String descripcion;
    
    private String fecha;//2021-07-01 12:00:00
}
