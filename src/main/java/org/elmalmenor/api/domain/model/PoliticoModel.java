package org.elmalmenor.api.domain.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@ToString
public class PoliticoModel {

    private String imagenUrl;
    private String nombre;
    private String apellido;
    private String distrito;
    private Date mandatoInicio;
    private Date mandatoFin;
    private String bloque;
    private String partido;
    private String email;
    private Date nacimiento;
    private String profesion;
    private String funcion;
    private String telefono;

    private Set<ProyectoModel> proyectos;
    private Set<ComisionModel> comisiones;

}
