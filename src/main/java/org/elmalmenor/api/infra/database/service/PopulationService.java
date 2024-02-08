package org.elmalmenor.api.infra.database.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.elmalmenor.api.domain.model.DiputadoModel;
import org.elmalmenor.api.infra.database.mapper.PoliticoMapper;
import org.elmalmenor.api.infra.database.model.*;
import org.elmalmenor.api.infra.database.repository.*;
import org.elmalmenor.api.infra.database.spec.Populatable;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class PopulationService implements Populatable {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

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

        diputados.parallel().forEach(e -> {

            if (politicianRepository.findByFirstNameAndLastName(e.getNombre(), e.getApellido()).isEmpty()) {

                LOGGER.info("Procesando Diputado...: {} {}", e.getNombre(), e.getApellido());
                Politician politico = politicoMapper.map(e);

                constructProyectosRelation(politico, e);
                constructProfessionRelation(politico,  e);

                politicianRepository.save(politico);
                politicianRepository.flush();

                constructPeriodRelation(politico, e);

            }

        });

        LOGGER.info("Proceso Terminado");

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

        diputadoModel.getProyectos().parallelStream().forEach(p -> {
            if (!p.getSumario().trim().isEmpty()) {
                ProjectType projectType = getProjectTypeOrSave(politicoMapper.mapProjectType(p));

                Project project = politicoMapper.mapProject(p);
                project.setProjectType(projectType);

                projects.add(getProjectOrSave(project));
            }
        });

        politician.setProjects(projects);

    }

    private void constructProfessionRelation(Politician politician, DiputadoModel diputadoModel) {

        List<String> toNormalize = toListWithDivider(diputadoModel.getProfesion());

        Set<Profession> professions = toNormalize.stream()
                .map(this::getProfessionOrSave)
                .collect(Collectors.toSet());

        politician.setProfessions(professions);
    }

    private synchronized void constructBlocRelation(Period period, DiputadoModel diputadoModel) {
        Bloc bloque = getBlocOrSave(politicoMapper.mapBloc(diputadoModel));
        period.setBloc(bloque);
    }

    private synchronized void constructFuncionRelation(Period period, DiputadoModel diputadoModel) {
        PublicFunction funcion = getPublicFunctionOrSave(politicoMapper.mapPublicPunction(diputadoModel));
        period.setPublicFunction(funcion);
    }

    private synchronized void constructDistrictRelation(Period period, DiputadoModel diputadoModel) {
        District district = getDistrictOrSave(politicoMapper.mapDistrict(diputadoModel));
        period.setDistrict(district);
    }

    private synchronized Project getProjectOrSave(Project project) {
        return projectRepository
                .findById(project.getId())
                .orElseGet(() -> {
                    projectRepository.save(project);
                    projectRepository.flush();
                    return project;
                });
    }

    private synchronized ProjectType getProjectTypeOrSave(ProjectType projectType) {
        projectType.setName(toTitleCase(projectType.getName()));

        return projectTypeRepository
                .findByName(projectType.getName())
                .orElseGet(() -> {
                    projectTypeRepository.save(projectType);
                    projectTypeRepository.flush();
                    return projectType;
                });
    }

    private synchronized PublicFunction getPublicFunctionOrSave(PublicFunction publicFunction) {
        return publicFunctionRepository
                .findByName(publicFunction.getName())
                .orElseGet(() -> {
                    publicFunctionRepository.save(publicFunction);
                    publicFunctionRepository.flush();
                    return publicFunction;
                });
    }

    private synchronized CommissionType getCommissionTypeOrSave(CommissionType tipoComision) {
        tipoComision.setName(toTitleCase(tipoComision.getName()));

        return commissionTypeRepository
                .findByName(tipoComision.getName())
                .orElseGet(() -> {
                    commissionTypeRepository.save(tipoComision);
                    commissionTypeRepository.flush();
                    return tipoComision;
                });
    }

    private synchronized District getDistrictOrSave(District district) {
        district.setName(toTitleCase(district.getName()));

        return districtRepository
                .findByName(district.getName())
                .orElseGet(() -> {
                    districtRepository.save(district);
                    districtRepository.flush();
                    return district;
                });
    }

    private synchronized Commission getCommissionOrSave(Commission comision) {
        comision.setName(toTitleCase(comision.getName()));

        return comisionRepository
                .findByName(comision.getName())
                .orElseGet(() -> {
                    comisionRepository.save(comision);
                    comisionRepository.flush();
                    return comision;
                });
    }

    private synchronized CommissionPosition getCommissionPositionOrSave(CommissionPosition commissionPosition) {
        commissionPosition.setName(toTitleCase(commissionPosition.getName()));

        return commissionPositionRepository
                .findByName(commissionPosition.getName())
                .orElseGet(() -> {
                    commissionPositionRepository.save(commissionPosition);
                    commissionPositionRepository.flush();
                    return commissionPosition;
                });
    }

    private synchronized Bloc getBlocOrSave(Bloc bloc) {
        bloc.setName(toTitleCase(bloc.getName()));

        return blocRepository
                .findByName(bloc.getName())
                .orElseGet(() -> {
                    blocRepository.save(bloc);
                    blocRepository.flush();
                    return bloc;
                });
    }

    private synchronized Profession getProfessionOrSave(String name) {
        String nameCase = toTitleCase(name);

        return professionRepository
                .findByName(nameCase)
                .orElseGet(() -> {
                    Profession p = new Profession();
                    p.setName(nameCase);
                    professionRepository.save(p);
                    professionRepository.flush();
                    return p;
                });
    }

    private List<String> toListWithDivider(String string) {

        List<String> words;

        switch (string) {
            case String s && (s.contains("y")) -> words = Arrays.asList(string.split("y"));
            case String s && (s.contains("-")) -> words = Arrays.asList(string.split("-"));
            case String s && (s.contains("/")) ->  {
                words = Arrays.asList(string.split("/"));

                if (words.get(words.size()-1).equals("a") || words.get(words.size()-1).equals("o")) {
                    words.remove(words.size()-1);
                }
            }
            default -> words = List.of(string);
        }

        return words.stream().map(String::trim).collect(Collectors.toList());
    }

    private String toTitleCase(String string) {
        if (string.trim().isEmpty()) {
            return "No Definido";
        }

        List<String> list = List.of(string.split(" "));

        return IntStream.range(0 , list.size())
                .mapToObj(i -> {
                    String word = list.get(i).toLowerCase();

                    if (i != 0 && checkBannedWords(word)) {
                        return word;
                    }

                    return word.substring(0, 1).toUpperCase() + word.substring(1);
                })
                .collect(Collectors.joining(" "));

    }

    private boolean checkBannedWords(String string) {
        return List.of("por", "y", "de", "del", "la", "las", "los", "lo", "en", "e").contains(string);
    }
}
