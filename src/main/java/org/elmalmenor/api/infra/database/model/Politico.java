package org.elmalmenor.api.infra.database.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.sql.Date;
import java.util.Set;

@Data
@Entity
@Table
@EqualsAndHashCode
public class Politico implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idLegislador;
    private String nombre;
    private String apellido;
    private String email;
    private Boolean presidenteBloque;
    private String profesion;
    private Date nacimiento;
    private String imagenUrl;

    @ManyToOne
    @JoinColumn(name="id_bloque")
    private Bloque bloque;

    @ManyToOne
    @JoinColumn(name="id_distrito")
    private Distrito distrito;

    @OneToMany(mappedBy = "politico", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PoliticoFuncion> funciones;

    @ManyToMany
    @JoinTable(name = "politico_proyecto",
            joinColumns = { @JoinColumn(name = "politico_id_legislador")},
            inverseJoinColumns = { @JoinColumn(name = "proyecto_id_proyecto")}
    )
    private Set<Proyecto> proyectos;

    @OneToMany(mappedBy = "politico", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<PoliticoComision> comisiones;

}
