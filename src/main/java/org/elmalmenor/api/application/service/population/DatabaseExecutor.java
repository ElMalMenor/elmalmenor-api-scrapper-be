package org.elmalmenor.api.application.service.population;

import org.elmalmenor.api.application.spec.population.DatabaseProcessor;
import org.elmalmenor.api.domain.model.PoliticoModel;
import org.elmalmenor.api.infra.database.spec.Populatable;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

@Service
public class DatabaseExecutor implements DatabaseProcessor {

    private final Populatable populatable;

    public DatabaseExecutor(Populatable populatable) {
        this.populatable = populatable;
    }

    @Override
    public void populateDatabase(Stream<PoliticoModel> diputados) {
        populatable.populateTable(diputados);
    }
}
