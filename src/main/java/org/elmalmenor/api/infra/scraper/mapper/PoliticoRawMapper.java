package org.elmalmenor.api.infra.scraper.mapper;

import org.elmalmenor.api.domain.model.ComisionModel;
import org.elmalmenor.api.domain.model.PoliticoModel;
import org.elmalmenor.api.domain.model.ProyectoModel;
import org.elmalmenor.api.infra.scraper.model.ComisionRaw;
import org.elmalmenor.api.infra.scraper.model.PoliticoRaw;
import org.elmalmenor.api.infra.scraper.model.ProyectoRaw;
import org.elmalmenor.api.utils.Utils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

@Mapper(componentModel = "spring", imports = { Utils.class })
public abstract class PoliticoRawMapper {

    @Mapping(target = "imagenUrl", source = "imagenUrl")
    @Mapping(target = "nombre", expression = "java(Utils.toTitleCase(politico.getNombre()))")
    @Mapping(target = "apellido", expression = "java(Utils.toTitleCase(politico.getApellido()))")
    @Mapping(target = "distrito", source = "distrito")
    @Mapping(target = "mandatoInicio", expression = "java(customDateFormat(politico.getMandatoInicio()))")
    @Mapping(target = "mandatoFin", expression = "java(customDateFormat(politico.getMandatoFin()))")
    @Mapping(target = "partido", source = "bloque")
    @Mapping(target = "email", source = "contactoRaw.email")
    @Mapping(target = "nacimiento", expression = "java(customDateFormat(politico.getContactoRaw().getNacimiento()))")
    @Mapping(target = "profesion", source = "contactoRaw.profesion")
    @Mapping(target = "proyectos", expression = "java(mapProyecto(politico.getProyectoRaws()))")
    @Mapping(target = "comisiones", expression = "java(mapComision(politico.getComisionRawSet()))")
    @Mapping(target = "funcion", source = "funcion")
    @Mapping(target = "telefono", source = "politico.contactoRaw.telefono")
    public abstract PoliticoModel map(PoliticoRaw politico);

    public abstract Stream<PoliticoModel> map(Stream<PoliticoRaw> diputadosRaw);

    @Mapping(target = "fecha", expression = "java(customDateFormat(proyectoRaw.getFecha()))")
    public abstract ProyectoModel mapProyecto(ProyectoRaw proyectoRaw);

    public abstract Set<ProyectoModel> mapProyecto(Set<ProyectoRaw> proyectoRaw);

    public abstract Set<ComisionModel> mapComision(Set<ComisionRaw> comisionRaw);

    protected Date customDateFormat(String date) {
        if (Objects.isNull(date))
            return null;

        Map<String, String> regexMap = Map.of(
                "regex1", "([0-9]{2})/([0-9]{2})/([0-9]{4})", "format1", "yyyy/MM/dd",
                "regex2", "([0-9]{4})/([0-9]{2})/([0-9]{2})", "format2", "dd/MM/yyyy",
                "regex3", "([0-9]{4})-([0-9]{2})-([0-9]{2})", "format3", "yyyy-MM-dd",
                "regex4", "([0-9]{2})-([0-9]{2})-([0-9]{4})", "format4", "dd-MM-yyyy");

        switch (date) {
            case String s when date.matches(regexMap.get("regex1")) -> { return dateFormat(date, regexMap.get("format1")); }
            case String s when date.matches(regexMap.get("regex2")) -> { return dateFormat(date, regexMap.get("format2")); }
            case String s when date.matches(regexMap.get("regex3")) -> { return dateFormat(date, regexMap.get("format3")); }
            case String s when date.matches(regexMap.get("regex4")) -> { return dateFormat(date, regexMap.get("format4")); }
            default -> { return null; }
        }
    }

    protected Date dateFormat(String date, String format) {
        try {
            return new SimpleDateFormat(format).parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

}
