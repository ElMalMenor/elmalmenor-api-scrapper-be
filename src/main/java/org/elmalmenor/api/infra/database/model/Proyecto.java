package org.elmalmenor.api.infra.database.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.sql.Date;

@Data
@Entity
@Table
public class Proyecto implements Serializable {

    @Id
    private String idProyecto;
    private String sumario;
    private Date fecha;

    @ManyToOne
    @JoinColumn(name="id_tipo_proyecto")
    private TipoProyecto tipoProyecto;

}
