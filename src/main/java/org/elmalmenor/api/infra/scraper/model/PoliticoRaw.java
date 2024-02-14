package org.elmalmenor.api.infra.scraper.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Getter
@Setter
@ToString
public class PoliticoRaw {

    private String imagenUrl;
    private String detallePath;
    private String nombre;
    private String apellido;
    private String distrito;
    private String mandato;
    private String mandatoInicio;
    private String mandatoFin;
    private String bloque;
    private String partido;
    private String funcion;

    private ContactoRaw contactoRaw;
    private Set<ComisionRaw> comisionRawSet;
    private Set<ProyectoRaw> proyectoRaws;

}
