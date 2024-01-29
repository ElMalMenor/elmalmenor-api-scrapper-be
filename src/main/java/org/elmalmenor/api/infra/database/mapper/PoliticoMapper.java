package org.elmalmenor.api.infra.database.mapper;

import org.elmalmenor.api.domain.model.ComisionModel;
import org.elmalmenor.api.domain.model.DiputadoModel;
import org.elmalmenor.api.domain.model.ProyectoModel;
import org.elmalmenor.api.infra.database.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class PoliticoMapper {

    @Mapping(target = "nombre", source = "nombre")
    @Mapping(target = "apellido", source = "apellido")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "profesion", source = "profesion")
    @Mapping(target = "nacimiento", source = "nacimiento")
    @Mapping(target = "imagenUrl", source = "imagenUrl")
    @Mapping(target = "bloque", ignore = true)
    @Mapping(target = "distrito", ignore = true)
    @Mapping(target = "funciones", ignore = true)
    @Mapping(target = "proyectos", ignore = true)
    @Mapping(target = "comisiones", ignore = true)
    public abstract Politico map(DiputadoModel diputadoModel);

    @Mapping(target = "descripcion", source = "bloque")
    @Mapping(target = "partido", ignore = true)
    public abstract Bloque mapBloque(DiputadoModel diputadoModel);

    @Mapping(target = "descripcion", source = "distrito")
    public abstract Distrito mapDistrito(DiputadoModel diputadoModel);

    @Mapping(target = "idProyecto", source = "expediente")
    @Mapping(target = "sumario", source = "sumario")
    @Mapping(target = "fecha", source = "fecha")
    public abstract Proyecto mapProyecto(ProyectoModel proyectoModel);

    @Mapping(target = "descripcion", source = "tipo")
    public abstract TipoProyecto mapTipoProyecto(ProyectoModel proyectoModel);

    @Mapping(target = "nombre", source = "funcion")
    @Mapping(target = "descripcion", source = "funcion")
    public abstract Funcion mapFuncion(DiputadoModel diputadoModel);

    @Mapping(target = "descripcion", source = "cargo")
    public abstract CargoComision mapCargoComision(ComisionModel comisionModel);

    @Mapping(target = "nombre", source = "nombre")
    @Mapping(target = "descripcion", source = "nombre")
    public abstract Comision mapComision(ComisionModel comisionModel);

}
