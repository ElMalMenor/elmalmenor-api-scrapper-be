package org.elmalmenor.api.infra.database.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.elmalmenor.api.infra.database.model.embedded.PoliticoFuncionId;

import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table
@NoArgsConstructor
public class PoliticoFuncion implements Serializable {

    @EmbeddedId
    private PoliticoFuncionId id;

    @ManyToOne
    @MapsId("idLegislador")
    @ToString.Exclude
    private Politico politico;

    @ManyToOne
    @MapsId("idFuncion")
    private Funcion funcion;

    private Date periodoInicio;
    private Date periodoFin;
    private Boolean activa;


    public PoliticoFuncion(Politico politico, Funcion funcion, Date periodoInicio, Date periodoFin, Boolean activa) {
        this.politico = politico;
        this.funcion = funcion;
        this.periodoInicio = periodoInicio;
        this.periodoFin = periodoFin;
        this.activa = activa;
        generateId();
    }

    private void generateId() {
        PoliticoFuncionId id = new PoliticoFuncionId();

        id.setIdFuncion(funcion.getIdFuncion());
        id.setIdLegislador(politico.getIdLegislador());

        this.id = id;
    }
}
