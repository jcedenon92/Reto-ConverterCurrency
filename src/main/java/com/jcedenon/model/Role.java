package com.jcedenon.model;

import com.jcedenon.auditing.Auditable;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "roles")
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class Role extends Auditable {

    @Id
    @EqualsAndHashCode.Include
    private String id;
    private String name;

//    @CreatedDate
//    private LocalDateTime createdDate;
}
