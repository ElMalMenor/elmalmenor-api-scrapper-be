package org.elmalmenor.api.domain.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class ProyectoModel {

    private String expediente;
    private String tipo;
    private String sumario;
    private Date fecha;

}
