package org.elmalmenor.api.infra.database.mapper;

import org.elmalmenor.api.domain.model.ComisionModel;
import org.elmalmenor.api.domain.model.DiputadoModel;
import org.elmalmenor.api.domain.model.ProyectoModel;
import org.elmalmenor.api.infra.database.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class PoliticoMapper {

    @Mapping(target = "firstName", source = "nombre")
    @Mapping(target = "lastName", source = "apellido")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "phone", ignore = true)
    @Mapping(target = "birthDate", source = "nacimiento")
    @Mapping(target = "imagePath", source = "imagenUrl")
    @Mapping(target = "periods", ignore = true)
    @Mapping(target = "projects", ignore = true)
    @Mapping(target = "professions", ignore = true)
    public abstract Politician map(DiputadoModel diputadoModel);

    @Mapping(target = "name", source = "profesion")
    public abstract Profession mapProfession(DiputadoModel diputadoModel);

    @Mapping(target = "name", source = "bloque")
    @Mapping(target = "politicianParty", ignore = true)
    public abstract Bloc mapBloc(DiputadoModel diputadoModel);

    @Mapping(target = "name", source = "distrito")
    public abstract District mapDistrict(DiputadoModel diputadoModel);

    @Mapping(target = "id", source = "expediente")
    @Mapping(target = "name", source = "sumario")
    @Mapping(target = "date", source = "fecha")
    public abstract Project mapProject(ProyectoModel proyectoModel);

    @Mapping(target = "name", source = "tipo")
    public abstract ProjectType mapProjectType(ProyectoModel proyectoModel);

    @Mapping(target = "name", source = "funcion")
    public abstract PublicFunction mapPublicPunction(DiputadoModel diputadoModel);

    @Mapping(target = "name", source = "cargo")
    public abstract CommissionPosition mapCommissionPosition(ComisionModel comisionModel);

    @Mapping(target = "name", source = "nombre")
    public abstract Commission mapComission(ComisionModel comisionModel);

}
