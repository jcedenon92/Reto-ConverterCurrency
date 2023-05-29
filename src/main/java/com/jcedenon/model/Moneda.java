package com.jcedenon.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jcedenon.auditing.Auditable;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "monedas")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Moneda extends Auditable {

    @Id
    @EqualsAndHashCode.Include
    private String id;

    @Size(min = 3)
    private String nombre; //Dolar, Euro, Peso, etc
    private String simbolo; //$, €, etc
    private String codigo; //USD, EUR, MXN, etc
    private String pais; //Estados Unidos, España, México, etc
}
