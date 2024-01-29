package org.elmalmenor.api.infra.database.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
@Entity
@Table
public class CargoComision {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCargoComision;
    private String descripcion;

    @OneToMany(mappedBy = "cargoComision", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<PoliticoComision> comisiones;
}
