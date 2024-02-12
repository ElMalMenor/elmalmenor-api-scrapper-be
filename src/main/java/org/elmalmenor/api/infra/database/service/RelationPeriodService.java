package org.elmalmenor.api.infra.database.service;

import lombok.RequiredArgsConstructor;
import org.elmalmenor.api.domain.model.DiputadoModel;
import org.elmalmenor.api.infra.database.mapper.PoliticoMapper;
import org.elmalmenor.api.infra.database.model.*;
import org.elmalmenor.api.infra.database.repository.BlocRepository;
import org.elmalmenor.api.infra.database.repository.DistrictRepository;
import org.elmalmenor.api.infra.database.repository.PeriodReposistory;
import org.elmalmenor.api.infra.database.repository.PublicFunctionRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

import static org.elmalmenor.api.utils.Utils.toTitleCase;

@Service
@RequiredArgsConstructor
public class RelationPeriodService {

    private final PoliticoMapper politicoMapper;

    private final DistrictRepository districtRepository;
    private final BlocRepository blocRepository;
    private final PeriodReposistory periodReposistory;
    private final PublicFunctionRepository publicFunctionRepository;

    private final RelationComissionService relationComissionService;

    private final Set<PublicFunction> cachePublicFunction = new HashSet<>();
    private final Set<Bloc> cacheBloc = new HashSet<>();
    private final Set<District> cacheDistrict = new HashSet<>();

    public synchronized void construct(Politician politician, DiputadoModel diputadoModel) {

        Period period = new Period();
        period.setStartDate(diputadoModel.getMandatoInicio());
        period.setEndDate(diputadoModel.getMandatoFin());
        period.setActive(true);

        period.setPolitician(politician);
        period.setBloc(getBlocOrSave(politicoMapper.mapBloc(diputadoModel)));
        period.setDistrict(getDistrictOrSave(politicoMapper.mapDistrict(diputadoModel)));
        period.setPublicFunction(getPublicFunctionOrSave(politicoMapper.mapPublicPunction(diputadoModel)));

        periodReposistory.saveAndFlush(period);

        relationComissionService.construct(period, diputadoModel);

    }

    private synchronized PublicFunction getPublicFunctionOrSave(PublicFunction publicFunction) {
        return cachePublicFunction.stream()
                .filter(e -> e.getName().equals(publicFunction.getName()))
                .findFirst()
                .orElseGet(() -> {
                    publicFunctionRepository.saveAndFlush(publicFunction);
                    cachePublicFunction.add(publicFunction);
                    return publicFunction;
                });
    }

    private synchronized Bloc getBlocOrSave(Bloc bloc) {
        bloc.setName(toTitleCase(bloc.getName()));

        return cacheBloc.stream()
                .filter(e -> e.getName().equals(bloc.getName()))
                .findFirst()
                .orElseGet(() -> {
                    blocRepository.saveAndFlush(bloc);
                    cacheBloc.add(bloc);
                    return bloc;
                });
    }

    private synchronized District getDistrictOrSave(District district) {
        district.setName(toTitleCase(district.getName()));

        return cacheDistrict.stream()
                .filter(e -> e.getName().equals(district.getName()))
                .findFirst()
                .orElseGet(() -> {
                    districtRepository.saveAndFlush(district);
                    cacheDistrict.add(district);
                    return district;
                });
    }

}
