package org.elmalmenor.api.application.spec.population;

import org.elmalmenor.api.domain.model.PoliticoModel;

import java.util.stream.Stream;

public interface ScrapperProcessor {

    Stream<PoliticoModel> execScrapperDiputados();
    Stream<PoliticoModel> execScrapperSenadores();

}
