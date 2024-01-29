package org.elmalmenor.api.application.service.population;

import org.elmalmenor.api.application.spec.population.ScrapperProcessor;
import org.elmalmenor.api.domain.model.DiputadoModel;
import org.elmalmenor.api.infra.scraper.spec.Scrabble;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

@Service
public class ScrapperExecutor implements ScrapperProcessor {

    private final Scrabble scrabble;

    @Autowired
    public ScrapperExecutor(Scrabble scrabble) {
        this.scrabble = scrabble;
    }

    @Override
    public Stream<DiputadoModel> execScrapperDiputados() {
        return scrabble.scrap();
    }
}
