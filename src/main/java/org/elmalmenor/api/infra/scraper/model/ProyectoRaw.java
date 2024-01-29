package org.elmalmenor.api.infra.scraper.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProyectoRaw {

    private String expediente;
    private String tipo;
    private String sumario;
    private String fecha;

}
