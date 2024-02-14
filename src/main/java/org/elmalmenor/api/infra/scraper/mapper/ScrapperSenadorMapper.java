package org.elmalmenor.api.infra.scraper.mapper;

import org.elmalmenor.api.infra.scraper.model.ComisionRaw;
import org.elmalmenor.api.infra.scraper.model.ContactoRaw;
import org.elmalmenor.api.infra.scraper.model.PoliticoRaw;
import org.elmalmenor.api.infra.scraper.model.ProyectoRaw;
import org.htmlunit.html.DomNode;
import org.json.JSONObject;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

@Mapper(componentModel = "spring")
public abstract class ScrapperSenadorMapper {

    @Mapping(target = "imagenUrl", expression = "java(jsonObject.getString(\"FOTO\"))")
    @Mapping(target = "detallePath", expression = "java(\"/senadores/senador/\" + jsonObject.getString(\"ID\"))")
    @Mapping(target = "nombre", expression = "java(jsonObject.getString(\"NOMBRE\"))")
    @Mapping(target = "apellido", expression = "java(jsonObject.getString(\"APELLIDO\"))")
    @Mapping(target = "distrito", expression = "java(jsonObject.getString(\"PROVINCIA\"))")
    @Mapping(target = "mandato", expression = "java(mapMandato(jsonObject))")
    @Mapping(target = "mandatoInicio", expression = "java(jsonObject.getString(\"D_LEGAL\"))")
    @Mapping(target = "mandatoFin", expression = "java(jsonObject.getString(\"C_LEGAL\"))")
    @Mapping(target = "bloque", expression = "java(jsonObject.getString(\"BLOQUE\"))")
    @Mapping(target = "partido", expression = "java(jsonObject.getString(\"PARTIDO O ALIANZA\"))")
    @Mapping(target = "funcion", expression = "java(\"Senador\")")
    public abstract PoliticoRaw map(JSONObject jsonObject);

    public abstract Stream<PoliticoRaw> map(List<DomNode> domNodes);

    protected String mapMandato(JSONObject jsonObject) {
        return jsonObject.getString("D_LEGAL").split("-")[0]
                + "-" +
                jsonObject.getString("C_LEGAL").split("-")[0];
    }

    protected String map(DomNode domNode, int childNumber) {
        return domNode.querySelector("td:nth-child("+ childNumber +")").asNormalizedText();
    }

    protected String mapRef(DomNode domNode, int childNumber, String ref) {
        return domNode.querySelector("td:nth-child("+ childNumber +") " + ref).asNormalizedText();
    }

    @Mapping(target = "telefono", expression = "java(mapliSplit(domNode, 2))")
    @Mapping(target = "interno", expression = "java(mapliSplit(domNode, 3))")
    @Mapping(target = "profesion", ignore = true)
    @Mapping(target = "nacimiento", ignore = true)
    @Mapping(target = "email", expression = "java(mapli(domNode, 1).toLowerCase())")
    public abstract ContactoRaw mapContacto(DomNode domNode);

    protected String mapli(DomNode domNode, int childNumber) {
        return domNode.querySelector("li:nth-child("+ childNumber +")").asNormalizedText();
    }

    protected String mapliSplit(DomNode domNode, int childNumber) {
        String node = domNode.querySelector("li:nth-child("+ childNumber +")").asNormalizedText();

        if (node.contains("Hipólito"))
            return null;

        return node.split(":")[1].trim();
    }

    protected String map(DomNode domNode, String htmlClazzQuery) {
        return Optional.of(domNode.querySelector(htmlClazzQuery).asNormalizedText()).orElse(null);
    }

    @Mapping(target = "nombre", expression = "java(map(domNode, 1))")
    @Mapping(target = "cargo", expression = "java(map(domNode, 2))")
    public abstract ComisionRaw mapComision(DomNode domNode);

    public abstract Set<ComisionRaw> mapComisiones(List<DomNode> domNodes);

    @Mapping(target = "expediente", expression = "java(mapRef(domNode, 1, \"a\"))")
    @Mapping(target = "tipo", ignore = true)
    @Mapping(target = "sumario", expression = "java(map(domNode, 3))")
    @Mapping(target = "fecha", expression = "java(map(domNode, 2))")
    public abstract ProyectoRaw mapProyectos(DomNode domNode);

    public abstract Set<ProyectoRaw> mapProyectos(List<DomNode> domNodes);
}
