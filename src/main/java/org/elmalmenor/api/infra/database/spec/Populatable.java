package org.elmalmenor.api.infra.database.spec;

import org.elmalmenor.api.domain.model.PoliticoModel;

import java.util.stream.Stream;

public interface Populatable {

    void populateTable(Stream<PoliticoModel> diputados);

}
