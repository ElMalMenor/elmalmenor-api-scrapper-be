package org.elmalmenor.api.infra.database.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@Data
@Entity
@Table
public class Comision implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idComision;
    private String nombre;
    private String descripcion;

    @ManyToOne
    @JoinColumn(name="id_tipo_comision")
    private TipoComision tipoComision;

    @OneToMany(mappedBy = "comision", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<PoliticoComision> comisiones;
}
