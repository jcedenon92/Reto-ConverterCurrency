package com.jcedenon.model;

import com.jcedenon.auditing.Auditable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "usuarios")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Usuario extends Auditable {

    @Id
    @EqualsAndHashCode.Include
    private String id;
    private String username;
    private String password;
    private Boolean status;
    private List<Role> roles;
}
