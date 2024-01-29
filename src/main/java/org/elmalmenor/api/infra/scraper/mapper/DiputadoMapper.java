package org.elmalmenor.api.infra.scraper.mapper;

import org.apache.commons.codec.binary.Base64;
import org.elmalmenor.api.domain.model.ComisionModel;
import org.elmalmenor.api.domain.model.DiputadoModel;
import org.elmalmenor.api.domain.model.ProyectoModel;
import org.elmalmenor.api.infra.scraper.model.ComisionRaw;
import org.elmalmenor.api.infra.scraper.model.DiputadoRaw;
import org.elmalmenor.api.infra.scraper.model.ProyectoRaw;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Set;
import java.util.stream.Stream;

@Mapper(componentModel = "spring")
public abstract class DiputadoMapper {

    @Mapping(target = "imagenUrl", source = "imagenUrl")
    @Mapping(target = "nombre", source = "nombre")
    @Mapping(target = "apellido", source = "apellido")
    @Mapping(target = "distrito", source = "distrito")
    @Mapping(target = "mandatoInicio", dateFormat = "dd/MM/yyyy", source = "mandatoInicio")
    @Mapping(target = "mandatoFin", dateFormat = "dd/MM/yyyy", source = "mandatoFin")
    @Mapping(target = "partido", source = "bloque")
    @Mapping(target = "email", source = "contactoRaw.email")
    @Mapping(target = "nacimiento", dateFormat = "dd/MM/yyyy", source = "contactoRaw.nacimiento")
    @Mapping(target = "profesion", source = "contactoRaw.profesion")
    @Mapping(target = "proyectos", expression = "java(mapProyecto(diputado.getProyectoRaws()))")
    @Mapping(target = "comisiones", expression = "java(mapComision(diputado.getComisionRawSet()))")
    @Mapping(target = "funcion", source = "funcion")
    public abstract DiputadoModel map(DiputadoRaw diputado);

    public abstract Stream<DiputadoModel> map(Stream<DiputadoRaw> diputadosRaw);

    @Mapping(target = "fecha", dateFormat = "dd/MM/yyyy", source = "fecha")
    public abstract ProyectoModel mapProyecto(ProyectoRaw proyectoRaw);

    public abstract Set<ProyectoModel> mapProyecto(Set<ProyectoRaw> proyectoRaw);

    public abstract Set<ComisionModel> mapComision(Set<ComisionRaw> comisionRaw);

}
