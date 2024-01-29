package org.elmalmenor.api.infra.database.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Data
@Entity
@Table
public class PoliticoComision implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPoliticoComision;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Politico politico;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Comision comision;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private CargoComision cargoComision;

}
