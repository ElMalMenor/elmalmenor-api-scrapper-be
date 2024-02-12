package org.elmalmenor.api.infra.database.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.elmalmenor.api.domain.model.DiputadoModel;
import org.elmalmenor.api.infra.database.spec.Populatable;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class PopulationService implements Populatable {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private final RelationPoliticianService relationPoliticianService;

    @Override
    public void populateTable(Stream<DiputadoModel> diputados) {

        LOGGER.info("Proceso Iniciado");

        diputados.parallel().forEach(relationPoliticianService::construct);

        LOGGER.info("Proceso Terminado");

    }

}
