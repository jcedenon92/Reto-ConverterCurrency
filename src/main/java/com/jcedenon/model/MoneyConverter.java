package com.jcedenon.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jcedenon.auditing.Auditable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "moneys_converter")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MoneyConverter extends Auditable {

    @Id
    private String id;

    private TipoCambio tipoCambio;

    private Double montoOrigen;

    private Double montoCambiado;

}
