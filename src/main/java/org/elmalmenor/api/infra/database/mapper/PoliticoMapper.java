package org.elmalmenor.api.infra.database.mapper;

import org.elmalmenor.api.domain.model.ComisionModel;
import org.elmalmenor.api.domain.model.PoliticoModel;
import org.elmalmenor.api.domain.model.ProyectoModel;
import org.elmalmenor.api.infra.database.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class PoliticoMapper {

    @Mapping(target = "firstName", source = "nombre")
    @Mapping(target = "lastName", source = "apellido")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "birthDate", source = "nacimiento")
    @Mapping(target = "imagePath", source = "imagenUrl")
    @Mapping(target = "periods", ignore = true)
    @Mapping(target = "projects", ignore = true)
    @Mapping(target = "professions", ignore = true)
    public abstract Politician map(PoliticoModel diputadoModel);

    @Mapping(target = "name", source = "bloque")
    @Mapping(target = "politicianParty", ignore = true)
    public abstract Bloc mapBloc(PoliticoModel diputadoModel);

    @Mapping(target = "name", source = "partido")
    public abstract PoliticianParty mapPoliticianParty(PoliticoModel diputadoModel);

    @Mapping(target = "name", source = "distrito")
    public abstract District mapDistrict(PoliticoModel diputadoModel);

    @Mapping(target = "id", source = "expediente")
    @Mapping(target = "name", source = "sumario")
    @Mapping(target = "date", source = "fecha")
    public abstract Project mapProject(ProyectoModel proyectoModel);

    @Mapping(target = "name", source = "tipo")
    public abstract ProjectType mapProjectType(ProyectoModel proyectoModel);

    @Mapping(target = "name", source = "funcion")
    public abstract PublicFunction mapPublicPunction(PoliticoModel diputadoModel);

    @Mapping(target = "name", source = "cargo")
    public abstract CommissionPosition mapCommissionPosition(ComisionModel comisionModel);

    @Mapping(target = "name", source = "nombre")
    public abstract Commission mapComission(ComisionModel comisionModel);

}
