package org.elmalmenor.api.infra.scraper.spec;

import org.elmalmenor.api.domain.model.PoliticoModel;

import java.util.stream.Stream;

public interface Scrabble {


    Stream<PoliticoModel> scrap();


}
