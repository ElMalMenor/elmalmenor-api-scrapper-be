package org.elmalmenor.api.application.spec.population;

import org.elmalmenor.api.domain.model.DiputadoModel;

import java.util.stream.Stream;

public interface DatabaseProcessor {

    void populateDatabase(Stream<DiputadoModel> diputados);

}
