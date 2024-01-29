package org.elmalmenor.api.application.service;

import lombok.RequiredArgsConstructor;
import org.elmalmenor.api.application.spec.DatabasePopulationProcessor;
import org.elmalmenor.api.application.spec.population.DatabaseProcessor;
import org.elmalmenor.api.application.spec.population.ScrapperProcessor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DatabasePopulationService implements DatabasePopulationProcessor {

    private final DatabaseProcessor databaseProcessor;
    private final ScrapperProcessor scrapperProcessor;

    @Override
    public void processPopulation() {

        databaseProcessor.populateDatabase(
                scrapperProcessor.execScrapperDiputados()
        );

    }
}
