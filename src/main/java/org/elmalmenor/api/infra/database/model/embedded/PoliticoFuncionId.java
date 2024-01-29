package org.elmalmenor.api.infra.database.model.embedded;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Data
@Embeddable
public class PoliticoFuncionId implements Serializable {

    private Integer idLegislador;
    private Integer idFuncion;

}
