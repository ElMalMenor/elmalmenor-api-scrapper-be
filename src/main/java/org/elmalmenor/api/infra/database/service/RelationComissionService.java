package org.elmalmenor.api.infra.database.service;

import lombok.RequiredArgsConstructor;
import org.elmalmenor.api.domain.model.ComisionModel;
import org.elmalmenor.api.domain.model.PoliticoModel;
import org.elmalmenor.api.infra.database.mapper.PoliticoMapper;
import org.elmalmenor.api.infra.database.model.Commission;
import org.elmalmenor.api.infra.database.model.CommissionPosition;
import org.elmalmenor.api.infra.database.model.Period;
import org.elmalmenor.api.infra.database.model.PoliticianCommission;
import org.elmalmenor.api.infra.database.repository.ComisionRepository;
import org.elmalmenor.api.infra.database.repository.CommissionPositionRepository;
import org.elmalmenor.api.infra.database.repository.PoliticianCommissionRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static org.elmalmenor.api.utils.Utils.toTitleCase;

@Service
@RequiredArgsConstructor
public class RelationComissionService {

    private final PoliticoMapper politicoMapper;

    private final ComisionRepository comisionRepository;
    private final CommissionPositionRepository commissionPositionRepository;
    private final PoliticianCommissionRepository politicianCommissionRepository;

    private final Set<CommissionPosition> cacheComissionPosition = new HashSet<>();
    private final Set<Commission> cacheComission = new HashSet<>();

    public synchronized void construct(Period period, PoliticoModel politicoModel) {

        if (!Objects.nonNull(politicoModel.getComisiones()))
            return;

        politicoModel.getComisiones().forEach(e -> constructComission(period, e));

    }

    private synchronized void constructComission(Period period, ComisionModel comisionModel) {
        CommissionPosition commissionPosition = getCommissionPositionOrSave(politicoMapper.mapCommissionPosition(comisionModel));
        Commission commission = getCommissionOrSave(politicoMapper.mapComission(comisionModel));

        PoliticianCommission politicoComision = new PoliticianCommission();

        politicoComision.setCommissionPosition(commissionPosition);
        politicoComision.setPeriod(period);
        politicoComision.setCommission(commission);

        politicianCommissionRepository.saveAndFlush(politicoComision);
    }

    private synchronized CommissionPosition getCommissionPositionOrSave(CommissionPosition commissionPosition) {
        commissionPosition.setName(toTitleCase(commissionPosition.getName()));

        return cacheComissionPosition.stream()
                .filter(e -> e.getName().equals(commissionPosition.getName()))
                .findFirst()
                .orElseGet(() -> {
                    Optional<CommissionPosition> optional = commissionPositionRepository.findByName(commissionPosition.getName());

                    if (optional.isPresent()) {
                        cacheComissionPosition.add(optional.get());
                        return optional.get();
                    }

                    commissionPositionRepository.saveAndFlush(commissionPosition);
                    cacheComissionPosition.add(commissionPosition);
                    return commissionPosition;
                });
    }

    private synchronized Commission getCommissionOrSave(Commission comision) {
        comision.setName(toTitleCase(comision.getName()));

        return cacheComission.stream()
                .filter(e -> e.getName().equals(comision.getName()))
                .findFirst()
                .orElseGet(() -> {
                    Optional<Commission> optional = comisionRepository.findByName(comision.getName());

                    if (optional.isPresent()) {
                        cacheComission.add(optional.get());
                        return optional.get();
                    }

                    comisionRepository.saveAndFlush(comision);
                    cacheComission.add(comision);
                    return comision;
                });
    }

}
