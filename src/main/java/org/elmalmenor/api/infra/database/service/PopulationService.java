package org.elmalmenor.api.infra.database.service;

import lombok.RequiredArgsConstructor;
import org.elmalmenor.api.domain.model.DiputadoModel;
import org.elmalmenor.api.infra.database.mapper.PoliticoMapper;
import org.elmalmenor.api.infra.database.model.*;
import org.elmalmenor.api.infra.database.repository.*;
import org.elmalmenor.api.infra.database.spec.Populatable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class PopulationService implements Populatable {

    private final PoliticoMapper politicoMapper;

    private final ProjectRepository projectRepository;
    private final ProjectTypeRepository projectTypeRepository;
    private final PublicFunctionRepository publicFunctionRepository;
    private final CommissionTypeRepository commissionTypeRepository;
    private final DistrictRepository districtRepository;
    private final ComisionRepository comisionRepository;
    private final CommissionPositionRepository commissionPositionRepository;
    private final BlocRepository blocRepository;
    private final PoliticianRepository politicianRepository;
    private final PeriodReposistory periodReposistory;
    private final PoliticianCommissionRepository politicianCommissionRepository;
    private final ProfessionRepository professionRepository;

    @Override
    public void populateTable(Stream<DiputadoModel> diputados) {

        diputados.forEach(e -> {

            if (politicianRepository.findByFirstNameAndLastName(e.getNombre(), e.getApellido()).isEmpty()) {

                System.out.println("Procesando Diputado...: " + e.getApellido() + " | " + e);
                Politician politico = politicoMapper.map(e);

                constructProyectosRelation(politico, e);
                constructProfessionRelation(politico,  e);

                politicianRepository.save(politico);
                politicianRepository.flush();

                constructPeriodRelation(politico, e);

            } else {
                System.out.println("Ya Procesado Diputado...: " + e.getApellido());
            }


        });

    }

    private void constructPeriodRelation(Politician politician, DiputadoModel diputadoModel) {

        Period period = new Period();
        period.setStartDate(diputadoModel.getMandatoInicio());
        period.setEndDate(diputadoModel.getMandatoFin());
        period.setActive(true);

        period.setPolitician(politician);
        period.setBloc(getBlocOrSave(politicoMapper.mapBloc(diputadoModel)));
        period.setDistrict(getDistrictOrSave(politicoMapper.mapDistrict(diputadoModel)));
        period.setPublicFunction(getPublicFunctionOrSave(politicoMapper.mapPublicPunction(diputadoModel)));

        constructComissionsRelation(period, diputadoModel);

        periodReposistory.save(period);

    }

    private void constructComissionsRelation(Period period, DiputadoModel diputadoModel) {

        if (!Objects.nonNull(diputadoModel.getComisiones()))
            return;

        diputadoModel.getComisiones().forEach(e -> {

            CommissionPosition commissionPosition = getCommissionPositionOrSave(politicoMapper.mapCommissionPosition(e));
            Commission commission = getCommissionOrSave(politicoMapper.mapComission(e));

            PoliticianCommission politicoComision = new PoliticianCommission();

            politicoComision.setCommissionPosition(commissionPosition);
            politicoComision.setPeriod(period);
            politicoComision.setCommission(commission);

            politicianCommissionRepository.save(politicoComision);

        });

    }

    private void constructProyectosRelation(Politician politician, DiputadoModel diputadoModel) {

        Set<Project> projects = new HashSet<>();

        if (!Objects.nonNull(diputadoModel.getProyectos()))
            return;

        diputadoModel.getProyectos().forEach(p -> {
            ProjectType projectType = getProjectTypeOrSave(politicoMapper.mapProjectType(p));

            Project project = politicoMapper.mapProject(p);
            project.setProjectType(projectType);

            projects.add(getProjectOrSave(project));

        });

        politician.setProjects(projects);

    }

    private void constructProfessionRelation(Politician politician, DiputadoModel diputadoModel) {
        Set<Profession> professions = Arrays.stream(diputadoModel.getProfesion().split("-"))
                .map(e -> getProfessionOrSave(e.trim()))
                .collect(Collectors.toSet());

        politician.setProfessions(professions);
    }

    private void constructBlocRelation(Period period, DiputadoModel diputadoModel) {
        Bloc bloque = getBlocOrSave(politicoMapper.mapBloc(diputadoModel));
        period.setBloc(bloque);
    }

    private void constructFuncionRelation(Period period, DiputadoModel diputadoModel) {
        PublicFunction funcion = getPublicFunctionOrSave(politicoMapper.mapPublicPunction(diputadoModel));
        period.setPublicFunction(funcion);
    }

    private void constructDistrictRelation(Period period, DiputadoModel diputadoModel) {
        District district = getDistrictOrSave(politicoMapper.mapDistrict(diputadoModel));
        period.setDistrict(district);
    }

    private Project getProjectOrSave(Project project) {
        return projectRepository
                .findById(project.getId())
                .orElseGet(() -> {
                    projectRepository.save(project);
                    projectRepository.flush();
                    return project;
                });
    }

    private ProjectType getProjectTypeOrSave(ProjectType projectType) {
        return projectTypeRepository
                .findByName(projectType.getName())
                .orElseGet(() -> {
                    projectTypeRepository.save(projectType);
                    projectTypeRepository.flush();
                    return projectType;
                });
    }

    private PublicFunction getPublicFunctionOrSave(PublicFunction publicFunction) {
        return publicFunctionRepository
                .findByName(publicFunction.getName())
                .orElseGet(() -> {
                    publicFunctionRepository.save(publicFunction);
                    publicFunctionRepository.flush();
                    return publicFunction;
                });
    }

    private CommissionType getCommissionTypeOrSave(CommissionType tipoComision) {
        return commissionTypeRepository
                .findByName(tipoComision.getName())
                .orElseGet(() -> {
                    commissionTypeRepository.save(tipoComision);
                    commissionTypeRepository.flush();
                    return tipoComision;
                });
    }

    private District getDistrictOrSave(District district) {
        return districtRepository
                .findByName(district.getName())
                .orElseGet(() -> {
                    districtRepository.save(district);
                    districtRepository.flush();
                    return district;
                });
    }

    private Commission getCommissionOrSave(Commission comision) {
        return comisionRepository
                .findByName(comision.getName())
                .orElseGet(() -> {
                    comisionRepository.save(comision);
                    comisionRepository.flush();
                    return comision;
                });
    }

    private CommissionPosition getCommissionPositionOrSave(CommissionPosition commissionPosition) {
        return commissionPositionRepository
                .findByName(commissionPosition.getName())
                .orElseGet(() -> {
                    commissionPositionRepository.save(commissionPosition);
                    commissionPositionRepository.flush();
                    return commissionPosition;
                });
    }

    private Bloc getBlocOrSave(Bloc bloc) {
        return blocRepository
                .findByName(bloc.getName())
                .orElseGet(() -> {
                    blocRepository.save(bloc);
                    blocRepository.flush();
                    return bloc;
                });
    }

    private Profession getProfessionOrSave(String name) {
        return professionRepository
                .findByName(name)
                .orElseGet(() -> {
                    Profession p = new Profession();
                    p.setName(name);
                    professionRepository.save(p);
                    professionRepository.flush();
                    return p;
                });
    }
}
