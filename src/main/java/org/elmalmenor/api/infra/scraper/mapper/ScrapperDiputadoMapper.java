package org.elmalmenor.api.infra.scraper.mapper;

import org.elmalmenor.api.infra.scraper.model.ComisionRaw;
import org.elmalmenor.api.infra.scraper.model.ContactoRaw;
import org.elmalmenor.api.infra.scraper.model.PoliticoRaw;
import org.elmalmenor.api.infra.scraper.model.ProyectoRaw;
import org.htmlunit.html.DomNode;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

@Mapper(componentModel = "spring")
public abstract class ScrapperDiputadoMapper {

    @Mapping(target = "imagenUrl", expression = "java(mapRefAttribute(domNode, 1, \"img\", \"src\"))")
    @Mapping(target = "detallePath", expression = "java(mapRefAttribute(domNode, 2, \"a\", \"href\"))")
    @Mapping(target = "nombre", expression = "java(mapRef(domNode, 2, \"a\").split(\",\")[1].trim())")
    @Mapping(target = "apellido", expression = "java(mapRef(domNode, 2, \"a\").split(\",\")[0].trim())")
    @Mapping(target = "distrito", expression = "java(map(domNode, 3))")
    @Mapping(target = "mandato", expression = "java(map(domNode, 4))")
    @Mapping(target = "mandatoInicio", expression = "java(map(domNode, 5).isEmpty() ? null : map(domNode, 5))")
    @Mapping(target = "mandatoFin", expression = "java(map(domNode, 6).isEmpty() ? null : map(domNode, 6))")
    @Mapping(target = "bloque", expression = "java(map(domNode, 7))")
    @Mapping(target = "funcion", expression = "java(\"Diputado\")")
    @Mapping(target = "partido", ignore = true)
    public abstract PoliticoRaw map(DomNode domNode);

    public abstract Stream<PoliticoRaw> map(List<DomNode> domNodes);

    protected String map(DomNode domNode, int childNumber) {
        return domNode.querySelector("td:nth-child("+ childNumber +")").asNormalizedText();
    }

    protected String mapRefAttribute(DomNode domNode, int childNumber, String ref, String attr) {
        return domNode.querySelector("td:nth-child("+ childNumber +") " + ref)
                .getAttributes().getNamedItem(attr)
                .getNodeValue();
    }

    protected String mapRef(DomNode domNode, int childNumber, String ref) {
        return domNode.querySelector("td:nth-child("+ childNumber +") " + ref).asNormalizedText();
    }


    @Mapping(target = "telefono", ignore = true)
    @Mapping(target = "interno", ignore = true)
    @Mapping(target = "profesion", expression = "java(map(domNode, \".encabezadoProfesion span\"))")
    @Mapping(target = "nacimiento", expression = "java(map(domNode, \".encabezadoFecha span\"))")
    @Mapping(target = "email", expression = "java(map(domNode, \".col-12 a\").toLowerCase())")
    public abstract ContactoRaw mapContacto(DomNode domNode);

    protected String map(DomNode domNode, String htmlClazzQuery) {
        return Optional.of(domNode.querySelector(htmlClazzQuery).asNormalizedText()).orElse(null);
    }

    @Mapping(target = "nombre", expression = "java(map(domNode, 1))")
    @Mapping(target = "cargo", expression = "java(map(domNode, 2))")
    public abstract ComisionRaw mapComision(DomNode domNode);

    public abstract Set<ComisionRaw> mapComisiones(List<DomNode> domNodes);

    @Mapping(target = "expediente", expression = "java(mapRef(domNode, 1, \"a\"))")
    @Mapping(target = "tipo", expression = "java(map(domNode, 2))")
    @Mapping(target = "sumario", expression = "java(map(domNode, 3))")
    @Mapping(target = "fecha", expression = "java(map(domNode, 4))")
    public abstract ProyectoRaw mapProyectos(DomNode domNode);

    public abstract Set<ProyectoRaw> mapProyectos(List<DomNode> domNodes);
}
