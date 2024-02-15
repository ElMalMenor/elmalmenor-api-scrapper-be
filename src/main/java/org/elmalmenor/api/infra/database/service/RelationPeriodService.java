package org.elmalmenor.api.infra.database.service;

import lombok.RequiredArgsConstructor;
import org.elmalmenor.api.domain.model.PoliticoModel;
import org.elmalmenor.api.infra.database.mapper.PoliticoMapper;
import org.elmalmenor.api.infra.database.model.*;
import org.elmalmenor.api.infra.database.repository.*;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
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
    private final PoliticianPartyRepository politicianPartyRepository;
    private final RelationComissionService relationComissionService;

    private final Set<PublicFunction> cachePublicFunction = new HashSet<>();
    private final Set<Bloc> cacheBloc = new HashSet<>();
    private final Set<District> cacheDistrict = new HashSet<>();
    private final Set<PoliticianParty> cachePoliticianParty = new HashSet<>();

    public synchronized void construct(Politician politician, PoliticoModel politicoModel) {

        Period period = new Period();
        period.setStartDate(politicoModel.getMandatoInicio());
        period.setEndDate(politicoModel.getMandatoFin());
        period.setActive(true);

        period.setPolitician(politician);

        Bloc bloc = politicoMapper.mapBloc(politicoModel);
        bloc.setPoliticianParty(getPoliticianPartyOrSave(politicoMapper.mapPoliticianParty(politicoModel)));

        period.setBloc(getBlocOrSave(bloc));

        period.setDistrict(getDistrictOrSave(politicoMapper.mapDistrict(politicoModel)));
        period.setPublicFunction(getPublicFunctionOrSave(politicoMapper.mapPublicPunction(politicoModel)));

        periodReposistory.saveAndFlush(period);

        relationComissionService.construct(period, politicoModel);

    }

    private synchronized PublicFunction getPublicFunctionOrSave(PublicFunction publicFunction) {
        return cachePublicFunction.stream()
                .filter(e -> e.getName().equals(publicFunction.getName()))
                .findFirst()
                .orElseGet(() -> {
                    Optional<PublicFunction> optional = publicFunctionRepository.findByName(publicFunction.getName());

                    if (optional.isPresent()) {
                        cachePublicFunction.add(optional.get());
                        return optional.get();
                    }

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
                    Optional<Bloc> optional = blocRepository.findByName(bloc.getName());

                    if (optional.isPresent()) {
                        cacheBloc.add(optional.get());
                        return optional.get();
                    }

                    blocRepository.saveAndFlush(bloc);
                    cacheBloc.add(bloc);
                    return bloc;
                });
    }

    private synchronized PoliticianParty getPoliticianPartyOrSave(PoliticianParty politicianParty) {
        politicianParty.setName(toTitleCase(politicianParty.getName()));

        return cachePoliticianParty.stream()
                .filter(e -> e.getName().equals(politicianParty.getName()))
                .findFirst()
                .orElseGet(() -> {
                    Optional<PoliticianParty> optional = politicianPartyRepository.findByName(politicianParty.getName());

                    if (optional.isPresent()) {
                        cachePoliticianParty.add(optional.get());
                        return optional.get();
                    }

                    politicianPartyRepository.saveAndFlush(politicianParty);
                    cachePoliticianParty.add(politicianParty);
                    return politicianParty;
                });
    }

    private synchronized District getDistrictOrSave(District district) {
        if (district.getName().contains("CIUDAD AUTÓNOMA DE BUENOS AIRES"))
            district.setName(toTitleCase("CIUDAD DE BUENOS AIRES"));

        if (district.getName().contains("ANTÁRTIDA"))
            district.setName(toTitleCase(district.getName().split(",")[0]));
        else
            district.setName(toTitleCase(district.getName()));

        return cacheDistrict.stream()
                .filter(e -> e.getName().equals(district.getName()))
                .findFirst()
                .orElseGet(() -> {
                    Optional<District> optional = districtRepository.findByName(district.getName());

                    if (optional.isPresent()) {
                        cacheDistrict.add(optional.get());
                        return optional.get();
                    }

                    districtRepository.saveAndFlush(district);
                    cacheDistrict.add(district);
                    return district;
                });
    }

}
