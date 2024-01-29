package org.elmalmenor.api.infra.scraper.spec;

import org.elmalmenor.api.domain.model.DiputadoModel;

import java.util.stream.Stream;

public interface Scrabble {


    Stream<DiputadoModel> scrap();


}
