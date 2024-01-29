package org.elmalmenor.api.infra.database.spec;

import org.elmalmenor.api.domain.model.DiputadoModel;

import java.util.stream.Stream;

public interface Populatable {

    void populateTable(Stream<DiputadoModel> diputados);

}
