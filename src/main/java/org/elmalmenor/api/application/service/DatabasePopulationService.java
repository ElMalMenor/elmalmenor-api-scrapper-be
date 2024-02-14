package org.elmalmenor.api.application.service;

import lombok.RequiredArgsConstructor;
import org.elmalmenor.api.application.spec.DatabasePopulationProcessor;
import org.elmalmenor.api.application.spec.population.DatabaseProcessor;
import org.elmalmenor.api.application.spec.population.ScrapperProcessor;

import org.elmalmenor.api.domain.model.PoliticoModel;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class DatabasePopulationService implements DatabasePopulationProcessor {

    private final DatabaseProcessor databaseProcessor;
    private final ScrapperProcessor scrapperProcessor;

    @Override
    public void processPopulation() {

//        Stream<DiputadoModel> diputados = scrapperProcessor.execScrapperDiputados();
//        databaseProcessor.populateDatabase(diputados);

        Stream<PoliticoModel> senadores = scrapperProcessor.execScrapperSenadores();
        databaseProcessor.populateDatabase(senadores);

    }
}
